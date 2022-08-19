import csv

### read & write for movies entity ###
reader = csv.reader(open('original_data/titles.csv','r'))
writer = csv.writer(open('clean_data/titles.csv','w'), delimiter='\t')
writer.writerow(('titleID','type','title', 'year', 'runtime(min)', 'genres', 'avgRating', 'numVotes'))

### extract info from reader & store in a dic (clear repeat items) ###
header = next(reader)
movies_info = {}
for row in reader:
    row_s = row[0].split('\t')
    title_id = row_s[1]

    # dealing with edge case: missing elements
    while len(row_s) <= 10: 
        row_s.append('')
    year = row_s[4] if row_s[8] == '' else row_s[8]
    Year = 0 if year == '' else int(year)
    runTime = 0 if row_s[6] == '' else int(row_s[6])
    avgRating = 0 if row_s[9] == '' else float(row_s[9])
    numVotes = 0 if row_s[10] == '' else int(row_s[10])
    
    movie_info = [row_s[2], row_s[3], Year, runTime, row_s[7], avgRating, numVotes] 
    # type/title/year[int]/runtime[int]/genres/avgRating[float]/numVotes[int]

    if title_id not in movies_info.keys(): # add to movie dict only if not exist
        movies_info[title_id] = movie_info

### write into a writer ###
for key in movies_info.keys():
    info_list = movies_info[key]
    writer.writerow((key, info_list[0], info_list[1], info_list[2], info_list[3], info_list[4], info_list[5], info_list[6]))
    # titleID, type, title, year[int], runtime[int], genres, avgRating[float], numVotes[int]