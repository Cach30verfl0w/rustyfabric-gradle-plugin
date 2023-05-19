package de.cacheoverflow.rustyfabric.plugin.cargo.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AbstractCargoTask extends DefaultTask {

    private final MapProperty<String, String> environmentVariables;
    private final Property<String> executable;
    private final DirectoryProperty sourceFolder;
    private final DirectoryProperty workingFolder;
    private final Property<Boolean> releaseMode;
    private final String cargoTask;

    public AbstractCargoTask(@NotNull final String cargoTask) {
        ObjectFactory factory = this.getProject().getObjects();
        this.environmentVariables = factory.mapProperty(String.class, String.class);
        this.executable = factory.property(String.class);
        this.sourceFolder = factory.directoryProperty();
        this.workingFolder = factory.directoryProperty();
        this.releaseMode = factory.property(Boolean.class);
        this.cargoTask = cargoTask;
    }

    @TaskAction
    public void performTask() {
        // Copy Source Directory into Working Directory
        this.getLogger().info("Copy Source Folder into Working Folder...");
        this.copyDirectory(this.sourceFolder.getAsFile().get().toPath(), this.workingFolder.getAsFile().get().toPath());

        // Run Process
        ProcessBuilder processBuilder = this.buildProcess();
        try {
            Process process = processBuilder.start();
            String line;
            try (BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    // Read normal lines
                    while ((line = outputReader.readLine()) != null) {
                        this.getLogger().info(line);
                    }

                    // Read error lines
                    if ((line = errorReader.readLine()) != null) {
                        this.getLogger().info(line);
                        while ((line = errorReader.readLine()) != null) {
                            this.getLogger().info(line);
                        }
                    }

                    // Wait for exit
                    int exitCode = process.waitFor();
                    if (exitCode != 0) {
                        throw new GradleException(String.format("Cargo returned with error exit code %d", exitCode));
                    }
                }
            }
        } catch (IOException | InterruptedException ex) {
            throw new GradleException("Unable to run Cargo process", ex);
        }
    }

    private @NotNull ProcessBuilder buildProcess() {
        List<String> arguments = Arrays.asList(this.executable.getOrElse("cargo"), this.cargoTask);
        ProcessBuilder processBuilder = new ProcessBuilder(arguments);
        processBuilder.environment().putAll(this.environmentVariables.getOrElse(new HashMap<>()));
        processBuilder.directory(this.workingFolder.getAsFile().get());
        return processBuilder;
    }

    private void copyDirectory(@NotNull final Path sourceDirectory, @NotNull final Path targetDirectory) {
        try {
            Files.walkFileTree(sourceDirectory, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetFile = targetDirectory.resolve(sourceDirectory.relativize(file));
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path visitedTargetDirectory = targetDirectory.resolve(sourceDirectory.relativize(dir));
                    Files.createDirectories(visitedTargetDirectory);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ex) {
            this.getLogger().error("Unable to copy sources to build target", ex);
        }
    }

    @Input
    public @NotNull MapProperty<String, String> getEnvironmentVariables() {
        return this.environmentVariables;
    }

    @Input
    public @NotNull Property<String> getExecutable() {
        return this.executable;
    }

    @Input
    public @NotNull Property<Boolean> getReleaseMode() {
        return this.releaseMode;
    }

    @InputDirectory
    public @NotNull DirectoryProperty getSourceFolder() {
        return this.sourceFolder;
    }

    @OutputDirectory
    public @NotNull DirectoryProperty getWorkingFolder() {
        return this.workingFolder;
    }

}
