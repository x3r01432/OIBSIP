# Graphics Design · Task 1 — Posters & Fliers

Two print-ready awareness posters for **Verdant**, a fictional everyday-ecology brand.
All artwork is generated programmatically as real vector/raster files — no stock mockups.

## Deliverables
| Poster | Theme | Files |
|---|---|---|
| Spring Plant Swap | Community event flier | `poster1_spring_plant_swap.svg` / `.png` (1080) / `@2x.png` / `.pdf` |
| Every Drop Counts | Water-conservation PSA | `poster2_every_drop_counts.svg` / `.png` (1080) / `@2x.png` / `.pdf` |

Plus `contrast_report.md` documenting WCAG contrast for every text/background pair.

## Design notes
- **Poster 1 (Plant Swap):** Deep green / Mint / Amber / White. Custom potted-plant illustration, clear event hierarchy (headline → details → CTA).
- **Poster 2 (Every Drop Counts):** Navy `#023047`, Sky `#8ECAE6`, Sun `#FFB703`, White, with a dripping-tap illustration. Headline statistic — *"3,000+ gallons a year"* — is sourced from the U.S. EPA WaterSense program and cited on the poster.
- Typography: Poppins (display) + Inter (body), outlined to paths so PDFs render identically everywhere.
- **Accessibility:** every text pair tested — all ≥ 5.37:1 (AA, most AAA). See `contrast_report.md`.

## Rebuild
```bash
/workspace/.venv/bin/python /workspace/build/gfx/task1_posters.py
```

## Note on screenshots / output
The "output" for a print design is the artwork itself: the PNG previews and the print-ready PDFs in this folder are the deliverables.
