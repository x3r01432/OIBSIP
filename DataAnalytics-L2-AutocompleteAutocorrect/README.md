# Data Analytics — Level 2 Task 5: Autocomplete & Autocorrect
**Oasis Infobyte SIP** · Steven Manuel Rios

Notebook: [`autocomplete_autocorrect.ipynb`](autocomplete_autocorrect.ipynb), executed with all outputs embedded. The charts are in [`screenshots/`](screenshots/).

**Dataset:** Project Gutenberg #1342, #1661 and #98 (public domain), about 372k tokens.

**Key results:** Bigram vs trigram-with-backoff autocomplete (Recall@3 ≈ 0.24) tested on 12 prefixes. Autocorrect compares pyspellchecker, Levenshtein and Damerau–Levenshtein: Damerau–Levenshtein and pyspellchecker both fix 23/25 misspellings with 0 false corrections. The notebook ends with a discussion of limitations vs Gboard.

## Run
```bash
pip install pandas numpy scikit-learn matplotlib seaborn jupyter nltk pyspellchecker textdistance
jupyter notebook autocomplete_autocorrect.ipynb
```
