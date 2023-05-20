package de.cacheoverflow.rustyfabric.plugin.fabricrust.tasks.codegen;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractCodeGenerationTask extends DefaultTask implements Opcodes {

    private final DirectoryProperty workingFolder;

    public AbstractCodeGenerationTask() {
        ObjectFactory factory = this.getProject().getObjects();
        this.workingFolder = factory.directoryProperty();
        this.workingFolder.set(new File(this.getProject().getBuildDir(), "classes/java/main"));
    }

    protected abstract @NotNull List<ClassNode> generateClasses();

    @TaskAction
    public void performTask() {
        File workingFolder = this.workingFolder.getAsFile().get();
        if (!workingFolder.exists())
            throw new GradleException("Unable to generate code => Working folder is not existing!");

        List<ClassNode> classes = this.generateClasses();
        for (ClassNode classNode : classes) {
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(writer);
            try {
                File file = new File(workingFolder, classNode.name + ".class");
                file.getParentFile().mkdirs();
                Files.write(file.toPath(), writer.toByteArray());
            } catch (IOException ex) {
                throw new GradleException("Unable to write class to file", ex);
            }
        }
    }

    protected @NotNull ClassNode generateClass(@NotNull final String name, @NotNull final List<String> interfaces) {
        ClassNode classNode = new ClassNode();
        classNode.name = name;
        classNode.access = ACC_PUBLIC;
        classNode.interfaces = interfaces;
        classNode.version = V17;
        classNode.fields = new ArrayList<>();

        MethodVisitor constructorVisitor = classNode.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        constructorVisitor.visitCode();
        constructorVisitor.visitVarInsn(ALOAD, 0);
        constructorVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        constructorVisitor.visitInsn(RETURN);
        constructorVisitor.visitMaxs(1, 1);
        constructorVisitor.visitEnd();
        return classNode;
    }

    @OutputDirectory
    public @NotNull DirectoryProperty getWorkingFolder() {
        return this.workingFolder;
    }

}
