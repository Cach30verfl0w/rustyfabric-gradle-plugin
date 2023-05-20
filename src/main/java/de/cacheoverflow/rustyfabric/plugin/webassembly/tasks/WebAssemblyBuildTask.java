package de.cacheoverflow.rustyfabric.plugin.webassembly.tasks;

import de.cacheoverflow.rustyfabric.plugin.utils.ProcessTask;

import java.util.List;

public class WebAssemblyBuildTask extends ProcessTask {

    public WebAssemblyBuildTask() {
        super("wasm-pack", List.of("build"));
    }

}
