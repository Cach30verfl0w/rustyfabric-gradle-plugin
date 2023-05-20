package de.cacheoverflow.rustyfabric.plugin.fabricrust.tasks;

import com.google.gson.*;
import de.cacheoverflow.rustyfabric.plugin.IOHelper;
import de.cacheoverflow.rustyfabric.plugin.cargo.extension.CargoPluginExtension;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ConfigureFabricResourcesTask extends DefaultTask {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @TaskAction
    public void performTask() {
        CargoPluginExtension cargoExtension = this.getProject().getExtensions().getByType(CargoPluginExtension.class);

        this.getLogger().info("Modify Fabric resource => fabric.mod.json");
        File fabricModDefinitionResource = new File(this.getProject().getBuildDir(), "resources/main/fabric.mod.json");
        if (!fabricModDefinitionResource.exists())
            throw new GradleException("Unable to modify Fabric resources => No fabric.mod.json found!");

        // Find main node in Json file
        JsonElement element = JsonParser.parseString(IOHelper.readFile(fabricModDefinitionResource));
        if (!(element instanceof JsonObject object))
            throw new GradleException("Unable to modify Fabric resources => Illegal formatted fabric.mod.json found!");

        JsonObject entrypointsNode = object.getAsJsonObject("entrypoints");
        if (entrypointsNode == null)
            throw new GradleException("Unable to modify Fabric resources => No 'entrypoints' node found!");

        JsonArray mainNode = entrypointsNode.getAsJsonArray("main");
        if (mainNode == null) {
            mainNode = new JsonArray();
            entrypointsNode.add("main", mainNode);
        }

        // Insert WASM runtime initializer into modification
        mainNode.add("de.cacheoverflow.rustcraft.wasmruntime.WasmRuntimeInitializer");
        mainNode.remove(JsonNull.INSTANCE);
        IOHelper.writeFile(fabricModDefinitionResource, GSON.toJson(object));

        this.getLogger().info("Include Mod resource => Rust Source Code as Web Assembly File");
        File file = new File(cargoExtension.getWorkingFolder().getAsFile().get(),
                String.format("target/wasm32-unknown-unknown/debug/%s.wasm", cargoExtension.getCargoProjectExtension().getProjectName()
                        .getOrElse(this.getProject().getName())
                        .replace("-", "_")));
        if (!file.exists())
            throw new GradleException("Unable to include mod resource => Mod resource not found!");

        File resourcesFile = new File(this.getProject().getBuildDir(), "resources/main/source.wasm");
        try {
            Files.copy(file.toPath(), resourcesFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new GradleException("Unable to include mod resource", ex);
        }
    }

}
