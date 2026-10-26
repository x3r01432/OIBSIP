# Graphics Design · Task 5 — Magazine Layout

**"Verdant Quarterly" — Issue 04.** A 6-page A4 magazine spread, authored in HTML/CSS and printed to PDF with headless Chrome.

## Deliverables
- `verdant_quarterly.pdf` — the 6-page magazine (A4, print layout)
- `verdant_quarterly.html` — editable source
- `preview/page-1…6.png` — page previews
- `images/` — photography (Unsplash) + the Verdant logo

## Pages
1. **Cover** — full-bleed photo, masthead, title.
2. **Contents + Editor's Letter** — table of contents, drop-cap intro, stat callouts.
3. **Feature: The Return of the Urban Forest** — two-column, drop cap, inline image, **pull quote**.
4. **Field Notes: A Season of Water** — two-column, subheads, stat box.
5. **Photo Essay: The Balcony Experiment** — full-bleed photo page.
6. **Credits & Masthead** — photography, data, and font credits.

## Requirements met
- Running **header, footer, and page number on every page**.
- Consistent **two-column grid** on article pages.
- **2 full-bleed image pages** (p.1, p.5).
- **Body text 10.5 pt** (within the 10–12 pt range).
- A **pull quote** (p.3) and a dedicated **credits page** (p.6).
- Fonts: Poppins (display), Inter (body), Playfair Display (quotes/cover).

## Photography
All photos via Unsplash (Unsplash License). Full per-page credits are on page 6 and in `images/`.

## Rebuild
```bash
cd GraphicsDesign-Task5-MagazineLayout
google-chrome --headless=new --no-pdf-header-footer --print-to-pdf=verdant_quarterly.pdf verdant_quarterly.html
```
