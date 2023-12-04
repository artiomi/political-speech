## Political speech evaluation application

### Implementation details
The following application provide functionality to evaluate political candidates speeches and give response to below questions:
- Which politician gave the most speeches in 2013?
- Which politician gave the most speeches on "homeland security"?
- Which politician spoke the fewest words overall?
<p>
In order to provide above information, was developed a REST API endpoint which receives a list of URLs, which points to CSV files with political speeches details.<br>

```shell
  curl -X 'GET' \
  'http://localhost:8080/evaluation?url1=file:/D:/data/test-input1.csv&url2=file:/D:/data/test-input2.csv' \
  -H 'accept: application/json'
```
__Response format:__ <br>
```
{
 "mostSpeeches": string|null,
 "mostSecurity": string|null,
 "leastWordy": string|null
}
```

#### Input CSV file format
The CSV file should contain these headers: `Speaker ; Topic ; Date ; Words` which are separated by `;`.<br>
__Example:__

```
Speaker;Topic;Date;Words
Alexander Abel; education policty; 2012-10-30; 5310
Bernhard Belling; coal subsidies; 2012-11-05; 1210
Caesare Collins; coal subsidies; 2012-11-06; 1119
Alexander Abel; homeland security; 2012-12-11; 911
```

#### Execution steps
- Endpoint accepts query parameters in following form: param name should have format `urlN`, where `N` is an `Int > 0`, 
 param value should be a valid URL to a CSV file, currently are supported only`file, http, https` schemas.
 By default, the endpoint accepts up to 5 query params, this value can be updated via `app.speech.validator.max-allowed-urls` application property.
- Content of the CSV file to which URL points is read and mapped to a POJO.
- CSV records are mapped with a `batchId`(random value generated for each evaluation request) and stored in database for evaluation calculations from next step.
 This solution was chosen in order to use SQL aggregation functionality for further calculations. 
- Each evaluation index calculation is done by a dedicated component.
  Currently, all components, run a custom SQL query for specific `batchId`, with additional filters (if needed) 
 and return aggregated results per candidate.  Some components accept customization via properties:
  - For *most speeches per year*, via property `app.speech.aggregation.speech-year`, can be updated the year for which aggregation is made.
  Default:`2013`.
  - For *topic related speech*, via property `app.speech.aggregation.speech-topic` , can be updated the topic for which aggregation is made.
  Default: `homeland security`.

### Build & Run
Java - `21`<br>
Kotlin - `1.9.20`<br>
Spring Boot - `3.2.0`

#### Build
```shell
mvn clean compile
```

### Package
```shell
mvn clean package
```
**Note:** In order to package and run all test, it is required to have __docker__ process running, because there are integration
tests which requires database instance.<br>
It is possible to skip tests by running:
```shell
mvn clean package -DskipTests
```

### Run
**Note:** Before running the app on local, it is required to start docker instance, by running below command in project's root directory: `docker-compose up -d`
```shell
mvn spring-boot:run
```
### Swagger
After app startup, Swagger documentation will be available [here](http://localhost:8080/swagger-ui/index.html).
