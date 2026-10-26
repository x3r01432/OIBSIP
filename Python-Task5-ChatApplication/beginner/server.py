"""Beginner tier chat server: relays messages between connected clients (socket + threading)."""
import socket
import threading
from datetime import datetime

HOST, PORT = "127.0.0.1", 5555
clients = {}  # conn -> name
lock = threading.Lock()


def stamp(msg):
    return f"[{datetime.now():%H:%M}] {msg}"


def broadcast(msg, exclude=None):
    with lock:
        for c in list(clients):
            if c is not exclude:
                try:
                    c.sendall((msg + "\n").encode())
                except OSError:
                    pass


def handle(conn):
    buf = ""
    while "\n" not in buf:  # first line is the user's name
        data = conn.recv(1024)
        if not data:
            conn.close()
            return
        buf += data.decode()
    name, buf = buf.split("\n", 1)
    name = name.strip() or "anon"
    with lock:
        clients[conn] = name
    broadcast(stamp(f"*** {name} joined the chat ***"), exclude=conn)
    try:
        while "\n" in buf:
            line, buf = buf.split("\n", 1)
            if line.strip():
                broadcast(stamp(f"{name}: {line.strip()}"), exclude=conn)
        while True:
            data = conn.recv(1024)
            if not data:
                break
            buf += data.decode()
            while "\n" in buf:
                line, buf = buf.split("\n", 1)
                if line.strip():
                    broadcast(stamp(f"{name}: {line.strip()}"), exclude=conn)
    except OSError:
        pass
    finally:
        with lock:
            clients.pop(conn, None)
        conn.close()
        broadcast(stamp(f"*** {name} disconnected ***"))


def main():
    srv = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    srv.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    srv.bind((HOST, PORT))
    srv.listen()
    print(f"Chat server listening on {HOST}:{PORT}", flush=True)
    while True:
        conn, _ = srv.accept()
        threading.Thread(target=handle, args=(conn,), daemon=True).start()


if __name__ == "__main__":
    main()
