## MTCG Protocol



>2022 WS - Software Engineering 1 Labor
>
>Xinlan Zhang
>
>if21b126
>
>`Git Repository: https://github.com/Milo59/NEW-MTCG.git`



- **Enter the program**

  This is an HTTP/REST-based server as description requested.

  **Server work flow:**

--> Run `Main.java` 

--> execute `server.start()`  method 

--> call `run()` method in `Server.java` 

--> execute `server.run() ` method, The thread pool is used here which will execute `run()`  method in `RequestHandler.class` .

After starting run the programm, `RequestHandler.class`  determines the path from Test Script, and different paths are handled by different apps. 

```java
@Override
public void run() { //jump to here from server
    try {
        Request request = getRequest();
        Response response = null;
        String path = request.getPath();
        //Determine the path
        if(path.equals("/")){
            // default Handler
            response = new GameApp().indexHandle(request);
        }else if (path.startsWith("/users")) {
            //eg. Handler detects users path, call userApp.handle()
            UserApp userApp = new UserApp();
            response = userApp.handle(request);
        }else if(path.equals("/sessions")){
            response = new SessionApp().handle(request);
        }else if(path.equals("/packages")){
            response = new PackageApp().handle(request);
        }else if(path.equals("/transactions/packages")){
            response = new TransactionsApp().handle(request);
        }else if(path.startsWith("/deck")){
            response = new DeckApp().handle(request);
        } else if(path.equals("/cards")){
                response = new CardApp().handle(request);
        }else if(path.equals("/stats")){
            response = new StatsApp().handle(request);
        }else if(path.equals("/score")){
            response = new ScoreBoardApp().handle(request);
        }else if(path.equals("/battles")){
            response = new BattlesApp().handle(request);
        }else if(path.startsWith("/tradings")){
            response = new TradeApp().handle(request);
        }else {
            // unrecognized request
            response = application.handle(request);
        }
        sendResponse(response);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        closeRequest();
    }
}
```



> For example: RequestHandler detects the user path and calls the handle method of the UserApp class.



```java
 @Override
    public Response handle(Request request) {
        String path = request.getPath();
        if (path.replaceAll("/users","").length()>1){
            //token verification
            Response response = new Response();
            response.setStatusCode(StatusCode.OK);
            response.setContentType(ContentType.TEXT_PLAIN);

            String token = request.getToken();
            String username = path.replaceAll("/users/","");
            response.setContent("authentication failed");
            if (null == token || null == MemorySession.get(token)){
                return response;
            }
            if (!MemorySession.get(token).getUsername().equals(username)){
                if (request.getMethod().equals(Method.GET.method)){
                    response.setContent("you can only view your own information");
                }else if (request.getMethod().equals(Method.PUT.method)){
                    response.setContent("you can only update your own information");
                }
                return response;
            }
        }
        return userController.handle(request);
    }
}
```



>Then enter the handel method of the usercontroller which will call different processing according to the curl method/path.



```java
public Response handle(Request request) {
    String method = request.getMethod();
    String path = request.getPath();

    if (path.equals("/users") && method.equals(Method.POST.method)) {
        //Determine whether it is a POST request method, indicating that a user is to be created
        return create(request);
    }

    if (path.startsWith("/users/") && request.getMethod().equals(Method.GET.method)) { // Get method go this way
        return find(request);
    }

    if (path.startsWith("/users/") && request.getMethod().equals(Method.PUT.method)) { // Post Method come here
        return update(request);
    }

    Response response = new Response();
    response.setStatusCode(StatusCode.METHODE_NOT_ALLOWED);
    response.setContentType(ContentType.TEXT_PLAIN);
    response.setContent(request.getMethod() + ": Not allowed for " + request.getPath());

    return response;
}
```



>The interface defined in the repository folder is called in the method implementation below the controller class, and the specific implementation is in the Db version of the Repository that interacts with the database. Here the `find` method is used as an example:



