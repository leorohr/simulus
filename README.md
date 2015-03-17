Simulus is a university group project, aimed towards building a Java-based traffic simulation software.

To build this project run the ant-file build.xml.

```
ant -buildfile {PROJECT_ROOT}/
``` 
will build the project and create a runnable jar file in {PROJECT_ROOT}/deploy.

```
ant -buildfile {PROJECT_ROOT}/ javadoc
```
creates the JavaDoc files in {PROJECT_ROOT}/deploy/doc

```
ant -buildfile {PROJECT_ROOT}/ full_build
```
runs both of these targets.

Compiling the project requires JDK1.8U40
