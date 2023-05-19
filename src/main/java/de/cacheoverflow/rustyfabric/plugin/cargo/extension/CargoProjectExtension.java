package de.cacheoverflow.rustyfabric.plugin.cargo.extension;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class CargoProjectExtension {

    private final Property<String> projectName;
    private final Property<String> version;

    @Inject
    public CargoProjectExtension(@NotNull final ObjectFactory factory) {
        this.projectName = factory.property(String.class);
        this.version = factory.property(String.class);
    }

    public @NotNull Property<String> getProjectName() {
        return this.projectName;
    }

    public @NotNull Property<String> getVersion() {
        return this.version;
    }

}