```java
private Response find(Request request) {
    ObjectMapper objectMapper = new ObjectMapper();
    String path = request.getPath();
    String username = path.replaceAll("/users/","");
    Response response = new Response();
    response.setStatusCode(StatusCode.OK);
    response.setContentType(ContentType.APPLICATION_JSON);
    try{
        User user = userRepository.findByUsername(username);
        String content = "";
        content = objectMapper.writeValueAsString(user);
        response.setContent("User information "+content);
    } catch (Exception e) {
        e.printStackTrace();
        response.setContent(e.getMessage());
    }
    return response;
}
```



>update methode fulfiled the requirements when need to edite the user profile.

`UserRepository:`

```java
public interface UserRepository {

     void update(User user) throws Exception;
```

`UserDbRepository:`

```java
@Override
public void update(User user) throws Exception { //edit user profile
    Connection connection = DatabaseUtil.getConnection();
    String userDeleteByUsernameSql = "UPDATE USERS SET NAME = ? , BIO = ?, IMAGE = ? WHERE USERNAME = ?";
    try (PreparedStatement ps = connection.prepareStatement(userDeleteByUsernameSql)) {
        ps.setString(1, user.getName());
        ps.setString(2, user.getBio());
        ps.setString(3, user.getImage());
        ps.setString(4, user.getUsername());
        ps.execute();
    } catch (Exception e) {
        throw e;
    }
}
```



>The last but not the least, the User class is pre-defined in the model folder. 

+++

- **Structure **

As mentioned above, different modules of this programm are also structured in this way. 

- controller
  - xxController
- model
- reposotiry
  - xxDbRepository
  - xxRepository



+++

- **Database**

+ A PostgreSQL database is run locally containerized. 

**`Docker:`**

docker stop swe1db 

docker rm  swe1db 

docker run --name swe1db -e POSTGRES_USER=swe1user -e POSTGRES_PASSWORD=swe1pw -e POSTGRES_DB=swe1db -p 5431:5432 postgres

```java
public class DatabaseUtil {
    //get the connection to the database
    private static String DB_URL = "jdbc:postgresql://localhost:5431/swe1db";
    private static String DB_USER = "swe1user";
    private static String DB_PW = "swe1pw";

    private static Connection connection;

    private DatabaseUtil() {
    }
```

+ Design the table and the columns to store data.

+ For convenience, the table was designed locally with visual tools and generated PostgreSQL statements, and the SQL generated table was run with console using IDEA's database plugin.

  

  `user table:`

  |  id  | username | password | money | name | image | scored | bio  |
  | :--: | -------- | -------- | ----- | ---- | ----- | ------ | ---- |
  |      |          |          |       |      |       |        |      |

  `package table:`

  |  id  | name | price | state |
  | :--: | :--: | :---: | :---: |
  |      |      |       |       |

  `card table:`

  |  id  | u_id | p_id | damage | name | deck |
  | :--: | :--: | :--: | :----: | :--: | :--: |
  |      |      |      |        |      |      |

  `battle_log table:`

  | id   | user1 | user2 | winner | user1_card_name | user1_card_damage | user2_card_name | user2_card_damage |
  | ---- | ----- | ----- | ------ | --------------- | ----------------- | :-------------: | :---------------: |
  |      |       |       |        |                 |                   |                 |                   |

  `trades table:`

  |  id  | u_id | cardtotrade | type | minimumdamage | status |
  | :--: | :--: | :---------: | ---- | :-----------: | :----: |
  |      |      |             |      |               |        |

  

