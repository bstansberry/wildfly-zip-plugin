# wildfly-zip-plugin
Provides Plexus components to support 'zip' as a Maven packaging type. Provides a Maven mojo to attach a configured zip file to the project as the main artifact.

A typical use case for this would be a maven module that uses the `maven-assembly-plugin` to produce a zip, and that zip is the primary output of the module.

This project is primarily a workaround to an issue where `nxrm3-maven-plugin` was having problems dealing with `pom` packaging projects that attached a zip to the project with no Maven classifier. Using this plugin allows the packaging to be `zip` and for the zip file to be used as the main artifact of the module.

# Usage

Imagine a project where `maven-assembly-plugin` or some other tool produces a zip in the `target` dir with a file name equivalent to `${project.artifactId}-${project.version}.zip`. Using this plugin would look like this:

```
<project>
    
    ....
    
    <groupId>com.example</groupId>
    <artifactId>foo-zip</groupId>
    <!-- Custom packaging type provided by Maven extension
         org.wildfly.plugins:wildfly-zip-plugin -->
    <packaging>zip</packaging>

    ....
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.wildfly.plugins</groupId>
                <artifactId>wildfly-zip-plugin</artifactId>
                <version>${version.org.wildfly.plugins.wildfly-zip-plugin}</version>
                <!-- Tell maven this is an extension so it will make 'zip' packaging available. -->
                <extensions>true</extensions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <executions>
                    <execution>
                        <id>default-single</id>
                        <!-- Use 'prepare-package' so the zip is ready 
                         in 'package' phase for the wildfly-zip-plugin -->
                        <phase>prepare-package</phase>
                        ....
                        <configuration>
                            <!-- Don't attach the artifact; let wildfly-zip-plugin do it -->
                            <attach>false</attach>
                            <!-- Don't add a classifier; this is the main artifact for this module -->
                            <appendAssemblyId>false</appendAssemblyId>
                            ....
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>
```

The project provides a `package` mojo that attaches a specified file as the project's main artifact. It supports two configuration options:

* `artifact-directory` -- directory where the artifact is located. Defaults to `${project.build.directory}`.
* `artifact-name` -- file name of the artifact. Defaults to `${project.artifactId}-${project.version}.zip`

When the plugin is used as a maven extension (`<extensions>true</extensions>` in the plugin declaration), a `default-package` maven execution will run the `package` goal in the `package` phase.