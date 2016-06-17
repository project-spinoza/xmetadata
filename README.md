# xmetadata
Meta data extraction from various databases

# How to build
 * Download `git clone https://github.com/project-spinoza/xmetadata.git`
 * Change Dir `cd xmetadata`
 * Build `mvn clean install`
 
# How to run
  * RUN `sh launch.sh --mode cmd`<br><br>
  **NOTE** `--mode` can take two values `cmd` OR `rest`<br>
when started with `--mode cmd` the application starts as a commandline tool<br>
when started with `--mode rest` the application starts as a REST service.<br>
By default the service will be accessible on port `8181` e.g. `http://localhost:8181/<one_of_the_following_end_points>`

# REST end points
  * `POST` Authorization end point `/auth`
  * `GET` List available routes   `/xmeta/api/v1/`
  * `GET` Show databases   `/xmeta/api/v1/list/db`
  * `GET` Show tables   `/xmeta/api/v1/list/table/:database`
  * `GET` Show columns `/xmeta/api/v1/list/column/:database/:table`
  * `GET` Show columns with types `/xmeta/api/v1/list/columnWithType/:database/:table`
  * `GET` Get column type `/xmeta/api/v1/get/columnType/:database/:table/:column`
  * `GET` Get column length `/xmeta/api/v1/get/columnLength/:database/:table/:column`
  * `GET` Get column at pos `/xmeta/api/v1/get/columnAtPos/:database/:table/:columnIndex`
  * `GET` Get primary key `/xmeta/api/v1/get/primaryKey/:database/:table`
  * `GET` Get foreign key `/xmeta/api/v1/get/foreignKey/:database/:table`
  * `GET` Get foreign key reference tables** `/xmeta/api/v1/get/foreignKeysRefTable/:database/:table`
  * `GET` Get indexes `/xmeta/api/v1/get/indexes/:database/:table`
  * `GET` Get all indexes and ref tables `/xmeta/api/v1/get/allIdxRefTable/:database/:table`
  * `GET` Database exists `/xmeta/api/v1/exists/db/:db`
  * `GET` Table exists `/xmeta/api/v1/exists/table/:table`
  * `GET` Column exists `/xmeta/api/v1/exists/column/:column`
  * `GET` Supported features `/xmeta/api/v1/get/supports/:feature`
  * `GET` Get database info `/xmeta/api/v1/info/db/`
  * `GET` Get database driver info `/xmeta/api/v1/info/dbdriver/`
 
### How to access the service
  * After running the application in `--mode rest` do the following:
  * Send Post request to `/auth`, with json data in the following format:<br>
    `{"db_host":"<db_host>","db_port":<db_port> ,"db_user":"<db_user>","db_pass":"<db_pass>","db_type":"mysql"}`
  * If your credentials are correct the service will generate and return a `token` in json response. This `token` will be used in the   header for other subsequent requests otherwise the service will return `unauthorized` in response. You have to send this `token`    in the request header in this format `Authorization: bearer <your_token>`
   
  
# Available commands
  **NOTE**: A question Mark(?) before option represents optional parameter
  * `xmeta --help`<br>
    shows all available commands with their usage

  * `xmeta <commandName> --help`<br>
    shows help regarding this command

  * `xmeta connect -host <db_host> -port <db_port> -user <db_user> -pass <db_pwd> -type <db_type>`<br>
    connects to database `-type` represents type of db to connect to e.g. mysql | postgresql

  * `xmeta showDatabases`<br>
    shows available databases

  * `xmeta showTables -db <db_name>`<br>
    shows available tables in the database

  * `xmeta showColumns -db <db_name> -tbl <table_name>`<br>
    shows all columns in the table

  * `xmeta showColsWithType -db <db_name> -tbl <table_name>`<br>
    shows all columns along with their data type in the table

  * `xmeta getColType -db <db_name> -tbl <table_name> -col <col_name>`<br>
    shows data type of the single column

  * `xmeta getColLength -db <db_name> -tbl <table_name> -col <col_name>`<br>
    shows length of the column

  * `xmeta getColAtPos -db <db_name> -tbl <table_name> -pos <col_pos>`<br>
    shows column at position `-pos`

  * `xmeta getPK -db <db_name> -tbl <table_name>`<br>
    prints primary key of the table

  * `xmeta getFK -db <db_name> -tbl <table_name>`<br>
    prints foreign key of the table

  * `xmeta getFKRefTables -db <db_name> -tbl <table_name> -col <col_name>`<br>
    prints foreign keys and their reference tables

  * `xmeta getIndexes -db <db_name> -tbl <table_name>`<br>
   prints indexes in the table

  * `xmeta getAllIdxRefTable -db <db_name> -tbl <table_name>`<br>
   prints all indexes and their reference tables

  * `xmeta existsDatabase -db <db_name>`<br>
    prints `true` if database exists `false` other wise

  * `xmeta existsTable -db <db_name> -tbl <table_name>`<br>
    prints `true` if table exists `false` other wise

  * `xmeta existsColumn -db <db_name> -tbl <table_name> -col <col_name>`<br>
    prints `true` if column exists `false` other wise

  * `xmeta isSupport -f <feature_name>`<br>
    prints `true` if the feature `-f` supported by the database driver

# How to implement

### CORE CLASSES
  * A wraper for the underlying DB driver
  * connect to db
  * extract meta data
 
### COMMAND LINE WRAPER
 * A wraper for the CORE CLASSES
 * use core features through command line

### REST SERVICE
 * Use Vertx micro services
 * User authentication
 * A wraper for the CORE CLASSES
 * define specific end points for each core feature
 * generate Json as a response
