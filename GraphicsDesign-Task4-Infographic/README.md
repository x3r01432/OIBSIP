# Graphics Design · Task 4 — Infographic

**"The Power Shift" — Global electricity, 2000 → 2024.** A tall, single-column infographic built from real data.

## Deliverables
- `power_shift_infographic.png` — **800 × 2000 px @ 150 DPI**
- `power_shift_infographic@2x.png` — 1600 × 4000 px @ 300 DPI
- `power_shift_infographic.svg` — vector source
- `data/owid_world_electricity_2000_2024.csv` — the exact rows used

## What's inside
1. **Title header** — "The Power Shift".
2. **Line chart** — renewables' share of world electricity, 18.7% (2000) → 31.9% (2024).
3. **Donut chart** — 2024 mix: Fossil 59.1% / Renewables 31.9% / Nuclear 9.0%, with total generation.
4. **Icon stats** — solar (×2,101) and wind (×81) generation growth, with custom sun & turbine icons.
5. **Progress bars** — 2024 share by source (coal, gas, hydro, nuclear, wind, solar).
6. **Callout** — total demand doubled (15,279 → 30,930 TWh).

Visual types used: **icons, line chart, donut chart, progress bars** (4 ≥ required 3).
Palette is **exactly 4 colours**: Navy `#14213D`, Amber `#FCA311`, Teal `#2A9D8F`, Light `#F1F5F9`.

## Source
Our World in Data — Energy dataset (`owid/energy-data`), based on **Ember (2025)** and the **Energy Institute** Statistical Review of World Energy (2025). World series. Cited in the footer.

## Rebuild
```bash
/workspace/.venv/bin/python /workspace/build/gfx/task4_infographic.py
```
