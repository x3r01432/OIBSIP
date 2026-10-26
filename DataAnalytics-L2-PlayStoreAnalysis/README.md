# Data Analytics — Level 2 Task 4: Google Play Store Analysis
**Oasis Infobyte SIP** · Steven Manuel Rios

Notebook: [`playstore_analysis.ipynb`](playstore_analysis.ipynb), executed with all outputs embedded. The charts are in [`screenshots/`](screenshots/).

**Dataset:** Google Play Store Apps (Lavanya Gupta, Kaggle): 10,841 apps and 64,295 reviews.

**Key results:** 9,659 unique apps after cleaning. FAMILY, GAME and TOOLS are the most saturated categories, 92% of apps are free, and size vs installs has ρ = 0.31. VADER finds 68% of reviews positive. The notebook includes sentiment by category, an interactive plotly chart (`screenshots/interactive_category_bubble.html`) and 3 developer insights.

## Run
```bash
pip install pandas numpy scikit-learn matplotlib seaborn jupyter vaderSentiment plotly scipy
jupyter notebook playstore_analysis.ipynb
```
