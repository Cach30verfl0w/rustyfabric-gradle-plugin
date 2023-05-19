package de.cacheoverflow.rustyfabric.plugin.cargo.dependency;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LocalCargoDependency implements ICargoDependency {

    private final String name;
    private final String path;
    private final List<String> features;

    public LocalCargoDependency(@NotNull final String name, @NotNull final String path, @NotNull final List<String> features) {
        this.name = name;
        this.path = path;
        this.features = features;
    }

    @Override
    public @NotNull String buildCargoDependencyString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[dependencies.").append(this.name).append("]\n");
        stringBuilder.append("path = \"").append(this.path).append("\"\n");

        if (features.size() > 0) {
            stringBuilder.append("features = [");
            for (String feature : this.features) {
                stringBuilder.append(", \"").append(feature).append("\"");
            }
            stringBuilder.append("]\n");
        }

        return stringBuilder.toString().replaceFirst(", ", "");
    }
}
