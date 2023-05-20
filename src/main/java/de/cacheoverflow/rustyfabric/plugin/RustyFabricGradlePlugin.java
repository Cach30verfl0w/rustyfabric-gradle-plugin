package de.cacheoverflow.rustyfabric.plugin;

import de.cacheoverflow.rustyfabric.plugin.cargo.CargoPlugin;
import de.cacheoverflow.rustyfabric.plugin.fabricrust.FabricRustPlugin;
import de.cacheoverflow.rustyfabric.plugin.webassembly.WebAssemblyPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.TaskContainer;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RustyFabricGradlePlugin implements Plugin<Project> {

    @Override
    public void apply(@NotNull final Project project) {
        Logger logger = project.getLogger();

        logger.info("Loading internal Cargo Plugin of RustyFabric...");
        project.getPlugins().apply(CargoPlugin.class);
        project.getPlugins().apply(WebAssemblyPlugin.class);
        project.getPlugins().apply(FabricRustPlugin.class);

        TaskContainer tasks = project.getTasks();
        this.addDependToTask(tasks.getByName("classes"), new String[] { "configureFabricResources", "generateWebAssemblyRuntime" });
    }

    private void addDependToTask(@NotNull final Task task, @NotNull final String[] dependTasks) {
        Set<Object> depends = new HashSet<>(task.getDependsOn());
        depends.addAll(List.of(dependTasks));
        task.setDependsOn(depends);
    }

}
