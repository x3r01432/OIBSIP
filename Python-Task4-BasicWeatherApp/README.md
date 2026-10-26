# Python Programming — Task 4: Basic Weather App

| Tier | File | Features |
|---|---|---|
| Beginner | `weather_cli.py` | city prompt, API call + JSON parse, temp °C & °F, humidity, condition, wind; handles city-not-found / timeout / API errors; rejects empty input |
| Advanced | `weather_gui.py` | tkinter GUI, condition icons (OpenWeatherMap icon URLs), next-6-hours panel, next-5-days panel, °C/°F toggle, IP location via ipinfo.io (bonus), errors shown in the GUI |

**API:** the task allows "OpenWeatherMap (or equivalent free API)". This uses
[Open-Meteo](https://open-meteo.com) — free and **no API key needed**, so it runs out of the box.
Icons come from OpenWeatherMap's public icon URLs.

## Run
```bash
pip install requests pillow
python weather_cli.py
python weather_gui.py
```
Real output captured in `screenshots/`.

Author: Steven Manuel Rios
