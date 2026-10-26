"""Beginner tier chat client. Usage: python client.py <name>"""
import socket
import sys
import threading
from datetime import datetime

HOST, PORT = "127.0.0.1", 5555


def receive(sock):
    while True:
        try:
            data = sock.recv(1024)
        except OSError:
            break
        if not data:
            print("*** Server closed the connection ***")
            break
        print(data.decode(), end="", flush=True)


def main():
    name = sys.argv[1] if len(sys.argv) > 1 else input("Your name: ")
    sock = socket.create_connection((HOST, PORT))
    sock.sendall((name + "\n").encode())
    threading.Thread(target=receive, args=(sock,), daemon=True).start()
    print("Connected. Type messages, /quit to exit.")
    try:
        for line in sys.stdin:
            if line.strip() == "/quit":
                break
            sock.sendall(line.encode())
            print(f"[{datetime.now():%H:%M}] You: {line.strip()}")
    finally:
        sock.close()


if __name__ == "__main__":
    main()
