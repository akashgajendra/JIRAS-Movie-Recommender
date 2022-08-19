import pandas as pd

# read csv
crew_data = pd.read_csv('original_data/crew.csv', delimiter='\t')

# delete index column
del crew_data['Unnamed: 0']

# drop duplicate rows, based on certain columns
crew_data = crew_data.drop_duplicates(['titleId'])

# outputs to csv
crew_data.to_csv('clean_data/crew.csv', index=False, sep ='\t')