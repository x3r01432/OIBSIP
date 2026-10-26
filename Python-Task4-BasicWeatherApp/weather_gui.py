"""Advanced tier: tkinter weather app with icons, 6-hour and 5-day forecasts, unit toggle."""
import io
import tkinter as tk

import requests
from PIL import Image, ImageTk

from weather_core import WeatherError, c_to_f, fetch, ip_location


class App(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("Weather App")
        self.geometry("560x560")
        self.celsius = True
        self.data = None
        self.images = []
        top = tk.Frame(self); top.pack(pady=8)
        self.city = tk.Entry(top, width=24); self.city.pack(side="left")
        tk.Button(top, text="Get Weather", command=self.get).pack(side="left", padx=4)
        tk.Button(top, text="My Location", command=self.locate).pack(side="left")
        self.unit_btn = tk.Button(top, text="°C / °F", command=self.toggle); self.unit_btn.pack(side="left", padx=4)
        self.error = tk.Label(self, fg="red"); self.error.pack()
        self.panel = tk.Frame(self); self.panel.pack(fill="both", expand=True)

    def t(self, c):
        return f"{c:.0f}°C" if self.celsius else f"{c_to_f(c):.0f}°F"

    def icon(self, code, size=50):
        try:
            raw = requests.get(f"https://openweathermap.org/img/wn/{code}@2x.png", timeout=5).content
            img = ImageTk.PhotoImage(Image.open(io.BytesIO(raw)).resize((size, size)))
            self.images.append(img)
            return img
        except Exception:
            return None

    def locate(self):
        city = ip_location()
        if city:
            self.city.delete(0, "end"); self.city.insert(0, city); self.get()
        else:
            self.error.config(text="Could not detect location from IP.")

    def toggle(self):
        self.celsius = not self.celsius
        if self.data:
            self.render()

    def get(self):
        self.error.config(text="")
        try:
            self.data = fetch(self.city.get())
        except WeatherError as e:
            self.error.config(text=str(e))
            return
        self.render()

    def render(self):
        for w in self.panel.winfo_children():
            w.destroy()
        self.images.clear()
        d = self.data
        tk.Label(self.panel, text=d["place"], font=("Arial", 16, "bold")).pack()
        tk.Label(self.panel, image=self.icon(d["icon"], 80)).pack()
        tk.Label(self.panel, text=f"{self.t(d['temp_c'])}  {d['condition']}", font=("Arial", 14)).pack()
        tk.Label(self.panel, text=f"Humidity {d['humidity']}%   Wind {d['wind_kmh']} km/h").pack()
        tk.Label(self.panel, text="Next 6 hours", font=("Arial", 11, "bold")).pack(pady=(10, 0))
        hr = tk.Frame(self.panel); hr.pack()
        for time, temp, code in d["hourly"]:
            f = tk.Frame(hr, padx=6); f.pack(side="left")
            tk.Label(f, text=time).pack(); tk.Label(f, image=self.icon(code, 36)).pack(); tk.Label(f, text=self.t(temp)).pack()
        tk.Label(self.panel, text="Next 5 days", font=("Arial", 11, "bold")).pack(pady=(10, 0))
        for day, lo, hi, cond in d["daily"]:
            tk.Label(self.panel, text=f"{day}   {self.t(lo)} – {self.t(hi)}   {cond}").pack()


if __name__ == "__main__":
    App().mainloop()
