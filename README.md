# RustyFabric Gradle Plugin
This is a Gradle Plugin that allows the user to develop and execute Fabric modifications with the Rust and Java programming languages with the WebAssembly runtime [Wasmtime](https://wasmtime.dev/). **This is a highly experimental project and not ready for production usage but I try to do my best.**

## Setup RustyFabric environment
You can just simply download the [fabric-example-mod](https://github.com/FabricMC/fabric-example-mod) for your version then you should add `rustyfabric` to your Gradle plugins. After this, you should add the wasmtime Java wrapper to your dependencies:
```groovy
dependencies {
	 implementation 'io.github.kawamuray.wasmtime:wasmtime-java:0.14.0'
}
```
After all these steps you create a `rust` folder in the `main` folder and you can start developing your modification.

## Features of the Plugin
This plugin provides the following tasks:
 - `cargoCleanup` - Cleanup the temporary build folder `build/cargo`
 - `cargoGenerate` - Generate a `Cargo.toml` and the wasm compile target definition by the definitions and dependencies in your `build.gradle`
 - `configureFabricResources` - This task copies your WASM source into the resources of your Fabric modification and inserts the `WasmRuntimeInitializer` as an entrypoint
 - `wasmBuild` - Builds the Rust part of your project to a WASM file

## Usage in Java and Rust
At the moment, the Gradle plugin is too underdeveloped to use it in an environment. The instructions for the usage are coming soon.
