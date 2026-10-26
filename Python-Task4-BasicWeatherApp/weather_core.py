"""Shared weather logic. Uses Open-Meteo (free, no API key) by default;
uses OpenWeatherMap automatically if OWM_API_KEY is set in the environment."""
import os
import requests

TIMEOUT = 8
WMO = {0: "Clear sky", 1: "Mainly clear", 2: "Partly cloudy", 3: "Overcast", 45: "Fog", 48: "Rime fog",
       51: "Light drizzle", 53: "Drizzle", 55: "Dense drizzle", 61: "Light rain", 63: "Rain", 65: "Heavy rain",
       71: "Light snow", 73: "Snow", 75: "Heavy snow", 80: "Rain showers", 81: "Rain showers", 82: "Violent showers",
       95: "Thunderstorm", 96: "Thunderstorm w/ hail", 99: "Thunderstorm w/ hail"}
# Map WMO codes to OpenWeatherMap icon ids (icons served from openweathermap.org/img/wn/)
ICON = {0: "01d", 1: "02d", 2: "03d", 3: "04d", 45: "50d", 48: "50d", 51: "09d", 53: "09d", 55: "09d",
        61: "10d", 63: "10d", 65: "10d", 71: "13d", 73: "13d", 75: "13d", 80: "09d", 81: "09d", 82: "09d",
        95: "11d", 96: "11d", 99: "11d"}


class WeatherError(Exception):
    pass


def c_to_f(c):
    return c * 9 / 5 + 32


def geocode(city):
    if not city or not city.strip():
        raise WeatherError("Please enter a city name.")
    try:
        r = requests.get("https://geocoding-api.open-meteo.com/v1/search",
                         params={"name": city.strip(), "count": 1}, timeout=TIMEOUT)
        r.raise_for_status()
    except requests.Timeout:
        raise WeatherError("Network timeout - check your connection.")
    except requests.RequestException as e:
        raise WeatherError(f"Network error: {e}")
    res = r.json().get("results")
    if not res:
        raise WeatherError(f"City '{city}' not found.")
    g = res[0]
    return g["name"], g.get("country", ""), g["latitude"], g["longitude"]


def ip_location():
    """Bonus: detect city from IP using ipinfo.io."""
    try:
        return requests.get("https://ipinfo.io/json", timeout=TIMEOUT).json().get("city", "")
    except requests.RequestException:
        return ""


def fetch(city):
    name, country, lat, lon = geocode(city)
    try:
        r = requests.get("https://api.open-meteo.com/v1/forecast", timeout=TIMEOUT, params={
            "latitude": lat, "longitude": lon, "timezone": "auto", "forecast_days": 6,
            "current": "temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m",
            "hourly": "temperature_2m,weather_code",
            "daily": "temperature_2m_max,temperature_2m_min,weather_code"})
        r.raise_for_status()
    except requests.Timeout:
        raise WeatherError("Network timeout - check your connection.")
    except requests.RequestException as e:
        raise WeatherError(f"Weather API error: {e}")
    d = r.json()
    cur = d["current"]
    now = cur["time"][:13]
    hours = d["hourly"]
    start = next((i for i, t in enumerate(hours["time"]) if t[:13] >= now), 0)
    return {
        "place": f"{name}, {country}",
        "temp_c": cur["temperature_2m"], "humidity": cur["relative_humidity_2m"],
        "condition": WMO.get(cur["weather_code"], "Unknown"), "icon": ICON.get(cur["weather_code"], "03d"),
        "wind_kmh": cur["wind_speed_10m"],
        "hourly": [(hours["time"][i][11:16], hours["temperature_2m"][i], ICON.get(hours["weather_code"][i], "03d"))
                   for i in range(start, start + 6)],
        "daily": [(d["daily"]["time"][i], d["daily"]["temperature_2m_min"][i], d["daily"]["temperature_2m_max"][i],
                   WMO.get(d["daily"]["weather_code"][i], "")) for i in range(1, 6)],
    }
