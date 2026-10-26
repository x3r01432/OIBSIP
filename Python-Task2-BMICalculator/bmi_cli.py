"""Beginner tier: command-line BMI calculator."""


def classify(bmi: float) -> str:
    if bmi < 18.5:
        return "Underweight"
    if bmi < 25:
        return "Normal"
    if bmi < 30:
        return "Overweight"
    return "Obese"


def calc_bmi(weight: float, height: float) -> float:
    if weight <= 0 or height <= 0:
        raise ValueError("Weight and height must be positive numbers.")
    return weight / (height ** 2)


def ask(prompt: str) -> float:
    while True:
        raw = input(prompt).strip()
        try:
            val = float(raw)
        except ValueError:
            print("  Error: please enter a number (e.g. 70 or 1.75).")
            continue
        if val <= 0:
            print("  Error: value must be greater than zero.")
            continue
        return val


def main():
    print("=== BMI Calculator ===")
    w = ask("Weight (kg): ")
    h = ask("Height (m): ")
    bmi = calc_bmi(w, h)
    print(f"Your BMI is {bmi:.2f} -> {classify(bmi)}")


if __name__ == "__main__":
    main()
