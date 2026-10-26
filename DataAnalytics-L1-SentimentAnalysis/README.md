# Data Analytics — Level 1 Task 4: Sentiment Analysis
**Oasis Infobyte SIP** · Steven Manuel Rios

Notebook: [`sentiment_analysis.ipynb`](sentiment_analysis.ipynb), executed with all outputs embedded. The charts are in [`screenshots/`](screenshots/).

**Dataset:** Twitter US Airline Sentiment (Figure Eight / Kaggle), 14,640 tweets in 3 classes.

**Key results:** TF-IDF (1–2 grams) with Naive Bayes, Logistic Regression and Linear SVM. The best is Linear SVM (accuracy 78.6%, macro F1 0.723). The notebook also has word clouds per class and an error analysis of 5 misclassified tweets.

## Run
```bash
pip install pandas numpy scikit-learn matplotlib seaborn jupyter nltk wordcloud
jupyter notebook sentiment_analysis.ipynb
```
