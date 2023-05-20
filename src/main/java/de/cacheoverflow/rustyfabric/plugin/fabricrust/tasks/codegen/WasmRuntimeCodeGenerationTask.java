package de.cacheoverflow.rustyfabric.plugin.fabricrust.tasks.codegen;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.Arrays;
import java.util.List;

public class WasmRuntimeCodeGenerationTask extends AbstractCodeGenerationTask {

    @Override
    protected @NotNull List<ClassNode> generateClasses() {
        ClassNode wasmRuntimeInitializerNode = this.generateClass("de/cacheoverflow/rustyfabric/wasmruntime/WasmRuntimeInitializer",
                List.of("net/fabricmc/api/ModInitializer"));

        // Static Fields
        FieldNode staticLoggerNode = new FieldNode(ACC_PUBLIC | ACC_STATIC | ACC_FINAL, "LOGGER",
                "Lorg/apache/logging/log4j/Logger;", null, null);
        FieldNode staticHasRunNode = new FieldNode(ACC_PRIVATE | ACC_STATIC, "hasRun", "Z", null, null);
        wasmRuntimeInitializerNode.fields.addAll(Arrays.asList(staticLoggerNode, staticHasRunNode));

        // Generate static field initializer (a.e. Logger)
        MethodVisitor visitor = wasmRuntimeInitializerNode.visitMethod(ACC_PUBLIC, "<clinit>", "()V", null, null);
        visitor.visitCode();
        visitor.visitLdcInsn("RustyFabric WebAssembly Runtime");
        visitor.visitMethodInsn(INVOKESTATIC, "org/apache/logging/log4j/LogManager", "getLogger", "(Ljava/lang/String;)Lorg/apache/logging/log4j/Logger;");
        visitor.visitFieldInsn(PUTSTATIC, "de/cacheoverflow/rustyfabric/wasmruntime/WasmRuntimeInitializer", "LOGGER", "Lorg/apache/logging/log4j/Logger;");
        visitor.visitEnd();

        return List.of(wasmRuntimeInitializerNode);
    }

}
