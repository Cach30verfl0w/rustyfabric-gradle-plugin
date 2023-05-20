package de.cacheoverflow.rustyfabric.plugin;

import de.cacheoverflow.rustyfabric.plugin.cargo.CargoPlugin;
import de.cacheoverflow.rustyfabric.plugin.fabricrust.FabricRustPlugin;
import de.cacheoverflow.rustyfabric.plugin.webassembly.WebAssemblyPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.jetbrains.annotations.NotNull;

public class RustyFabricGradlePlugin implements Plugin<Project> {

    @Override
    public void apply(@NotNull final Project project) {
        Logger logger = project.getLogger();

        logger.info("Loading internal Cargo Plugin of RustyFabric...");
        project.getPlugins().apply(CargoPlugin.class);
        project.getPlugins().apply(WebAssemblyPlugin.class);
        project.getPlugins().apply(FabricRustPlugin.class);
    }

}
