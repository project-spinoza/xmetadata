# xmetadata
Meta data extractor from various databases

# what to extract?

## LIST
  * list databases
  * list tables
  * list columns

## GET
  * get columnType
  * get primary key
  * get foreign key
  * get columnLenght
  * get columnAtPosition
  * get foregn Keys referenced table
  * get indexes (primary and foreign) for a table
  * get All indexes and referreced table
  * get stored procedures

## EXISTS
  * exists database [db_name] ?
  * exists table [table_name] ?
  * exists column [column_name] ?

## SUPPORTS (supported features)
  * supports groupBy ?
  * supports outerJoin ?
  * supports innerJoin ?
  * etc...
 
# How to implement

 ### CORE CLASSES
  * A wraper for the underline JDBC driver
  * authenticate user
  * connect to db
  * extract meta data
 
### COMMAND LINE WRAPER
 * A wraper for the CORE CLASSES
 * use core features through command line

### REST SERVICE WRAPER
 * Use Vertx micro services
 * A wraper for the CORE CLASSES
 * define specific end point for each core feature
 * generates Json as a response
