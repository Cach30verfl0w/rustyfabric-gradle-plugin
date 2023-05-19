package de.cacheoverflow.rustyfabric.plugin.cargo.extension;

import de.cacheoverflow.rustyfabric.plugin.cargo.dependency.GitCargoDependency;
import de.cacheoverflow.rustyfabric.plugin.cargo.dependency.ICargoDependency;
import de.cacheoverflow.rustyfabric.plugin.cargo.dependency.LocalCargoDependency;
import de.cacheoverflow.rustyfabric.plugin.cargo.dependency.RemoteCargoDependency;
import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargoPluginExtension {

    private final List<ICargoDependency> cargoDependencies = new ArrayList<>();

    private final CargoProjectExtension cargoProject;
    private final MapProperty<String, String> environmentVariables;
    private final Property<String> executable;
    private final DirectoryProperty sourceFolder;
    private final DirectoryProperty workingFolder;
    private final Property<Boolean> releaseMode;

    @Inject
    public CargoPluginExtension(@NotNull final Project project) {
        ObjectFactory factory = project.getObjects();
        this.cargoProject = factory.newInstance(CargoProjectExtension.class);
        this.environmentVariables = factory.mapProperty(String.class, String.class);
        this.executable = factory.property(String.class);
        this.sourceFolder = factory.directoryProperty();
        this.workingFolder = factory.directoryProperty();
        this.releaseMode = factory.property(Boolean.class);

        this.sourceFolder.set(new File(project.getProjectDir(), "src" + File.separator + "main" + File.separator + "rust"));
        this.workingFolder.set(new File(project.getBuildDir(), "cargo"));
        this.executable.set("cargo");
        this.releaseMode.set(false);
        this.environmentVariables.set(new HashMap<>());
    }

    @SuppressWarnings("unchecked")
    public void library(@NotNull final Map<String, Object> parameters) {
        if (parameters.containsKey("name") && parameters.containsKey("version")) {
            this.cargoDependencies.add(new RemoteCargoDependency((String) parameters.get("name"), (String) parameters.get("version"),
                    (List<String>) parameters.getOrDefault("features", new ArrayList<>())));
        } else if (parameters.containsKey("name") && parameters.containsKey("path")) {
            this.cargoDependencies.add(new LocalCargoDependency((String) parameters.get("name"), (String) parameters.get("path"),
                    (List<String>) parameters.getOrDefault("features", new ArrayList<>())));
        } else if (parameters.containsKey("name") && parameters.containsKey("git")) {
            this.cargoDependencies.add(new GitCargoDependency((String) parameters.get("name"), (String) parameters.get("git"),
                    (List<String>) parameters.getOrDefault("features", new ArrayList<>())));
        }
        throw new GradleException("Invalid dependency specification " + parameters);
    }

    public void project(@NotNull final Action<CargoProjectExtension> action) {
        action.execute(this.cargoProject);
    }

    public @NotNull CargoProjectExtension getCargoProjectExtension() {
        return this.cargoProject;
    }

    public @NotNull MapProperty<String, String> getEnvironmentVariables() {
        return this.environmentVariables;
    }

    public @NotNull Property<String> getExecutable() {
        return this.executable;
    }

    public @NotNull DirectoryProperty getSourceFolder() {
        return this.sourceFolder;
    }

    public @NotNull DirectoryProperty getWorkingFolder() {
        return this.workingFolder;
    }

    public @NotNull Property<Boolean> getReleaseMode() {
        return this.releaseMode;
    }


    public @NotNull List<ICargoDependency> getCargoDependencies() {
        return this.cargoDependencies;
    }

}
