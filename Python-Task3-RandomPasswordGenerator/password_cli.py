"""Beginner tier: command-line random password generator (random + string)."""
import random
import string

POOLS = {"u": string.ascii_uppercase, "l": string.ascii_lowercase, "n": string.digits, "s": string.punctuation}


def generate(length: int, kinds: str) -> str:
    if length < 8:
        raise ValueError("Length must be at least 8.")
    chosen = [POOLS[k] for k in set(kinds) if k in POOLS]
    if len(chosen) < 2:
        raise ValueError("Select at least 2 character types.")
    pwd = [random.choice(p) for p in chosen]          # one of each selected type
    allc = "".join(chosen)
    pwd += [random.choice(allc) for _ in range(length - len(pwd))]
    random.shuffle(pwd)
    return "".join(pwd)


def main():
    print("=== Random Password Generator ===")
    while True:
        try:
            length = int(input("Length (min 8): "))
            kinds = input("Types: u=upper l=lower n=numbers s=symbols (e.g. ulns): ").strip().lower()
            print("Password:", generate(length, kinds))
        except ValueError as e:
            print("  Error:", e if str(e)[0].isupper() else "please enter a whole number.")
            continue
        if input("Generate another? (y/n): ").strip().lower() != "y":
            break


if __name__ == "__main__":
    main()
