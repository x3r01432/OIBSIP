# Data Science — Task 3: Car Price Prediction
Notebook: `car_price_prediction.ipynb` (executed, with outputs)

- Data: *Vehicle dataset from CarDekho* (301 rows), in `data/car_data.csv`
- Cleaning: null check, 2 duplicates removed, categorical text normalised (case/whitespace)
- Features: `Car_Age` from Year; `Brand` extracted from `Car_Name` (model→maker map, rare brands grouped as "other")
- EDA: price distribution, price by fuel type, price vs age; one-hot encoding; correlation heatmap
- Linear Regression, Random Forest, Gradient Boosting (trained on log price), evaluated with MAE, RMSE and R²
- **Best:** Linear Regression, R² ≈ 0.90; permutation-importance chart included

Author: Steven Manuel Rios
