Simulus is a university group project, aimed towards building a Java-based traffic simulation software.

To build this project run the ant-file build.xml.

```
ant {PROJECT_ROOT}/
``` 
will build the project and create a runnable jar file in {PROJECT_ROOT}/deploy.

```
ant javadoc
```
creates the JavaDoc files in {PROJECT_ROOT}/deploy/doc

```
ant full_build
```
runs both of these targets.

Compiling the project requires JDK1.8U40
