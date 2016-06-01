# xmetadata
Meta data extractor from various databases

# what to extract?

### LIST
  * list databases
  * list tables
  * list columns

### GET
  * get columnType
  * get primary key
  * get foreign key
  * get columnLength
  * get columnAtPosition
  * get foregn Keys referenced table
  * get indexes (primary and foreign) for a table
  * get All indexes and referreced table
  * get stored procedures

### EXISTS
  * exists database [db_name] ?
  * exists table [table_name] ?
  * exists column [column_name] ?

### SUPPORTS (supported features)
  * supports groupBy ?
  * supports outerJoin ?
  * supports innerJoin ?
  * etc...
 
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
