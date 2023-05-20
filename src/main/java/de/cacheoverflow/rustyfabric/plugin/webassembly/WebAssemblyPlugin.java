package de.cacheoverflow.rustyfabric.plugin.webassembly;

import de.cacheoverflow.rustyfabric.plugin.cargo.extension.CargoPluginExtension;
import de.cacheoverflow.rustyfabric.plugin.utils.ProcessTask;
import de.cacheoverflow.rustyfabric.plugin.webassembly.tasks.PrepareWebAssemblyTask;
import de.cacheoverflow.rustyfabric.plugin.webassembly.tasks.WebAssemblyBuildTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WebAssemblyPlugin implements Plugin<Project> {

    private static final String TASK_GROUP = "webassembly";

    @Override
    public void apply(@NotNull final Project project) {
        CargoPluginExtension extension = project.getExtensions().getByType(CargoPluginExtension.class);
        TaskContainer tasks = project.getTasks();
        this.addProcessTask(tasks, "wasmBuild", WebAssemblyBuildTask.class, extension, "cargoInit", "wasmPrepare");
        this.addProcessTask(tasks, "wasmPrepare", PrepareWebAssemblyTask.class, extension, "cargoInit");
    }

    private void addProcessTask(@NotNull final TaskContainer tasks, @NotNull final String name,
                                @NotNull final Class<? extends ProcessTask> clazz, @NotNull final CargoPluginExtension extension, @NotNull String... depends) {
        tasks.register(name, clazz, task -> {
            task.setGroup(WebAssemblyPlugin.TASK_GROUP);
            task.setDependsOn(List.of(depends));
            task.getEnvironmentVariables().set(extension.getEnvironmentVariables());
            task.getExecutable().set(extension.getExecutable());
            task.getSourceFolder().set(extension.getSourceFolder());
            task.getWorkingFolder().set(extension.getWorkingFolder());
            task.getReleaseMode().set(extension.getReleaseMode());
        });
    }

}
