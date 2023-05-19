package de.cacheoverflow.rustyfabric.plugin.cargo.tasks;

import de.cacheoverflow.rustyfabric.plugin.cargo.dependency.ICargoDependency;
import de.cacheoverflow.rustyfabric.plugin.cargo.extension.CargoPluginExtension;
import de.cacheoverflow.rustyfabric.plugin.cargo.extension.CargoProjectExtension;
import de.cacheoverflow.rustyfabric.plugin.IOHelper;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CargoInitTask extends DefaultTask {

    private final CargoPluginExtension pluginExtension;
    private final DirectoryProperty sourceFolder;
    private final DirectoryProperty workingFolder;

    public CargoInitTask() {
        ObjectFactory factory = this.getProject().getObjects();
        this.pluginExtension = this.getProject().getExtensions().getByType(CargoPluginExtension.class);
        this.sourceFolder = factory.directoryProperty();
        this.workingFolder = factory.directoryProperty();
    }

    @TaskAction
    public void performTask() {
        StringBuilder cargoTomlBuilder = new StringBuilder();
        if (!this.workingFolder.getAsFile().get().mkdirs())
            this.getLogger().info("Skipping working folder creation => Already existing!");

        CargoProjectExtension projectExtension = this.pluginExtension.getCargoProjectExtension();
        // Package Information
        cargoTomlBuilder.append("[package]\n");
        cargoTomlBuilder.append("name = \"").append(projectExtension.getProjectName().getOrElse(this.getProject().getName())).append("\"\n");
        cargoTomlBuilder.append("version = \"").append(projectExtension.getVersion().getOrElse(this.getProject().getVersion().toString())).append("\"\n");
        cargoTomlBuilder.append("\n");
        // Lib
        cargoTomlBuilder.append("[lib]\n");
        cargoTomlBuilder.append("crate-type = [\"cdylib\"]\n");

        // Dependencies
        for (ICargoDependency dependency : this.pluginExtension.getCargoDependencies()) {
            cargoTomlBuilder.append(dependency.buildCargoDependencyString());
        }

        // Write
        IOHelper.writeFile(new File(this.workingFolder.getAsFile().get(), "Cargo.toml"), cargoTomlBuilder.toString());
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
