# Python Programming — Task 1: Voice Assistant

Both tiers in `assistant.py`.

**Beginner:** microphone input (`speech_recognition`), "hello" greeting, current time & date,
web search (opens browser), asks you to repeat when speech isn't understood, `pyttsx3` speech for every reply.

**Advanced:**
- **Intent understanding** from free-form sentences: NLTK-stemmed tokens scored against intent vocabularies
  ("could you tell me what the weather's like in Paris" → `weather`), not exact command matching
- **Email by voice** via `smtplib` (set `SMTP_HOST`, `SMTP_USER`, `SMTP_PASS` for a *test* account)
- **Timed reminders** ("remind me in 10 minutes to stretch") with an audible alert
- **Live weather** (Open-Meteo, free/no key)
- **General knowledge**: local `knowledge_base.json`, then Wikipedia summary API
- **Custom commands**: edit `custom_commands.json` or say "teach a new command"

## Run
```bash
pip install -r requirements.txt
python assistant.py          # voice mode (microphone)
python assistant.py --text   # type instead of talk
```

## Privacy
- Audio is captured only while "Listening..." is shown. It goes to **Google's free Web Speech API**
  (`recognize_google`) for transcription; nothing is stored locally.
- Weather requests send the city name to Open-Meteo; questions not in the local knowledge base send the topic to Wikipedia.
- Email content goes only to your configured SMTP server. Credentials come from environment variables, never the code.
- Custom commands are saved in plain text in `custom_commands.json`. No conversation log is kept.

## Testing note
The build box has no microphone or speakers, so the intent engine and skills were tested in `--text`
mode (`screenshots/text_mode_test.txt`). Voice capture needs a real mic.

Author: Steven Manuel Rios
