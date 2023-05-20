package de.cacheoverflow.rustyfabric.plugin.cargo.extension;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class CargoProjectExtension {

    private final Property<String> projectName;
    private final Property<String> version;
    private final Property<String> compileTarget;

    @Inject
    public CargoProjectExtension(@NotNull final ObjectFactory factory) {
        this.projectName = factory.property(String.class);
        this.version = factory.property(String.class);
        this.compileTarget = factory.property(String.class);
        this.compileTarget.set("default");
    }

    public @NotNull Property<String> getCompileTarget() {
        return this.compileTarget;
    }

    public @NotNull Property<String> getProjectName() {
        return this.projectName;
    }

    public @NotNull Property<String> getVersion() {
        return this.version;
    }

}
