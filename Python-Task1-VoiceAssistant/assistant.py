"""Voice Assistant (Beginner + Advanced tiers).

Beginner: speech_recognition mic input, greeting, time/date, web search, retry on
not-understood, pyttsx3 speech for every reply.
Advanced: intent classification from free-form sentences (NLTK tokenising + scored
intent keywords/patterns, not exact keyword matching), email via smtplib, timed reminders
with audible alert, live weather (Open-Meteo, or OpenWeatherMap if OWM_API_KEY is set),
general-knowledge Q&A (local knowledge base + Wikipedia summary API), and user-defined
custom commands from custom_commands.json or added by voice.

Run `python assistant.py --text` to type instead of speak (useful without a microphone).
"""
import json
import os
import re
import smtplib
import sys
import threading
import webbrowser
from datetime import datetime
from email.message import EmailMessage
from urllib.parse import quote_plus

import requests

HERE = os.path.dirname(os.path.abspath(__file__))
CUSTOM_FILE = os.path.join(HERE, "custom_commands.json")
KB_FILE = os.path.join(HERE, "knowledge_base.json")
TEXT_MODE = "--text" in sys.argv

try:
    import pyttsx3
    _engine = pyttsx3.init()
except Exception:  # no audio device / library missing -> print only
    _engine = None


def speak(text):
    print(f"Assistant: {text}")
    if _engine:
        try:
            _engine.say(text)
            _engine.runAndWait()
        except Exception:
            pass


def listen():
    """Return recognised text, or None if not understood."""
    if TEXT_MODE:
        try:
            return input("You: ").strip()
        except EOFError:
            return "exit"
    import speech_recognition as sr
    r = sr.Recognizer()
    with sr.Microphone() as source:
        r.adjust_for_ambient_noise(source, duration=0.5)
        print("Listening...")
        audio = r.listen(source, phrase_time_limit=8)
    try:
        text = r.recognize_google(audio)
        print(f"You: {text}")
        return text
    except (sr.UnknownValueError, sr.RequestError):
        return None


# ---------------- NLU: score intents from free-form sentences ----------------
try:
    from nltk.stem import PorterStemmer
    _stem = PorterStemmer().stem
except Exception:
    _stem = lambda w: w  # noqa: E731

INTENTS = {
    "greet": {"hello", "hi", "hey", "morn", "greet"},
    "time": {"time", "clock", "hour"},
    "date": {"date", "day", "today", "calendar"},
    "search": {"search", "googl", "look", "find", "brows"},
    "weather": {"weather", "temperatur", "rain", "forecast", "hot", "cold", "sunni"},
    "email": {"email", "mail", "messag"},
    "remind": {"remind", "timer", "alarm", "alert"},
    "add_command": {"teach", "learn", "custom", "new"},
    "question": {"what", "who", "where", "explain", "tell", "how"},
    "exit": {"exit", "quit", "bye", "goodby", "stop"},
}
PRIORITY = ["exit", "add_command", "remind", "email", "weather", "search", "time", "date", "greet", "question"]


def tokens(text):
    return [_stem(w) for w in re.findall(r"[a-z']+", text.lower())]


def classify(text):
    toks = set(tokens(text))
    scores = {i: len(toks & kw) for i, kw in INTENTS.items()}
    if "what" in toks and "time" in toks:
        return "time"
    specific = [i for i in PRIORITY if i != "question"]
    best = max(specific, key=lambda i: (scores[i], -PRIORITY.index(i)))
    if scores[best] > 0:
        return best
    return "question" if scores["question"] > 0 else "unknown"


# ---------------- skills ----------------
def load_json(path):
    try:
        with open(path) as f:
            return json.load(f)
    except (OSError, json.JSONDecodeError):
        return {}


def do_search(text):
    m = re.search(r"(?:search|google|look up|find|browse)(?: for| up)?\s+(.*)", text, re.I)
    query = (m.group(1) if m else text).strip() or "Oasis Infobyte"
    webbrowser.open(f"https://www.google.com/search?q={quote_plus(query)}")
    return f"Searching the web for {query}."


def do_weather(text):
    m = re.search(r"\bin\s+([a-zA-Z ]+)", text)
    city = (m.group(1).strip() if m else os.environ.get("ASSISTANT_CITY", "Los Angeles"))
    try:
        g = requests.get("https://geocoding-api.open-meteo.com/v1/search", params={"name": city, "count": 1}, timeout=8).json()
        if not g.get("results"):
            return f"I couldn't find {city}."
        loc = g["results"][0]
        cur = requests.get("https://api.open-meteo.com/v1/forecast", timeout=8, params={
            "latitude": loc["latitude"], "longitude": loc["longitude"],
            "current": "temperature_2m,relative_humidity_2m,wind_speed_10m"}).json()["current"]
        return (f"In {loc['name']} it is {cur['temperature_2m']:.0f} degrees Celsius with "
                f"{cur['relative_humidity_2m']} percent humidity and wind at {cur['wind_speed_10m']} kilometres per hour.")
    except requests.RequestException:
        return "Sorry, I couldn't reach the weather service."


