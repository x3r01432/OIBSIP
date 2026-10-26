"""Advanced tier: tkinter password generator using the secrets module."""
import secrets
import string
import tkinter as tk

try:
    import pyperclip
except ImportError:  # clipboard falls back to tkinter's own clipboard
    pyperclip = None

AMBIGUOUS = set("0O1lI")
TYPES = [("Uppercase", string.ascii_uppercase), ("Lowercase", string.ascii_lowercase),
         ("Numbers", string.digits), ("Symbols", string.punctuation)]


def secure_password(length, pools, exclude_ambiguous=False):
    if exclude_ambiguous:
        pools = ["".join(c for c in p if c not in AMBIGUOUS) for p in pools]
    if len(pools) < 2:
        raise ValueError("Select at least 2 character types")
    pwd = [secrets.choice(p) for p in pools]   # guarantee one of each selected type
    allc = "".join(pools)
    pwd += [secrets.choice(allc) for _ in range(length - len(pwd))]
    secrets.SystemRandom().shuffle(pwd)
    return "".join(pwd)


def strength(pwd):
    kinds = sum(any(c in p for c in pwd) for _, p in TYPES)
    score = (len(pwd) >= 12) + (len(pwd) >= 16) + (kinds >= 3) + (kinds == 4)
    return ("Weak", "red", 25) if score <= 1 else ("Medium", "orange", 60) if score <= 2 else ("Strong", "green", 100)


class App(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("Secure Password Generator")
        self.geometry("420x400")
        self.history = []
        tk.Label(self, text="Length").pack()
        self.length = tk.Scale(self, from_=8, to=64, orient="horizontal", length=300)
        self.length.set(16)
        self.length.pack()
        self.vars = []
        for name, _ in TYPES:
            v = tk.BooleanVar(value=True)
            tk.Checkbutton(self, text=name, variable=v).pack(anchor="w", padx=60)
            self.vars.append(v)
        self.ambig = tk.BooleanVar()
        tk.Checkbutton(self, text="Exclude ambiguous (0 O 1 l I)", variable=self.ambig).pack(anchor="w", padx=60)
        tk.Button(self, text="Generate & Copy", command=self.generate).pack(pady=6)
        self.out = tk.Entry(self, width=44, font=("Courier", 11), justify="center")
        self.out.pack()
        self.bar = tk.Canvas(self, width=300, height=12, bg="#eee", highlightthickness=0)
        self.bar.pack(pady=4)
        self.label = tk.Label(self, text="")
        self.label.pack()
        tk.Label(self, text="Last 5 (session only, not saved):").pack()
        self.hist = tk.Label(self, text="", font=("Courier", 9), justify="left")
        self.hist.pack()

    def generate(self):
        pools = [p for (_, p), v in zip(TYPES, self.vars) if v.get()]
        try:
            pwd = secure_password(self.length.get(), pools, self.ambig.get())
        except ValueError as e:
            self.label.config(text=str(e), fg="red")
            return
        self.out.delete(0, "end")
        self.out.insert(0, pwd)
        if pyperclip:
            try:
                pyperclip.copy(pwd)
            except Exception:
                self.clipboard_clear(); self.clipboard_append(pwd)
        else:
            self.clipboard_clear(); self.clipboard_append(pwd)
        name, color, pct = strength(pwd)
        self.bar.delete("all")
        self.bar.create_rectangle(0, 0, 3 * pct, 12, fill=color, width=0)
        self.label.config(text=f"Strength: {name} (copied to clipboard)", fg=color)
        self.history = ([pwd] + self.history)[:5]
        self.hist.config(text="\n".join(self.history))


if __name__ == "__main__":
    App().mainloop()
