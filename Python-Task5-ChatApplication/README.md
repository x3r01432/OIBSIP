# Python Programming — Task 5: Chat Application

## Beginner tier — `beginner/` (socket + threading)
- `server.py` listens on `127.0.0.1:5555` and relays messages between clients
- `client.py <name>` connects; bidirectional real-time messaging
- Timestamped lines: `[14:35] Alice: Hello`
- Join / disconnect notices sent to the other client(s)
```bash
python beginner/server.py          # terminal 1
python beginner/client.py Alice    # terminal 2
python beginner/client.py Bob      # terminal 3
```

## Advanced tier — `advanced/` (Flask web app + SQLite)
- Registration & login (passwords hashed with Werkzeug PBKDF2, stored in SQLite)
- Create/join named rooms; history loaded on join
- Near-real-time updates via HTTP long-polling
- Browser notifications for new messages when the tab is hidden
- Emoji shortcodes (`:smile:` → 😄, `:rocket:` → 🚀, …)
```bash
pip install flask && python advanced/app.py   # http://localhost:5000
```

## Security transparency
- Messages are stored **in plaintext** in `chat.db` (SQLite) on the server; anyone with file access can read them.
- **No end-to-end encryption.** Traffic is plain HTTP / raw TCP unless you put it behind TLS (e.g. a reverse proxy with HTTPS).
- Passwords are **hashed** (never stored in plaintext). The Flask `secret_key` must be changed for real deployments.
- The beginner socket chat has no authentication or encryption at all.

Test output in `screenshots/`.

Author: Steven Manuel Rios
