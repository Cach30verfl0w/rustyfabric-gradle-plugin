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

## Usage in Java and Rust
At the moment, the Gradle plugin is too underdeveloped to use it in an environment. The instructions for the usage are coming soon.
