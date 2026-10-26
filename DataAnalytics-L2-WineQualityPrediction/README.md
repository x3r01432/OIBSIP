# Data Analytics — Level 2 Task 2: Wine Quality Prediction
**Oasis Infobyte SIP** · Steven Manuel Rios

Notebook: [`wine_quality_prediction.ipynb`](wine_quality_prediction.ipynb), executed with all outputs embedded. The charts are in [`screenshots/`](screenshots/).

**Dataset:** Wine Quality, UCI #186 (red, 1,599 wines; the white set is also in `data/`).

**Key results:** 3-class binning (low/medium/high) with Random Forest, SGD and SVC. RF has the best accuracy (87.5%), but SVC has the best balanced accuracy (0.589) and premium recall (77%), so it is recommended. The top features are volatile acidity, alcohol and sulphates.

## Run
```bash
pip install pandas numpy scikit-learn matplotlib seaborn jupyter 
jupyter notebook wine_quality_prediction.ipynb
```
