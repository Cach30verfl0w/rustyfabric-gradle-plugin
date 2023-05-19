package de.cacheoverflow.rustyfabric.plugin.cargo.dependency;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GitCargoDependency implements ICargoDependency {

    private final String name;
    private final String git;
    private final List<String> features;

    public GitCargoDependency(@NotNull final String name, @NotNull final String git, @NotNull final List<String> features) {
        this.name = name;
        this.git = git;
        this.features = features;
    }

    @Override
    public @NotNull String buildCargoDependencyString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[dependencies.").append(this.name).append("]\n");
        stringBuilder.append("git = \"").append(this.git).append("\"\n");

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
