## deletes index column
## deletes rows with duplicate Title_ID, Name_ID, and Category
## keeps job/characters... which will be added into DB as strings for now
## outputs result to principals.csv
import pandas as pd

# read csv
principals_data = pd.read_csv('original_data/principals.csv', delimiter='\t')

# delete index column
del principals_data['Unnamed: 0']

# drop duplicate rows, based on certain columns
principals_data = principals_data.drop_duplicates(['titleId', 'nconst', 'category'])

# outputs to csv
principals_data.to_csv('clean_data/principals.csv', index=False, sep ='\t')