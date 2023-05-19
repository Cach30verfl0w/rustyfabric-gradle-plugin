package de.cacheoverflow.rustyfabric.plugin.cargo.dependency;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RemoteCargoDependency implements ICargoDependency {

    private final String name;
    private final String version;
    private final List<String> features;

    public RemoteCargoDependency(@NotNull final String name, @NotNull final String version, @NotNull final List<String> features) {
        this.name = name;
        this.version = version;
        this.features = features;
    }

    @Override
    public @NotNull String buildCargoDependencyString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[dependencies.").append(this.name).append("]\n");
        stringBuilder.append("version = \"").append(this.version).append("\"\n");

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
