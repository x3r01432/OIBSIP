# Security · Task 9 — SQL Injection (Advanced)

## Executive summary (for a non-technical manager)
Our test application lets anyone who can reach its login page take complete control of its database by typing special characters into a normal form field — **no password or hacking tools required**. Using this flaw we bypassed the login entirely and extracted every user's name, password, and **Social Security Number**. **Risk level: Critical.** For a real business this would mean a reportable data breach, regulatory penalties, and loss of customer trust. The fix is well understood and moderate in effort (parameterised queries); it should be treated as an emergency.

## Target & setup
Extends Task 3 against the same self-owned local **Vuln Bank** app ([`vulnerable_app/`](vulnerable_app/)), demonstrating schema enumeration and data exfiltration equivalent to DVWA Medium.
```bash
cd vulnerable_app && php init_db.php && php -S 127.0.0.1:8080
bash ../sql_injection_exploit.sh   # documented manual steps
```

## Exploitation performed (full log: [`sql_injection_exploit_log.md`](sql_injection_exploit_log.md))
1. **Confirm injection** — `id=1'` breaks the query; `ORDER BY 2`/`3` reveals 2 columns.
2. **Enumerate schema** — `UNION SELECT name,sql FROM sqlite_master` dumps all tables and their DDL.
3. **Extract columns** — recovered full `users` table definition (id, username, password, first_name, last_name, ssn).
4. **Exfiltrate data** — dumped credentials and **name + SSN** for every user.
5. **Auth bypass** — `admin' -- ` logs in with no password.

Automated tooling note: `sqlmap` would automate steps 1–4; the manual payloads above produce the same results and are documented for learning. Burp Suite would be used to intercept/replay these GET requests (the raw request is a simple `GET /login.php?id=...`).

## Remediation (developer-facing)
**Why it exists:** user input is concatenated into SQL, so input becomes executable code.

**PHP (PDO prepared statement):**
```php
$stmt = $pdo->prepare('SELECT * FROM users WHERE username = ? AND password = ?');
$stmt->execute([$user, $hash]);        // input is bound as data, never code
```

**Python (parameterised query):**
```python
cur.execute("SELECT * FROM users WHERE id = ?", (user_id,))  # driver escapes safely
```
Additionally: validate/whitelist input, store passwords with **bcrypt/argon2** (not MD5), apply least-privilege DB accounts, and return generic error messages.

> **Note on screenshots:** this work ran in a headless environment with no GUI, so terminal/tool output is captured verbatim as text files in `output/` (these are the evidence in lieu of GUI screenshots).

> ⚠️ **Ethics:** All scanning/exploitation in this task targeted only `127.0.0.1` on a host I own and operate. Never run these tools against systems you do not own or lack written authorisation to test.
