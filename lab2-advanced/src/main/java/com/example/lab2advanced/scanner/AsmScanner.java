package com.example.lab2advanced.scanner;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.example.lab2advanced.annotation.MyComponent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class AsmScanner {

    public static Set<String> scan(
            Path root, String packageName) throws IOException {

        Set<String> components = new HashSet<>();

        String annotationDescriptor =
                Type.getDescriptor(MyComponent.class);

        try (Stream<Path> files = Files.walk(root)) {

            for (Path file : files.filter(
                    p -> p.toString().endsWith(".class")
            ).toList()) {

                byte[] bytecode = Files.readAllBytes(file);

                ClassReader reader = new ClassReader(bytecode);

                reader.accept(
                        new ClassVisitor(Opcodes.ASM9) {

                            private String className;

                            @Override
                            public void visit(
                                    int version,
                                    int access,
                                    String name,
                                    String signature,
                                    String superName,
                                    String[] interfaces) {

                                className = name.replace('/', '.');
                            }

                            @Override
                            public AnnotationVisitor visitAnnotation(
                                    String descriptor,
                                    boolean visible) {

                                if (descriptor.equals(
                                        annotationDescriptor)) {
                                    components.add(className);
                                }

                                return null;
                            }
                        },
                        ClassReader.SKIP_CODE
                                | ClassReader.SKIP_DEBUG
                                | ClassReader.SKIP_FRAMES
                );
            }
        }

        return components;
    }
}
