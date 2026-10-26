"""Advanced tier: tkinter BMI calculator with multi-user SQLite history and trend chart."""
import sqlite3
import tkinter as tk
from datetime import datetime
from tkinter import messagebox

from bmi_cli import calc_bmi, classify

DB = "bmi_history.db"
COLORS = {"Underweight": "#f0ad4e", "Normal": "#28a745", "Overweight": "#fd7e14", "Obese": "#dc3545"}


def db():
    conn = sqlite3.connect(DB)
    conn.execute("CREATE TABLE IF NOT EXISTS records(user TEXT, ts TEXT, weight REAL, height REAL, bmi REAL)")
    return conn


def save(user, w, h, bmi):
    try:
        with db() as c:
            c.execute("INSERT INTO records VALUES(?,?,?,?,?)", (user, datetime.now().isoformat(timespec="seconds"), w, h, bmi))
    except sqlite3.Error as e:
        messagebox.showerror("Database error", f"Could not save record: {e}")


def history(user):
    try:
        with db() as c:
            return c.execute("SELECT ts, bmi FROM records WHERE user=? ORDER BY ts", (user,)).fetchall()
    except sqlite3.Error as e:
        messagebox.showerror("Database error", f"Could not read history: {e}")
        return []


class App(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("BMI Calculator")
        self.geometry("360x260")
        self.entries = {}
        for i, (key, label) in enumerate([("user", "Name"), ("w", "Weight (kg)"), ("h", "Height (m)")]):
            tk.Label(self, text=label).grid(row=i, column=0, padx=10, pady=6, sticky="e")
            e = tk.Entry(self)
            e.grid(row=i, column=1, padx=10)
            self.entries[key] = e
        tk.Button(self, text="Calculate", command=self.calculate).grid(row=3, column=0, columnspan=2, pady=8)
        tk.Button(self, text="Show Trend", command=self.trend).grid(row=4, column=0, columnspan=2)
        self.result = tk.Label(self, text="", font=("Arial", 14, "bold"))
        self.result.grid(row=5, column=0, columnspan=2, pady=10)

    def calculate(self):
        user = self.entries["user"].get().strip() or "default"
        try:
            w, h = float(self.entries["w"].get()), float(self.entries["h"].get())
            bmi = calc_bmi(w, h)
        except ValueError:
            self.result.config(text="Enter positive numbers", fg="red")
            return
        cat = classify(bmi)
        self.result.config(text=f"BMI {bmi:.2f} - {cat}", fg=COLORS[cat])
        save(user, w, h, bmi)

    def trend(self):
        import matplotlib.pyplot as plt
        user = self.entries["user"].get().strip() or "default"
        rows = history(user)
        if not rows:
            messagebox.showinfo("No data", f"No records for {user}")
            return
        plt.figure(figsize=(6, 3.5))
        plt.plot([r[0] for r in rows], [r[1] for r in rows], marker="o")
        plt.axhspan(18.5, 25, color="green", alpha=0.1, label="Normal range")
        plt.title(f"BMI trend for {user}")
        plt.xticks(rotation=30, ha="right")
        plt.legend()
        plt.tight_layout()
        plt.show()


if __name__ == "__main__":
    App().mainloop()
