# Data Analytics — Level 2 Task 3: Fraud Detection
**Oasis Infobyte SIP** · Steven Manuel Rios

Notebook: [`fraud_detection.ipynb`](fraud_detection.ipynb), executed with all outputs embedded. The charts are in [`screenshots/`](screenshots/).

**Dataset:** Credit Card Fraud Detection (ULB / Kaggle), 284,807 transactions, 0.173% fraud. **The 150 MB CSV is not committed**; the notebook downloads it automatically from TensorFlow's public mirror.

**Key results:** LogReg + SMOTE reaches recall 0.92 but precision 0.05. Random Forest (balanced) reaches precision 0.95, recall 0.78, F1 0.85 and PR-AUC 0.86, and its best threshold (0.3) gives 86% precision at 84% recall. The notebook also covers ROC/PR curves, feature importance and a scalability design.

## Run
```bash
pip install pandas numpy scikit-learn matplotlib seaborn jupyter imbalanced-learn
jupyter notebook fraud_detection.ipynb
```
