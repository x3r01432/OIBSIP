# Data Analytics — Level 2 Task 1: House Price Prediction with Linear Regression
**Oasis Infobyte SIP** · Steven Manuel Rios

Notebook: [`house_price_prediction.ipynb`](house_price_prediction.ipynb), executed with all outputs embedded. The charts are in [`screenshots/`](screenshots/).

**Dataset:** Ames Housing (De Cock, 2011), 2,930 sales.

**Key results:** Linear Regression on log(price): R² 0.939, RMSE ≈ $22.2k. The notebook includes actual-vs-predicted and residual plots and a coefficient analysis. Bonus: Ridge reaches R² 0.940 and Lasso 0.939.

## Run
```bash
pip install pandas numpy scikit-learn matplotlib seaborn jupyter 
jupyter notebook house_price_prediction.ipynb
```