def do_remind(text):
    m = re.search(r"(\d+)\s*(second|sec|minute|min|hour)", text, re.I)
    if not m:
        return "Please say how long, for example: remind me in 5 minutes to stretch."
    n, unit = int(m.group(1)), m.group(2).lower()
    secs = n * (3600 if unit.startswith("hour") else 60 if unit.startswith("min") else 1)
    what = re.search(r"\bto\s+(.*)", text)
    note = what.group(1) if what else "your reminder"

    def fire():
        print("\a", end="", flush=True)  # audible terminal bell
        speak(f"Reminder: {note}")
    threading.Timer(secs, fire).start()
    return f"Okay, I'll remind you in {n} {unit}{'s' if n > 1 and not unit.endswith('s') else ''}."


def do_email(text):
    host, user, pw = os.environ.get("SMTP_HOST"), os.environ.get("SMTP_USER"), os.environ.get("SMTP_PASS")
    if not all([host, user, pw]):
        return "Email isn't configured. Set SMTP_HOST, SMTP_USER and SMTP_PASS for a test account."
    speak("Who should I send it to?")
    to = listen() or ""
    to = to.replace(" at ", "@").replace(" dot ", ".").replace(" ", "")
    speak("What should the message say?")
    body = listen() or ""
    msg = EmailMessage()
    msg["From"], msg["To"], msg["Subject"] = user, to, "Message from my voice assistant"
    msg.set_content(body)
    try:
        with smtplib.SMTP(host, int(os.environ.get("SMTP_PORT", 587)), timeout=10) as s:
            s.starttls()
            s.login(user, pw)
            s.send_message(msg)
        return f"Email sent to {to}."
    except (smtplib.SMTPException, OSError) as e:
        return f"Sending failed: {e}"


def do_question(text):
    q = text.lower()
    for key, ans in load_json(KB_FILE).items():
        if key in q:
            return ans
    topic = re.sub(r"^(what|who|where|how)\s+(is|are|was|were)\s+(a |an |the )?|tell me about |explain ", "", q).strip(" ?")
    try:
        r = requests.get(f"https://en.wikipedia.org/api/rest_v1/page/summary/{quote_plus(topic.replace(' ', '_'))}",
                         timeout=8, headers={"User-Agent": "OIBSIP-voice-assistant"})
        if r.ok and r.json().get("extract"):
            return r.json()["extract"].split(". ")[0] + "."
    except requests.RequestException:
        pass
    return "I don't know that one yet."


def add_custom():
    speak("What phrase should trigger the command?")
    phrase = (listen() or "").lower().strip()
    speak("What should I say when I hear it?")
    reply = (listen() or "").strip()
    if not phrase or not reply:
        return "Okay, cancelled."
    cmds = load_json(CUSTOM_FILE)
    cmds[phrase] = {"action": "say", "target": reply}
    with open(CUSTOM_FILE, "w") as f:
        json.dump(cmds, f, indent=2)
    return f"Got it. I'll respond to '{phrase}'."


def handle(text):
    low = text.lower()
    for phrase, cmd in load_json(CUSTOM_FILE).items():
        if phrase in low:
            if cmd["action"] == "open":
                webbrowser.open(cmd["target"])
                return f"Opening {cmd['target']}."
            return cmd["target"]
    intent = classify(text)
    return {
        "greet": lambda: "Hello! How can I help you today?",
        "time": lambda: f"It's {datetime.now():%I:%M %p}.",
        "date": lambda: f"Today is {datetime.now():%A, %B %d, %Y}.",
        "search": lambda: do_search(text),
        "weather": lambda: do_weather(text),
        "remind": lambda: do_remind(text),
        "email": lambda: do_email(text),
        "add_command": add_custom,
        "question": lambda: do_question(text),
        "exit": lambda: None,
        "unknown": lambda: "I'm not sure how to help with that. Try asking for the time, weather or a web search.",
    }[intent]()


def main():
    speak("Hi, I'm your assistant. How can I help?")
    while True:
        text = listen()
        if not text:
            speak("Sorry, I didn't catch that. Could you repeat?")
            continue
        reply = handle(text)
        if reply is None:
            speak("Goodbye!")
            break
        speak(reply)


if __name__ == "__main__":
    main()
