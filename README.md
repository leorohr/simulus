Simulus is a university group project, aimed towards building a Java-based traffic simulation software.

To build this project run the ant-file build.xml.

```
ant -buildfile {project_root}/
``` 
will build the project, create a runnable jar file in {project_root}/build and create the JavaDoc in {project_root}/javadoc

```
ant -buildfile {project_root}/ javadoc
```
only creates the JavaDoc files.

```
ant -buildfile {project_root}/ jar
```
only creates the executable file.

Use
```
ant -buildfile {project_root}/ install
```
to install Simulus to ${user.home}/Simulus.


Compiling the project requires JDK1.8U40 and JAVA_HOME environment variable to be set to the JDK8 home folder.
