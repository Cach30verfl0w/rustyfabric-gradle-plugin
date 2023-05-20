package de.cacheoverflow.rustyfabric.plugin.cargo.extension;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class CargoLibraryExtension {

    private final Property<String> type;

    @Inject
    public CargoLibraryExtension(@NotNull final ObjectFactory factory) {
        this.type = factory.property(String.class);
        this.type.set("none");
    }

    public @NotNull Property<String> getType() {
        return this.type;
    }

}
