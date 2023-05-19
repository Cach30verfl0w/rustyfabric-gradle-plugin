package de.cacheoverflow.rustyfabric.plugin.cargo.dependency;

import org.jetbrains.annotations.NotNull;

public interface ICargoDependency {

    @NotNull String buildCargoDependencyString();

}
