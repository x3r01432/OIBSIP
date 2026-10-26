# Data Science — Task 5: Sales Prediction Using Python
Notebook: `sales_prediction.ipynb` (executed, with outputs)

- Data: classic `Advertising.csv` (ISLR), 200 rows: TV, Radio, Newspaper → Sales
- EDA: null check, stats, pairplot, per-channel scatter plots, correlation heatmap
- Linear Regression (baseline), Polynomial Regression (degree 2), Random Forest, evaluated with MAE, RMSE and R²
- Residual plot for the best model
- **Best:** Polynomial Regression, R² ≈ 0.987 (it captures the TV×Radio synergy). TV has the largest overall impact, Radio the largest per dollar, and Newspaper is ≈ 0

Author: Steven Manuel Rios
