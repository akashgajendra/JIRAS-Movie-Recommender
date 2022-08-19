import pandas as pd

#Reading the file
user_ids = pd.read_csv("data/customer_ratings.csv", delimiter='\t')
pd.DataFrame(user_ids['customerId'].sort_values().unique()).to_csv("GUI_Files/resources/user_ids.csv",index=False, header=False, sep ='\n')