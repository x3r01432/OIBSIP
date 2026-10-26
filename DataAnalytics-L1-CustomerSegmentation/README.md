# Data Analytics — Level 1 Task 2: Customer Segmentation (RFM + K-Means)
**Oasis Infobyte SIP** · Steven Manuel Rios

Notebook: [`customer_segmentation.ipynb`](customer_segmentation.ipynb), executed with all outputs embedded. The charts are in [`screenshots/`](screenshots/).

**Dataset:** Online Retail, UCI ML Repository #352 (541,909 rows, compressed in `data/online_retail.csv.gz`).

**Key results:** 4,338 customers, average order £480, CLV £2,049. The elbow method gives K = 4 segments (Champions, Loyal-but-cooling, New, Lost). The 16% who are Champions bring in about 65% of revenue. Each segment has a marketing action.

## Run
```bash
pip install pandas numpy scikit-learn matplotlib seaborn jupyter openpyxl
jupyter notebook customer_segmentation.ipynb
```
