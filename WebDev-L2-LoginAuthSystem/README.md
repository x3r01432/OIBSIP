# WebDev Level 2 — Task 4: Login Authentication System

Approach **(A) front-end only** — HTML/CSS/JS with localStorage.

## Features
- Registration with username/email + password
- Password validation: min 8 chars, at least 1 number
- Duplicate user check with error
- Login page with username/email + password
- Generic "Invalid credentials" (doesn't reveal which field)
- Protected dashboard — redirects to login if no session
- Logout clears the session and redirects
- Passwords hashed with SHA-256 (Web Crypto API) — never stored in plain text
- Basic validation (no empty submissions)

## Tech Stack
HTML5, CSS3, Vanilla JavaScript, localStorage + sessionStorage.

## Note
Client-side hashing is for demonstration; a production system must hash server-side
(bcrypt/argon2) over HTTPS. See the full-stack alternative in the Level 3 task.

## Run
Open `index.html`. Register, then log in to reach the protected dashboard.

Author: Steven Manuel Rios
