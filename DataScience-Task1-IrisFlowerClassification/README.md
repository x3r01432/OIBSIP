# Data Science — Task 1: Iris Flower Classification
Notebook: `iris_classification.ipynb` (executed, with outputs)

- Data: `sklearn.datasets.load_iris()`
- EDA: shape, dtypes, null check, descriptive stats, pairplot, box plots, correlation heatmap
- Discussion of the most discriminative features (petal length/width)
- 80/20 stratified split; Logistic Regression, KNN, Decision Tree, Random Forest
- Accuracy, confusion matrices, classification reports, 5-fold cross-validation
- **Result:** Random Forest is best (≈96.7% CV accuracy); LR and KNN are close (96.0%)

Charts are in `screenshots/`. Stack: Python, pandas, scikit-learn, matplotlib, seaborn, Jupyter.
Run: `pip install pandas scikit-learn seaborn jupyter && jupyter notebook iris_classification.ipynb`

Author: Steven Manuel Rios
