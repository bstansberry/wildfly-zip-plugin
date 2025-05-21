/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.wildfly.plugins.zip;

import java.io.File;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

@Mojo(name = "package", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME, defaultPhase = LifecyclePhase.PACKAGE)
public class ZipPackagingMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;

    /**
     * The directory for the built artifact.
     */
    @Parameter(alias="artifact-directory", defaultValue = "${project.build.directory}")
    private File artifactDirectory;

    /**
     * The file name of the built artifact.
     */
    @Parameter(alias="artifact-name", defaultValue = "${project.artifactId}-${project.version}.zip")
    private String artifactName;

    @Override
    public void execute() throws MojoFailureException {
        if (!artifactDirectory.exists()) {
            throw new MojoFailureException("artifact-directory " + artifactDirectory.getAbsolutePath()
                    + " does not exist");
        } else if (!artifactDirectory.isDirectory()) {
            throw new MojoFailureException("artifact-directory " + artifactDirectory.getAbsolutePath()
                    + " is not a directory");
        }

        File f = artifactDirectory.toPath().resolve(artifactName).toFile();
        if (!f.exists()) {
            throw new MojoFailureException("artifact " + f.getAbsolutePath()
                    + " does not exist");

        } else if (f.isDirectory()) {
            throw new MojoFailureException("artifact " + f.getAbsolutePath()
                    + " is a directory");
        }
        project.getArtifact().setFile(f);
    }
}
