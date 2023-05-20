package de.cacheoverflow.rustyfabric.plugin.fabricrust;

import de.cacheoverflow.rustyfabric.plugin.fabricrust.tasks.AdjustResourcesTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FabricRustPlugin implements Plugin<Project> {

    private static final String TASK_GROUP = "fabricrust";

    @Override
    public void apply(@NotNull final Project project) {
        TaskContainer tasks = project.getTasks();
        tasks.register("adjustResources", AdjustResourcesTask.class, task -> {
            task.setGroup(FabricRustPlugin.TASK_GROUP);
            task.setDependsOn(List.of("wasmBuild", "processResources"));
        });
    }

}
