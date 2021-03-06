:include-markdown: installation-groovy-runner.md

# Minimal Groovy Setup

Generate webtau examples 

:include-cli-command: webtau --example

Navigate into `graphql` example

:include-cli-command: cd examples/graphql

:include-file: examples/graphql/introspection.groovy {title: "introspection.groovy"}

To run test

:include-cli-command: webtau introspection.groovy --url=http://localhost:8080 {paramsToHighlight: "url"}

:include-markdown: common/note-package-import.md

## Groovy Config File

Url parameter can be moved to a `webtau.cfg.groovy` file.  Please note that WebTau will automatically append `/graphql` to the url.

Webtau will attempt to send an introspection query in order to obtain information about the GraphQL schema.  If this fails, it
will not be able to record coverage information for GraphQL.  Should you wish to fail the test if introspection fails, please
set the `graphQLIgnoreIntrospectionFailures` property to `false`.

:include-file: examples/graphql/webtau.cfg.groovy {title: "webtau.cfg.groovy"}

[Specify multiple environments](configuration/environments) to streamline test execution.

# Minimal JUnit Setup

```tabs
Groovy: :include-file: maven/groovy-dep.xml {title: "Maven Dependency"}
Java: :include-file: maven/java-dep.xml {title: "Maven Dependency"}
```

```tabs
Groovy:
:include-file: com/example/tests/junit4/GraphQLWeatherGroovyIT.groovy {title: "JUnit 4 example"}
 
Java:
:include-file: com/example/tests/junit4/GraphQLWeatherJavaIT.java {title: "JUnit 4 example"}
```

## Junit Config File

Add `webtau.properties` to test class path

:include-file: src/test/resources/webtau-graphql.properties {title: "webtau.properties"}

# GraphQL Specifics

Before diving further into writing tests for your GraphQL server, please read through the REST testing documentation
starting with the [Data node page](REST/data-node) as much of the same core principles apply to GraphQL also.

The main GraphQL specific features are covered in the subsequent pages:
* [Queries and Mutations](GraphQL/queries-and-mutations)
* [Report](GraphQL/report)
