package com.example.lab2advanced.scanner;

import com.example.lab2advanced.annotation.MyComponent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class ReflectionScanner {

    public static Set<String> scan(
            Path root, String packageName) throws IOException {

        Set<String> components = new HashSet<>();

        try (Stream<Path> files = Files.walk(root)) {

            for (Path file : files.filter(
                    p -> p.toString().endsWith(".class")
            ).toList()) {

                String relative = root.relativize(file).toString();

                String className = packageName + "."
                        + relative.substring(
                                0, relative.length() - 6
                        ).replace('/', '.')
                        .replace('\\', '.');

                try {
                    // Loads AND initializes the class.
                    Class<?> clazz = Class.forName(className);

                    if (clazz.isAnnotationPresent(MyComponent.class)) {
                        components.add(className);
                    }

                } catch (ClassNotFoundException
                         | LinkageError e) {

                    System.out.println(
                            "[Reflection] Failed: " + className
                                    + " -> " + e.getClass().getSimpleName()
                    );
                }
            }
        }

        return components;
    }
}
