# petstore-qa


To run tests:
```bash
mvn clean test -Pqa -Dsuite=suites/regression.xml
```
**-Pqa** - Environment configured by profiles.

**-Dsuite=suites/regression.xml**  - Suites to run


To generate report:

```bash
mvn allure:serve
```
