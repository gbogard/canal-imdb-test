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
    
## Http Api endpoints

### Retrieve the cast of a movie

```
GET http://localhost:9000/titles/cast?title=The%20Sucker
```

```
[{"id":"nm1781213","primaryName":"Matty Cardarople","birthYear":1983,"deathYear":null,"primaryProfession":["actor","miscellaneous","director"],"category":"actor","job":null,"characters":"[\"Vlad\"]"},{"id":"nm0000086","primaryName":"Louis de Funès","birthYear":1914,"deathYear":1983,"primaryProfession":["actor","writer","soundtrack"],"category":"actor","job":null,"characters":"[\"Léopold Saroyan\"]"},{"id":"nm0100186","primaryName":"Bourvil","birthYear":1917,"deathYear":1970,"primaryProfession":["actor","soundtrack","writer"],"category":"actor","job":null,"characters":"[\"Antoine Maréchal\"]"},{"id":"nm0350768","primaryName":"Henri Génès","birthYear":1919,"deathYear":2005,"primaryProfession":["actor","soundtrack"],"category":"actor","job":null,"characters":"[\"Martial\"]"},{"id":"nm0892891","primaryName":"Venantino Venantini","birthYear":1930,"deathYear":2018,"primaryProfession":["actor"],"category":"actor","job":null,"characters":"[\"Mickey dit le bègue ou la souris\"]"}]
```

### Retrieve the titles by count of episodes

```
GET http://localhost:9000/titles/countEpisodes?limit=2
```

```
[{"id":"tt0058796","titleType":"tvSeries","primaryTitle":"Days of Our Lives","originalTitle":"Days of Our Lives","isAdult":false,"startYear":1965,"endYear":null,"runtimeMinutes":60,"genres":["Drama","Romance"],"episodeCount":11388},{"id":"tt0053494","titleType":"tvSeries","primaryTitle":"Coronation Street","originalTitle":"Coronation Street","isAdult":false,"startYear":1960,"endYear":null,"runtimeMinutes":30,"genres":["Drama","Romance"],"episodeCount":9845}]
```

The `limit` parameter is optional. It will be 10 by default, and cannot exceed 100.

## How to use

### Using the pre-built Docker image

For convenience, I have built a Docker image that already includes a populated database. You can just run it

```
docker run -p 9000:9000 gbogard/imdb-api-canal
```

and start making HTTP request immediately.

### Creating the database and running the program locally

#### Configuring the program

There are four environment variables that you can override:

- `JDBC_URL` : defaults to `jdbc:sqlite:canal.db`
- `TSV_FILES_LOCATION` : defaults to `$PWD`
- `API_HOST` : defaults to `0.0.0.0`
- `API_PORT` : defaults to `9000`

You need to put the following files in `TSV_FILES_LOCATION` :

- `titles.basics.tsv`
- `titles.episodes.tsv`
- `titles.principals.tsv`
- `names.basics.tsv`

#### Migrating the database

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

The database population process will take at least 10 minutes and the database file will take about 3.7GB on your system.

#### Running the HTTP API

```
sbt httpAPi/run
```

## Run the tests

```
sbt test
```