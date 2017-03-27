# Review app jenkins plugin

Jenkins plugin for shutting down review applications

## Getting started

### Prerequisites

- [JDK 8](https://www.oracle.com/java)

### Building

Project uses [Maven](https://maven.apache.org/) as a build tool but you don't have install it locally since the project comes with `./mvnw` wrapper script.

To build project please execute following command:

```bash
    ./mvn package
```

### Running

```bash
    ./mvnw hpi:run -Djetty.port=8090
```

Jenkins will be available on http://localhost:8090/jenkins
You can ignore the million stacktraces that happen when you startup jenkins...

The first time you start jenkins you will need to configure the default user and choose plugins to install, accept the default plugins.

## Developing

### Unit tests

To run all unit tests please execute following command:

```bash
    ./mvnw test
```

## Versioning

We use [SemVer](http://semver.org/) for versioning.
For the versions available, see the tags on this repository.

## License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

