-- Get User's total watchtime
SELECT SUM(runtime) AS runtime_sum FROM (SELECT runtime FROM titles WHERE title_id IN (SELECT title_id FROM customer WHERE customer_id='1488844')) AS user_total_watchtime;
-- Get movie's average watchtime among all users
SELECT (SELECT COUNT(*) FROM customer WHERE title_id='tt0389605') * (SELECT runtime FROM titles WHERE title_id='tt0389605') AS movie_total_watch_time FROM customer LIMIT 1;
-- Most watched title
SELECT Title_ID, COUNT(Title_ID) FROM customer GROUP BY Title_ID ORDER BY COUNT(Title_ID) DESC LIMIT 1;
-- Director of most watched title
SELECT director_ID FROM crew WHERE title_ID LIKE (SELECT title_ID FROM customer GROUP BY title_ID ORDER BY COUNT(title_ID) DESC LIMIT 1);
-- Name of the most watched title
SELECT title FROM titles WHERE title_ID IN (SELECT title_ID FROM customer GROUP BY title_ID ORDER BY COUNT(title_ID) DESC LIMIT 1);
-- Actors in most watched title
SELECT name_id FROM principals WHERE movie_ID LIKE (SELECT title_ID FROM customer GROUP BY title_ID ORDER BY COUNT(title_ID) DESC LIMIT 1) AND (category='actor' OR category='actress');
-- Names of Actors in most watched title
SELECT primary_name FROM names WHERE name_ID IN (SELECT name_id FROM principals WHERE movie_ID LIKE (SELECT title_ID FROM customer GROUP BY title_ID ORDER BY COUNT(title_ID) DESC LIMIT 1) AND (category='actor' OR category='actress'));
-- Highest rated movie
SELECT MAX(avg_rating) AS maximum_rating FROM (SELECT avg_rating FROM titles WHERE type='movie') as foo;
-- Lowest rated movie
SELECT MIN(avg_rating) AS maximum_rating FROM (SELECT avg_rating FROM titles WHERE type='movie') as foo;
-- Most prominent genre in database
SELECT MAX(genre) AS most_prominent_genre FROM titles;
-- Least prominent genre in database
SELECT MIN(genre) AS least_prominent_genre FROM titles;
-- Longest movie in DB
SELECT MAX(runtime) AS longest_movie FROM titles;
-- Shortest movie in DB
SELECT MIN(runtime) AS shortest_movie FROM titles;
-- How many action movies
SELECT COUNT(genre) AS number_action_movies FROM titles WHERE genre='Action';
-- Highest rated TV series
SELECT MAX(avg_rating) AS maximum_rating FROM (SELECT avg_rating FROM titles WHERE type='tvSeries') as foo;
-- Lowest rated TV series
SELECT MIN(avg_rating) AS maximum_rating FROM (SELECT avg_rating FROM titles WHERE type='tvSeries') as foo;
-- Highest rated TV short
SELECT MAX(avg_rating) AS maximum_rating FROM (SELECT avg_rating FROM titles WHERE type='short') as foo;
-- Lowest rated TV short
SELECT MIN(avg_rating) AS maximum_rating FROM (SELECT avg_rating FROM titles WHERE type='short') as foo;
-- Movies by a director
SELECT title FROM titles WHERE title_id IN (SELECT title_id FROM crew WHERE director_id='{nm0072872}');
-- Movies having a certain actor
SELECT title FROM titles WHERE title_id IN (SELECT title_id FROM principals WHERE name_id='nm0000044');
-- Average runtime of all productions on database
SELECT AVG(runtime) AS average_runtime FROM titles;
-- Productions made by a certain writer
SELECT title FROM titles WHERE title_id IN (SELECT title_id FROM crew WHERE writer_id ='{nm0832952}');
-- Count how many movies are in our collection
SELECT COUNT (*) FROM titles;
-- Count how many actors are in our collection
SELECT COUNT(*) FROM names WHERE profession LIKE CONCAT('%', 'actor', '%');
-- Count how many directors are in our collection
SELECT COUNT(*) FROM names WHERE profession LIKE CONCAT('%', 'director', '%');