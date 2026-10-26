# Python Programming — Task 3: Random Password Generator

| Tier | File | Features |
|---|---|---|
| Beginner | `password_cli.py` | length ≥ 8 enforced, choose ≥ 2 char types, validation, "generate another" loop (`random`, `string`) |
| Advanced | `password_gui.py` | tkinter slider + checkboxes, `secrets` (CSPRNG), strength bar (Weak/Medium/Strong), guaranteed one char per selected type, auto copy to clipboard (pyperclip, falls back to tkinter clipboard), exclude ambiguous chars, last-5 session history (never written to disk) |

## Run
```bash
python password_cli.py
pip install pyperclip && python password_gui.py
```
Screenshots/output in `screenshots/`.

Author: Steven Manuel Rios
