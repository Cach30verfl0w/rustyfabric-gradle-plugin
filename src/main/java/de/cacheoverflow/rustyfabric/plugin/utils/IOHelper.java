package de.cacheoverflow.rustyfabric.plugin.utils;

import org.gradle.api.GradleException;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class IOHelper {

    private IOHelper() {
        throw new UnsupportedOperationException();
    }

    public static boolean deleteDirectory(@NotNull final File directory) {
        File[] content = directory.listFiles();
        if (content != null) {
            for (File file : content) {
                deleteDirectory(file);
            }
        }
        return directory.delete();
    }

    public static @NotNull String readFile(@NotNull final File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException ex) {
            throw new GradleException("Unable to read " + file.getName() + "in resources", ex);
        }
    }

    public static void writeFile(@NotNull final File file, @NotNull final String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
            writer.flush();
        } catch (IOException ex) {
            throw new GradleException("Unable to write " + file.getName() + "in resources", ex);
        }
    }

}
