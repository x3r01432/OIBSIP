# Security · Task 3 — SQL Injection (Basic)

## What is SQL Injection?
SQL injection (SQLi) happens when user input is concatenated directly into a SQL query. By including SQL syntax (quotes, `OR`, `UNION`, comments) in the input, an attacker changes the *meaning* of the query — bypassing logins, reading data they shouldn't, or dumping the whole database.

## The target
DVWA requires a full LAMP/MySQL stack and a VM. To demonstrate the identical vulnerability in this headless environment, I built an equivalent **intentionally-vulnerable local PHP app ("Vuln Bank")** — source in [`vulnerable_app/`](vulnerable_app/) — that concatenates GET parameters straight into SQLite queries, exactly as DVWA's "Low" setting does. It runs only on `127.0.0.1:8080`.

Setup:
```bash
cd vulnerable_app && php init_db.php && php -S 127.0.0.1:8080
```

## Payloads & results
Full log with live output: [`sql_injection_notes.md`](sql_injection_notes.md).
- `id=1 OR 1=1` — tautology returns rows.
- `id=0 UNION SELECT username,password FROM users` — **dumps all credentials**.
- `user=admin' -- ` — **authentication bypass** (comments out the password check → LOGIN SUCCESS).
- `user=x' OR '1'='1` — tautology login bypass.

**Data exposed:** every username, (MD5) password hash, and — via Task 9 — full names and SSNs.

## Why the payload works & how to fix it
`' OR '1'='1` turns `WHERE username='x' OR '1'='1'` into an always-true condition; `--` comments out the rest of the query. The root cause is **mixing code and data**. The fix is **parameterised queries / prepared statements**, which send the SQL structure and the user data separately so input can never be executed as code (see Task 9 for code examples).

> **Note on screenshots:** this work ran in a headless environment with no GUI, so terminal/tool output is captured verbatim as text files in `output/` (these are the evidence in lieu of GUI screenshots).

> ⚠️ **Ethics:** All scanning/exploitation in this task targeted only `127.0.0.1` on a host I own and operate. Never run these tools against systems you do not own or lack written authorisation to test.
