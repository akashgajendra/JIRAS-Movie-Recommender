import pandas as pd

#Reading the file
names_data = pd.read_csv("original_data/names.csv", delimiter='\t')

# delete index column
del names_data['Unnamed: 0']

pre_dup=names_data.shape
print("Dimensions before dropping duplicates", pre_dup)
array=names_data.duplicated(subset=['nconst'],keep='first')
#Will print duplicate entries
#print(names_data[array])

names_data.drop_duplicates(['nconst'],inplace=True)
post_dup=names_data.shape
print("Dimensions after dropping duplicates",post_dup)

#Create new comma separated csv
names_data.to_csv("clean_data/CleanNames.csv",index=False, sep ='\t')

#Check for duplicates
if pre_dup==post_dup:
    print("No duplicates")


