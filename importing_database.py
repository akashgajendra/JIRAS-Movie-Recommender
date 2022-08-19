import psycopg2
import csv

commit_Frequency = 15
line_count = 0

# # Connecting to the PSQL Server
conn = psycopg2.connect("host=csce-315-db.engr.tamu.edu dbname=csce315904_3db user=csce315904_3user password=group3pass")

cur = conn.cursor()

# print("PSQL database version:")
# cur.execute("INSERT INTO teammembers VALUES ('Bob 2', 904, '2012', '05-31-2021')")

# conn.commit()
# cur.close()

# Adding names file to database
with open('original_data/names.csv', mode='r') as file:
    csvFile = csv.reader(file, delimiter='\t')

    next(csvFile)
    request = "CREATE TABLE names (nconst VARCHAR(255) PRIMARY KEY, primaryName VARCHAR(255) NOT NULL, birthYear Integer, deathYear Integer, primaryProfession VARCHAR(255))"
    cur.execute(request)
    conn.commit()
    

    i = 0
    for lines in csvFile:
        if "'" in lines[1]:
            lines[1] = lines[1].replace("'", "''")
        if lines[2] == '':
            lines[2] = 'NULL'
        if lines[3] == '':
            lines[3] = 'NULL'
        request = "INSERT INTO names VALUES ('%s', '%s', %s, %s, '%s') ON CONFLICT DO NOTHING"%(lines[0], lines[1], lines[2], lines[3], lines[4])
        cur.execute(request)
        i += 1
        if (i == commit_Frequency):
            line_count += i
            print("line " + str(line_count) + " of names")
            conn.commit()
            i = 0
            # break
    conn.commit()

line_count = 0
print('Principals')
#######    Adding principals file to database    #######
with open('original_data/principals.csv', mode='r') as file:
    csvFile = csv.reader(file, delimiter='\t')

    next(csvFile)
    request = "CREATE TABLE principals (Movie_ID VARCHAR(255), Name_ID VARCHAR(255), Category VARCHAR(255), Job VARCHAR(255), Characters VARCHAR(255), PRIMARY KEY (Movie_ID, Name_ID))"
    cur.execute(request)
    conn.commit()
    

    i = 0
    for lines in csvFile:
        if "'" in lines[3]:
            lines[3] = lines[3].replace("'", "''")
        if "'" in lines[4]:
            lines[4] = lines[4].replace("'", "''")
        if lines[3] == '':
            lines[3] = 'NULL'
        if lines[4] == '':
            lines[4] = 'NULL'
        request = "INSERT INTO principals VALUES ('%s', '%s', '%s', '%s', '%s') ON CONFLICT DO NOTHING"%(lines[0], lines[1], lines[2], lines[3], lines[4])
        cur.execute(request)
        i += 1
        if (i == commit_Frequency):
            line_count += i
            print("line " + str(line_count) + " of principals")
            conn.commit()
            i = 0
            # break
    conn.commit()

line_count = 0
print('Crew')
#######    Adding crew file to database    #######
with open('original_data/crew.csv', mode='r') as file:
    csvFile = csv.reader(file, delimiter='\t')

    next(csvFile)
    request = "CREATE TABLE crew (Movie_ID VARCHAR(255) PRIMARY KEY, Directors_ID TEXT [], Writers_ID TEXT [])"
    cur.execute(request)
    conn.commit()
    

    i = 0
    for lines in csvFile:
        if lines[1] == '':
            lines[1] = 'NULL'
        if lines[2] == '':
            lines[2] = 'NULL'
        request = "INSERT INTO crew VALUES ('%s', '{%s}', '{%s}') ON CONFLICT DO NOTHING"%(lines[0], lines[1], lines[2])
        cur.execute(request)
        i += 1
        if (i == commit_Frequency):
            line_count += i
            print("line " + str(line_count) + " of crew")
            conn.commit()
            i = 0
            # break
    conn.commit()

line_count = 0
print('titles')
#######    Adding titles file to database    #######    
with open('original_data/titles.csv', mode='r') as file:
    csvFile = csv.reader(file, delimiter='\t')

    next(csvFile)
    request = "CREATE TABLE titles (Movie_ID TEXT PRIMARY KEY, Title_Type TEXT, Title TEXT, Start_Year INTEGER, End_Year INTEGER, Runtime INTEGER, Genre TEXT, Year INTEGER, Avg_Rating NUMERIC(2,1), Num_Votes INTEGER)"
    cur.execute(request)
    conn.commit()
    

    i = 0
    for lines in csvFile:
        if "'" in lines[2]:
            lines[2] = lines[2].replace("'", "''")
        if lines[3] == '':
            lines[3] = 'NULL'
        if "'" in lines[3]:
            lines[3] = lines[3].replace("'", "''")
        if lines[4] == '':
            lines[4] = 'NULL'
        if lines[5] == '':
            lines[5] = 'NULL'
        if lines[6] == '':
            lines[6] = 'NULL'
        request = "INSERT INTO titles VALUES ('%s', '%s', '%s', %s, %s, %s, '%s', %s, %s, %s) ON CONFLICT DO NOTHING"%(lines[0], lines[1], lines[2], lines[3], lines[4], lines[5], lines[6], lines[7], lines[8], lines[9])
        cur.execute(request)
        i += 1
        if (i == commit_Frequency):
            line_count += i
            print("line " + str(line_count) + " of titles")
            conn.commit()
            i = 0
            # break
    conn.commit()

line_count = 0
print('ratings')
#######    Adding customer_ratings file to database    #######    
with open('original_data/customer_ratings.csv', mode='r') as file:
    csvFile = csv.reader(file, delimiter='\t')

    next(csvFile)
    request = "CREATE TABLE ratings (Customer_ID INTEGER PRIMARY KEY, Rating INTEGER, Date DATE, Movie_ID TEXT)"
    cur.execute(request)
    conn.commit()
    

    i = 0
    for lines in csvFile:
        request = "INSERT INTO ratings VALUES (%s, %s, '%s', '%s') ON CONFLICT DO NOTHING"%(lines[0], lines[1], lines[2], lines[3])
        cur.execute(request)
        i += 1
        if (i == commit_Frequency):
            line_count += i
            print("line " + str(line_count) + " of ratings")
            conn.commit()
            i = 0
            # break
    conn.commit()

cur.close()