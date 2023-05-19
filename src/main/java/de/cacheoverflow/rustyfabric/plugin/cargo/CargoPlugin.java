package de.cacheoverflow.rustyfabric.plugin.cargo;

import de.cacheoverflow.rustyfabric.plugin.cargo.extension.CargoPluginExtension;
import de.cacheoverflow.rustyfabric.plugin.cargo.tasks.*;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;
import org.jetbrains.annotations.NotNull;

public class CargoPlugin implements Plugin<Project> {

    private static final String TASK_GROUP = "cargo";

    @Override
    public void apply(@NotNull final Project project) {
        CargoPluginExtension extension = project.getExtensions().create("cargo", CargoPluginExtension.class, project);
        TaskContainer tasks = project.getTasks();
        this.addCargoTask(tasks, "cargoUpdate", CargoUpdateTask.class, extension);
        this.addCargoTask(tasks, "cargoBuild", CargoBuildTask.class, extension);
        this.addCargoTask(tasks, "cargoClean", CargoCleanTask.class, extension);
        this.addCargoTask(tasks, "cargoTest", CargoTestTask.class, extension);

        tasks.register("cargoInit", CargoInitTask.class, task -> {
            task.setGroup(CargoPlugin.TASK_GROUP);
            task.getSourceFolder().set(extension.getSourceFolder());
            task.getWorkingFolder().set(extension.getWorkingFolder());
        });
    }

    private void addCargoTask(@NotNull final TaskContainer tasks, @NotNull final String name,
                              @NotNull final Class<? extends AbstractCargoTask> clazz, @NotNull final CargoPluginExtension extension) {
        tasks.register(name, clazz, task -> {
            task.setGroup(CargoPlugin.TASK_GROUP);
            task.getEnvironmentVariables().set(extension.getEnvironmentVariables());
            task.getExecutable().set(extension.getExecutable());
            task.getSourceFolder().set(extension.getSourceFolder());
            task.getWorkingFolder().set(extension.getWorkingFolder());
            task.getReleaseMode().set(extension.getReleaseMode());
        });
    }

}
