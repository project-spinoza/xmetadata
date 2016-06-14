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
  * xmeta connect `-host` db_host `-port` db_port `-user` db_user `-pass` db_pwd `-type` db_type<br>
  **Usage:** xmeta connect [options]<br>
  Options:<br>
  &nbsp;&nbsp;`--help`<br>
  &nbsp;&nbsp;&nbsp;&nbsp;Display help for command<br>
  &nbsp;&nbsp;&nbsp;&nbsp;Default: false<br>
  &nbsp;&nbsp;`-db`<br>
  &nbsp;&nbsp;&nbsp;&nbsp;optional database name<br>
  &nbsp;&nbsp;`* -host`<br>
  &nbsp;&nbsp;&nbsp;&nbsp;database host<br>
  &nbsp;&nbsp;`* -pass`<br>
  &nbsp;&nbsp;&nbsp;&nbsp;database pass<br>
  &nbsp;&nbsp;`* -port`<br>
  &nbsp;&nbsp;&nbsp;&nbsp;database port<br>
  &nbsp;&nbsp;&nbsp;&nbsp;Default: 0<br>
  &nbsp;&nbsp;`* -type`<br>
  &nbsp;&nbsp;&nbsp;&nbsp;database type e.g. mysql, postgre etc.<br>
  &nbsp;&nbsp;`* -user`<br>
  &nbsp;&nbsp;&nbsp;&nbsp;database user<br>

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
