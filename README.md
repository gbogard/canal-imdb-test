# Canal+ Test (IMDB Stream processing)

## The need 

Given several TSV files from IMDB, write a service to :
  - Find the members of the crew given the name of a movie
  - Find a list of TV Shows by decreasing order of episode numbers
  
Usage of Akka streams is mandatory.

## The current solution

We use Akka Streams to read the big TSV files and populate a SQLite Database, and then query that database.
The project is divided in 5 sub-projects:
    - `domain` defines the domain entities and the interfaces to our repositories and our MovieService. All other
    projects depend on it.
    - `databaseInfrastructure` connects to the database and implements repositories and MovieService by providing
    the actual SQL requests.
    - `databasePopulator` creates the database schema and populates the database with data from the TSV files. Makes
    heavy use of Akka Streams.
    - `httpApi` implements the MovieService with Akka Streams' `Source` as a concrete type and exposes our service through a simple API
    - `benchmarks` is pretty self explanatory

## How to use

### Configuring the program

There are two environment variables that you can override:

- `JDBC_URL` : defaults to `jdbc:sqlite:canal.db`
- `TSV_FILES_LOCATION` : defaults to `$PWD`

You need to put the following files in `TSV_FILES_LOCATION` :

- `titles.basics.tsv`
- `titles.episodes.tsv`
- `titles.principals.tsv`
- `names.basics.tsv`

### Migrating the database

You need to run the database populator before you can use the aplication. When used with no argument,
it will create the database schema, and populate the database with all available entities :

```
sbt databasePopulator/run
```

You can also specify entities you want to populate :

```
sbt "databasePopulator/run --only people"
```

For a help message type

```
sbt "databasePopulator/run --help"
```

### Running the HTTP API

### Running benchmarks

## Previous solution

