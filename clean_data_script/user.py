# #------ remove dup ------#
import pandas as pd
# import datetime

### reading the file ###
user_data = pd.read_csv("original_data/customer_ratings.csv", delimiter='\t')

### drop repeating items ###
user_data.drop("Index", axis=1, inplace=True)

pre_dup = user_data.shape
user_data['date'] = pd.to_datetime(user_data.date,infer_datetime_format = True)
user_data.sort_values(by = 'date', ascending=False, inplace=True)
print("Dimensions before dropping duplicates:", pre_dup)
array = user_data.duplicated(subset=['customerId','titleId'], keep='first')
print(user_data[array])
user_data.drop_duplicates(['customerId','titleId'], inplace=True)
post_dup = user_data.shape
print("Dimensions after dropping duplicates:", post_dup)

user_data.to_csv("clean_data/user.csv", index=False, sep='\t')
#--------------------------#

# import csv
# import pandas as pd
# from datetime import datetime

### read & write for user entity ###
# reader = csv.reader(open('original_data/customer_ratings.csv', 'r'))
# writer = csv.writer(open('clean_data/user_format.csv', 'w'), delimiter='\t')
# writer.writerow(('user_ID','titleID','date', 'rating'))

# header = next(reader)
# user_with_movie_rating = {}
# for row in reader:
#     row_s = row[0].split('\t')
    # writer.writerow((row_s[1], row_s[4], row_s[3], float(row_s[2])))

## extract info from reader / store in a dic ###
# header = next(reader)
# user_with_movie_rating = {}
# for row in reader:
#     row_s = row[0].split('\t')
#     user_id = row_s[1]
#     movie_rating = [row_s[3], row_s[4], float(row_s[2])] #date, titleID, rating
#     if user_id in user_with_movie_rating.keys():
#         user_with_movie_rating[user_id].append(movie_rating)
#     else:
#         user_with_movie_rating[user_id] = [movie_rating]

# # ### write into a writer ###
# for key in user_with_movie_rating.keys():
#     for ele in user_with_movie_rating[key]:
#         writer.writerow((key, ele[1], ele[0], ele[2]))
    
#     writer.writerow((int(key), out))

# ### Test ###
# # with open ('clean_data/user.csv','r') as W:
# #     reader_test = csv.reader(W, delimiter='\t')
# #     for row_test in reader_test:
# #         # print(row_test[1])
# #         l1 = row_test[1].split('/')
# #         print(l1[0]) # 2005-09-06,tt0389605,3.0 
# #         print(l1[0].split(',')) # ['2005-09-06', 'tt0389605', '3.0']
# #         break # only test for the first user

