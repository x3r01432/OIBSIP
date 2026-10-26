# Graphics Design · Task 2 — Logo Design

A complete logo system for **Verdant**, generated as real SVG + PNG assets.

## Deliverables
- `svg/` and `png/` — 9 core variants: **3 styles** (color / mono / reversed) × **3 layouts** (horizontal / stacked / mark).
- Transparent reversed versions (`*_reversed_transparent.png`) for placing on photos.
- Stacked and mark PNGs exported at ≥ 1000 px with transparency.
- `logo_board.svg` / `logo_board.png` — a presentation board showing all variants, clear space, the colour palette, and minimum sizes.

## Design rationale
- **The idea.** "Verdant" means lush and green. The mark is **two leaves meeting to form a letter *V***, with an **amber dot** as the growing bud/seed — tying the brand name, the first letter, and the theme of growth into one shape.
- **Palette.** Deep `#1B4332` (trust, roots), Leaf `#40916C` (primary brand green), Mint `#D8F3DC` (light/background), Amber `#F4A261` (energy, the single accent). Restraint to four colours keeps the system cohesive across print and screen.
- **Typography.** Poppins — a geometric, friendly sans — set in a confident lowercase to feel approachable and modern. Wordmark text is outlined to paths so it never depends on a font being installed.
- **Flexibility.** The horizontal lockup suits headers and business cards; the stacked lockup suits square avatars; the standalone mark works as a favicon/app icon. Mono and reversed variants guarantee legibility on any background.
- **Clear space & min size** are defined on the logo board to protect the mark in use.

## Rebuild
```bash
/workspace/.venv/bin/python /workspace/build/gfx/task2_logo.py
```
