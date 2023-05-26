# java-filmorate
Template repository for Filmorate project.
![Entity Relationship Diagram](https://github.com/Alexander-Kuzin/java-filmorate/assets/119132206/b0bcf791-a595-4d93-8db5-9ce30d6ca61b)
ER https://miro.com/app/board/uXjVMEDPgGk=/?share_link_id=986033322932

Примеры запросов 
1. Получить фильм по ID: 
      SELECT * FROM FILM WHERE id = id;
      
2. Получить пользователя по ID:
      SELECT * FROM USERS WHERE id = id;
    
3. Получить жанры фильма по id :
     SELECT g.GENRE_NAME genre
      FROM GENRE g
      LEFT JOIN GENRES_FILM fg ON fg.GENRE_ID = g.GENRE_ID
      WHERE fg.FILM_ID = id;
      
4. Cписок друзей по ID пользователя:
      SELECT FRIEND_ID FROM FRIENDSHIP 
      WHERE USER_ID = ID;
      
5. TOP-10 фильмов:
     SELECT *
      FROM (
          SELECT FILM_ID, COUNT(USER_ID) likes_total
          FROM likes
          GROUP BY FILM_ID ) l
      RIGHT JOIN FILM f ON f.ID = l.FILM_ID
      ORDER BY likes_total DESC 
      LIMIT 10;
     
