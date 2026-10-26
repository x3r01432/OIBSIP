"""Advanced tier: Flask web chat with SQLite users/rooms/history, emoji shortcodes, notifications.
Real-time updates via lightweight long-polling (no extra socket library needed)."""
import sqlite3
import time
from datetime import datetime

from flask import Flask, g, jsonify, redirect, render_template, request, session, url_for
from werkzeug.security import check_password_hash, generate_password_hash

app = Flask(__name__)
app.secret_key = "change-me-in-production"
DB = "chat.db"
EMOJI = {":smile:": "😄", ":heart:": "❤️", ":thumbsup:": "👍", ":laughing:": "😆", ":fire:": "🔥",
         ":cry:": "😢", ":wave:": "👋", ":tada:": "🎉", ":wink:": "😉", ":rocket:": "🚀"}


def db():
    if "db" not in g:
        g.db = sqlite3.connect(DB)
        g.db.row_factory = sqlite3.Row
        g.db.executescript("""
            CREATE TABLE IF NOT EXISTS users(username TEXT PRIMARY KEY, pw_hash TEXT);
            CREATE TABLE IF NOT EXISTS rooms(name TEXT PRIMARY KEY);
            CREATE TABLE IF NOT EXISTS messages(id INTEGER PRIMARY KEY AUTOINCREMENT,
                room TEXT, username TEXT, body TEXT, ts TEXT);
            INSERT OR IGNORE INTO rooms VALUES('general');""")
    return g.db


@app.teardown_appcontext
def close(_):
    if "db" in g:
        g.db.close()


def emojify(text):
    for k, v in EMOJI.items():
        text = text.replace(k, v)
    return text


@app.route("/", methods=["GET", "POST"])
def login():
    error = None
    if request.method == "POST":
        u, p = request.form["username"].strip(), request.form["password"]
        row = db().execute("SELECT pw_hash FROM users WHERE username=?", (u,)).fetchone()
        if request.form.get("action") == "register":
            if row:
                error = "Username taken"
            elif not u or len(p) < 4:
                error = "Username required, password min 4 chars"
            else:
                db().execute("INSERT INTO users VALUES(?,?)", (u, generate_password_hash(p))); db().commit()
                session["user"] = u
                return redirect(url_for("rooms"))
        elif row and check_password_hash(row["pw_hash"], p):
            session["user"] = u
            return redirect(url_for("rooms"))
        else:
            error = "Invalid credentials"
    return render_template("login.html", error=error)


@app.route("/rooms", methods=["GET", "POST"])
def rooms():
    if "user" not in session:
        return redirect(url_for("login"))
    if request.method == "POST" and request.form.get("room", "").strip():
        name = request.form["room"].strip().lower()
        db().execute("INSERT OR IGNORE INTO rooms VALUES(?)", (name,)); db().commit()
        return redirect(url_for("room", name=name))
    names = [r["name"] for r in db().execute("SELECT name FROM rooms ORDER BY name")]
    return render_template("rooms.html", rooms=names, user=session["user"])


@app.route("/room/<name>")
def room(name):
    if "user" not in session:
        return redirect(url_for("login"))
    return render_template("room.html", room=name, user=session["user"])


@app.route("/api/<room>/messages")
def messages(room):
    after = int(request.args.get("after", 0))
    deadline = time.time() + 20  # long-poll up to 20s
    while True:
        rows = db().execute("SELECT * FROM messages WHERE room=? AND id>? ORDER BY id", (room, after)).fetchall()
        if rows or time.time() > deadline or request.args.get("wait") != "1":
            return jsonify([dict(r) for r in rows])
        time.sleep(0.5)


@app.route("/api/<room>/send", methods=["POST"])
def send(room):
    if "user" not in session:
        return jsonify(error="login required"), 401
    body = emojify(request.json.get("body", "").strip())
    if body:
        db().execute("INSERT INTO messages(room, username, body, ts) VALUES(?,?,?,?)",
                     (room, session["user"], body, datetime.now().strftime("%H:%M"))); db().commit()
    return jsonify(ok=True)


@app.route("/logout")
def logout():
    session.clear()
    return redirect(url_for("login"))


if __name__ == "__main__":
    app.run(debug=False, threaded=True, port=5000)