+ **.Sql**

  ```postgresql
  create table users
  (
      id       serial
          primary key,
      username varchar(255)        not null,
      password varchar(255)        not null,
      money    integer             not null,
      name     varchar(255),
      bio      varchar(255),
      image    varchar(255),
      scored   integer default 100 not null
  );
  
  comment on table users is 'user table';
  
  comment on column users.id is 'id';
  
  comment on column users.username is 'username';
  
  comment on column users.password is 'password';
  
  comment on column users.money is 'money';
  
  alter table users
      owner to swe1user;
  
  create unique index username_unique
      on users (username);
  
  
  
  create table packages
  (
      id           varchar(36)       not null
          primary key,
      name         varchar(36)       not null,
      price        integer default 5 not null,
      state        integer default 1 not null,
      updated_time timestamp
  );
  
  alter table packages
      owner to swe1user;
  
  create table card
  (
      id     varchar(36)    not null
          primary key,
      u_id   integer
          references users,
      p_id   varchar(36)    not null
          references packages,
      damage numeric(24, 1) not null,
      name   varchar(255)   not null,
      deck   integer default 0
  );
  
  alter table card
      owner to swe1user;
  
  
  
  create table trades
  (
      id            varchar(36) not null
          primary key,
      u_id          integer
          references users,
      cardtotrade   varchar(90) not null,
      type          varchar(36) not null,
      minimumdamage integer     not null,
      status        integer default 0
  );
  
  comment on column trades.status is 'default 0 means have not been traded yet';
  
  alter table trades
      owner to swe1user;
  
  
  
  create table battle_log
  (
      id                serial
          primary key,
      user1             varchar(255)   not null,
      user2             varchar(255)   not null,
      winner            varchar(255)   not null,
      user1_card_name   varchar(36)    not null,
      user1_card_damage numeric(24, 1) not null,
      user2_card_name   varchar(36)    not null,
      user2_card_damage numeric(24, 1) not null
  );
  
  comment on table battle_log is 'battle_log';
  
  comment on column battle_log.id is 'id';
  
  comment on column battle_log.user1 is 'user1';
  
  comment on column battle_log.user2 is 'user2';
  
  comment on column battle_log.winner is 'winner';
  
  comment on column battle_log.user1_card_name is 'user1_card';
  
  comment on column battle_log.user1_card_damage is 'user1_card_damage';
  
  comment on column battle_log.user2_card_name is 'user2_card_name';
  
  comment on column battle_log.user2_card_damage is 'user2_card_damage';
  
  alter table battle_log
      owner to swe1user;
  ```

  

+++

- **Features**

  Implement object serialization and deserialization via Jackson. 

  > Users of the system are divided into admin and players, administrators can configure packages in the server users can buy packages after registration and login , the cards in the package can be used to play against other players, the winner can take over this card. Players can view their own information to modify the profile page. There is a system of ranking and score.

  + Authentification

    Authentication is based on the request `header `, and each request from the user will carry an authentication message in the request `header `. This information is then used to authenticate login or permission.

    The concrete implementation is that when a user requests a service it will take a request header. The program will parse this request header. Then it will take out the user information from the map of the session, and it can determine whether the user is logged in or not, which means that if the user has access to different paths accordingly.

  + Unique feature

    Can count the number of people online by returning the number encapsulated in the `map()` by the `online() `  method.

    logout - The `remove() ` method removes the data in the `map` collection based on the transmitted token `request.getToken().`

    

  +++

  

- **Unit Test**

  - Why those be chosen

    When the code has been written, I want to verify by my own test code if there are logic errors or if it really works, creating users/trades/packages and other important features.

  - why critical

    To verify the correctness of my functions. Unit testing concentrates on the basic components of the program, ensuring the logic of my program in order to finally compose a complete project.

    

  +++

  

- **Tracked Time**

  Git log showed from 09.Dec.2022, but actually starting to understand and learn about this project is with this semester, and in November, December and January focused on implementing features.

  - **failures and selected solutions**

    For example:

    - I had a few times of having problems with using Version Control (Git) and lossing of code when change branchs at first, because I connected to the lektorr's repository at the beginning and did not commit my own code, even later I could not successfully pull the code to the project. 

      First create a new remote repository, then clone it to a new local folder. In the new folder, I tried to merge the development features in both branches, and then dropped the develop branch for simplicity and used master directly.

    - When judging whether the card meets the transaction conditions, I can view the information in the card's table that the conditions are met to prove that it should be able to trade successfully, but eventually the console shows that not allowed trade.
      After adding the breakpoint debugging, I found that it was because of the syntax problem of the judgment condition. I should use `.equals()` instead of directly using `==` .

      

    ---

    

- **Lessons-Learned**

  Learned...

  - how to manipulate the database through Java JDBC.

  - how to add delete modify queries to data.

  - how to use version control to push extracts to true and false commits.

  - How to resolve conflicts when Git is used.

  - How to write unit tests how to run unit tests.

  - postgresql

  - Jackson serialization and deserialization.

    ...

    





