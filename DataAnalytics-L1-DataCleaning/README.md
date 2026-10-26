# Data Analytics — Level 1 Task 3: Cleaning Data
**Oasis Infobyte SIP** · Steven Manuel Rios

Notebook: [`data_cleaning.ipynb`](data_cleaning.ipynb), executed with all outputs embedded. The charts are in [`screenshots/`](screenshots/).

**Dataset:** Titanic (seaborn-data), with documented, seeded messiness added (`data/titanic_messy.csv`). The output is `data/titanic_clean.csv`.

**Key results:** Data quality report, then a justified strategy for each column, duplicate removal, standardisation, IQR outlier decisions and dtype fixes. The before vs after table goes from 916 rows, 892 nulls and 25 duplicates to 891 rows, 0 nulls and 0 duplicates.

## Run
```bash
pip install pandas numpy scikit-learn matplotlib seaborn jupyter 
jupyter notebook data_cleaning.ipynb
```
