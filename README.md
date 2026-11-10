# Kerala Outlet Guide

An unofficial Android study project for browsing Kerala BEVCO outlets by district, finding a nearby outlet, and viewing the published price list.

## Start here · Day 1

**Goal:** Serve Kerala's 14 districts with a Java 21 Spring Boot REST API. The backend is a Maven project in `backend/`.

1. Install **JDK 21** and **Maven 3.6.3 or later**. Run `mvn -version` and check that Maven is using Java 21.
2. In IntelliJ, open [`backend/pom.xml`](backend/pom.xml) as a project, select JDK 21 as the Project SDK, and let Maven import the dependencies. From the `backend` folder, run the tests and start the server:

   ```shell
   mvn test
   mvn spring-boot:run
   ```

   You can also open `KeralaOutletGuideApplication.java` and run its `main()` from IntelliJ.

3. Visit [http://127.0.0.1:8080/api/v1/districts](http://127.0.0.1:8080/api/v1/districts) in a browser, or send a **GET** request to that URL in Bruno.

   ```shell
   curl -i http://127.0.0.1:8080/api/v1/districts
   ```

   Expect HTTP `200`, JSON, and 14 district entries in alphabetical order. `id` is the stable key the Android app will send when a user selects a district. `name` is the label it will display.

4. Follow [`KeralaOutletGuideApplication`](backend/src/main/java/com/keralaoutletguide/api/KeralaOutletGuideApplication.java) → [`DistrictController`](backend/src/main/java/com/keralaoutletguide/api/district/DistrictController.java) → [`DistrictService`](backend/src/main/java/com/keralaoutletguide/api/district/DistrictService.java). The service reads [`districts.json`](backend/src/main/resources/districts.json) once at startup. Spring MVC converts the returned Java record into JSON.
5. Try `GET /api/v1/districts/` (expect `404`) and `POST /api/v1/districts` (expect `405` and `Allow: GET`). Use **Ctrl+C** to stop the server.

### Build and run the executable JAR

From the `backend` folder:

```shell
mvn clean package
java -jar target/kerala-outlet-guide-api-0.1.0-SNAPSHOT.jar
```

If port 8080 is busy, set `BEVCO_PORT` to `8081` before running. In Bash use `export BEVCO_PORT=8081`. In PowerShell use `$env:BEVCO_PORT='8081'`. Update the request URL to match.

**Checkpoint:** You can rebuild the server, call the API from Bruno, and explain why the frontend should use `district.id` rather than `district.name` in the outlet request.

## Project guide

| File | What to look for |
| --- | --- |
| [Day 1 Spring Boot walkthrough](docs/day-1-spring-boot.md) | How the POM, controller, service, records, and tests work together |
| [Development plan](docs/development-plan.md) | Daily milestones through building, installing, and checking the APK |
| [Architecture and sequences](docs/architecture.md) | System diagram and request flows for all three features |
| [UI and UX](docs/ui-ux.md) | Four Android screen mockups, navigation, and error states |
| [API guide](docs/api-guide.md) | Examples and rules for the implemented and planned endpoints |
| [OpenAPI contract](docs/openapi.yaml) | Machine-readable API spec with implementation status per endpoint |

Day 1 uses Spring Boot 4.0.9 and a bundled district list. Maven downloads the Spring dependencies on the first build.
