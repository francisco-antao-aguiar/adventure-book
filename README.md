# adventure-book

Spring Boot API for the Pictet adventure book exercise. You can add, search and play books (with health and
consequences), either anonymously
or as a named player whose progress is kept on the server.

## Why I started with objective 6

The brief says to do the objectives in order, but I started with adding books (objective 6).

The database starts empty, so without a way to add books there was nothing to list, search or play.
Having the POST endpoint first let me load the sample books and test all the other objectives with them.

Since I was planning to do all the objectives anyway, doing 6 first didn't change the scope,
only the order.

## Running it

Needs Java 21. Maven isn't needed, the wrapper is included.

From the project folder:

```
mvnw.cmd clean package
mvnw.cmd spring-boot:run
```

It starts on port 8080. The database is H2 in memory, so no extra steps required.
If you want to look at the tables: http://localhost:8080/h2-console

```
url: jdbc:h2:mem:testdb
user: sa
password: (empty/no password) 
```

To try it I used Postman. Start by sending one of the books in `samples/` as the body of a POST to `/book`.

## Endpoints

Books:

```
GET    /book                              list all books
GET    /book/search                       search by title, author, category or difficulty
GET    /book/{id}                         get one book
POST   /book                              add a book
DELETE /book/{id}                         delete a book
POST   /book/{id}/categories              add a category to a book
DELETE /book/{id}/categories/{name}       remove a category from a book
GET    /category                          list all categories
```

Reading a book:

```
GET    /book/{id}/sections/start          get the first section
GET    /book/{id}/sections/{number}       get a section by its number
```

Playing without a player, client side (objective 4)

```
POST   /book/{id}/play/start              start a game
POST   /book/{id}/play                    make a move
```

Here the client keeps the game. Each call sends back the section and health from the last response,
plus the id of the option picked.

Playing as a player (objective 5)

```
POST   /players/{player}/book/{id}/play/start    start a game
GET    /players/{player}/book/{id}/play          get the active game
POST   /players/{player}/book/{id}/play          make a move
DELETE /players/{player}/book/{id}/play          stop the active game
GET    /players/{player}/games                   list all the player's games
```

Here the server saves the game after every move, so only the option picked is sent.
Start a game and keep picking options from the section in each response until the game ends
(health gets to below 0, or you reach an END section).

## How the player games work

- a player can only have one active game per book, starting another one while it's active gives a 409
- when a game ends it stays in `/players/{player}/games`, so that list is also the player's history
- after a game ends you can start a new one for the same book
- GET, move and stop only work on the active game, so after a game ends they return a 404
- stop deletes the active game, finished games are kept


