"""Beginner tier: command-line weather app."""
from weather_core import WeatherError, c_to_f, fetch


def main():
    city = input("Enter city name: ").strip()
    if not city:
        print("Error: city cannot be empty.")
        return
    try:
        w = fetch(city)
    except WeatherError as e:
        print("Error:", e)
        return
    print(f"\nWeather for {w['place']}")
    print(f"  Temperature : {w['temp_c']:.1f} °C / {c_to_f(w['temp_c']):.1f} °F")
    print(f"  Humidity    : {w['humidity']}%")
    print(f"  Condition   : {w['condition']}")
    print(f"  Wind speed  : {w['wind_kmh']} km/h")


if __name__ == "__main__":
    main()
