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
when started with `--mode rest` the application starts as a REST service.

# REST end points
  * List available routes   `/xmeta/api/v1/`
  * Show databases   `/xmeta/api/v1/list/db`
  * Show tables   `/xmeta/api/v1/list/table/:database`
  * Show columns `/xmeta/api/v1/list/column/:database/:table`
  * Show columns with types `/xmeta/api/v1/list/columnWithType/:database/:table`
  * Get column type `/xmeta/api/v1/get/columnType/:database/:table/:column`
  * Get column length `/xmeta/api/v1/get/columnLength/:database/:table/:column`
  * Get column at pos `/xmeta/api/v1/get/columnAtPos/:database/:table/:columnIndex`
  * Get primary key `/xmeta/api/v1/get/primaryKey/:database/:table`
  * Get foreign key `/xmeta/api/v1/get/foreignKey/:database/:table`
  * Get foreign key reference tables** `/xmeta/api/v1/get/foreignKeysRefTable/:database/:table`
  * Get indexes `/xmeta/api/v1/get/indexes/:database/:table`
  * Get all indexes and ref tables `/xmeta/api/v1/get/allIdxRefTable/:database/:table`
  * Database exists `/xmeta/api/v1/exists/db/:db`
  * Table exists `/xmeta/api/v1/exists/table/:table`
  * Column exists `/xmeta/api/v1/exists/column/:column`
  * Supported features `/xmeta/api/v1/get/supports/:feature`
  * Get database info `/xmeta/api/v1/info/db/`
  * Get database driver info `/xmeta/api/v1/info/dbdriver/`

# Available commands
  **NOTE**: A question Mark(?) before option represents optional parameter
  * `xmeta --help`<br>
    shows all available commands with their usage

  * `xmeta <commandName> --help`<br>
    shows help regarding this command
  * `xmeta connect -host <db_host> -port <db_port> -user <db_user> -pass <db_pwd> -type <db_type>`<br>
    connects to database `-type` represents type of db to connect to e.g. mysql | postgre
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
