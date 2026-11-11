
package com.example.lab2advanced;

import com.example.lab2advanced.annotation.MyComponent;
import com.example.lab2advanced.benchmark.BenchmarkRunner;
import com.example.lab2advanced.scanner.AsmScanner;
import com.example.lab2advanced.scanner.ReflectionScanner;
import com.example.lab2advanced.special.InitializationTracker;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {

    private static final String PACKAGE =
            "com.example.lab2advanced";

    private static final String BROKEN_COMPONENT =
            PACKAGE + ".special.BrokenComponent";

    private static final String MISSING_DEPENDENCY =
            PACKAGE + ".special.MissingDependency";

    private static final String GENERATED_PACKAGE =
            PACKAGE + ".generated.";

    private static final Pattern GENERATED_CLASS_PATTERN =
            Pattern.compile(
                    "com\\.example\\.lab2advanced\\.generated\\."
                            + "GeneratedClass\\d+"
            );

    private static Path classesDirectory;

    public static void main(String[] args) throws Exception {

        classesDirectory = Path.of(
                Main.class.getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI()
        );

        Path root = classesDirectory.resolve(
                "com/example/lab2advanced"
        );

        // Internal modes used by separate JVM processes.
        if (args.length > 0) {
            switch (args[0]) {
                case "internal-asm" -> {
                    AsmScanner.scan(root, PACKAGE);
                    return;
                }
                case "internal-reflection" -> {
                    ReflectionScanner.scan(root, PACKAGE);
                    return;
                }
                default -> throw new IllegalArgumentException(
                        "Unknown internal mode: " + args[0]
                );
            }
        }

        System.out.println(
                "=========================================="
        );
        System.out.println(
                " LAB 2 ADVANCED - COMPLETE EXPERIMENT"
        );
        System.out.println(
                "=========================================="
        );

        // TEST 1: Correctness and static initialization
        System.out.println(
                "\n========== TEST 1: CORRECTNESS =========="
        );

        Set<String> asm = AsmScanner.scan(root, PACKAGE);

        boolean initializedAfterAsm =
                InitializationTracker.isInitialized();

        System.out.println(
                "Static initialized after ASM: "
                        + initializedAfterAsm
        );

        Set<String> reflection =
                ReflectionScanner.scan(root, PACKAGE);

        boolean initializedAfterReflection =
                InitializationTracker.isInitialized();

        System.out.println(
                "Static initialized after Reflection: "
                        + initializedAfterReflection
        );

        Set<String> onlyAsm = new HashSet<>(asm);
        onlyAsm.removeAll(reflection);

        Set<String> onlyReflection =
                new HashSet<>(reflection);
        onlyReflection.removeAll(asm);

        boolean identical = asm.equals(reflection);

        System.out.println(
                "ASM components: " + asm.size()
        );
        System.out.println(
                "Reflection components: " + reflection.size()
        );
        System.out.println("Only ASM: " + onlyAsm);
        System.out.println(
                "Only Reflection: " + onlyReflection
        );
        System.out.println(
                "Results identical: " + identical
        );

        // TEST 2: Performance
        System.out.println(
                "\n========== TEST 2: PERFORMANCE =========="
        );

        BenchmarkRunner.run(root, PACKAGE);

        // TEST 3: JVM class loading
        System.out.println(
                "\n========== TEST 3: CLASS LOADING =========="
        );

        int asmLoaded = runAndCountLoadedClasses(
                "internal-asm"
        );

        int reflectionLoaded = runAndCountLoadedClasses(
                "internal-reflection"
        );

        System.out.println(
                "Generated classes loaded by ASM: "
                        + asmLoaded
        );

        System.out.println(
                "Generated classes loaded by Reflection: "
                        + reflectionLoaded
        );

        // TEST 4: Missing dependency
        System.out.println(
                "\n========== TEST 4: MISSING DEPENDENCY =========="
        );

        System.out.println(
                "Simulating unavailable MissingDependency..."
        );

        Path specialRoot = root.resolve("special");

        Set<String> asmSpecial = AsmScanner.scan(
                specialRoot,
                PACKAGE + ".special"
        );

        Set<String> reflectionSpecial =
                scanWithMissingDependency(specialRoot);

        boolean asmFindsBroken =
                asmSpecial.contains(BROKEN_COMPONENT);

        boolean reflectionFindsBroken =
                reflectionSpecial.contains(BROKEN_COMPONENT);

        System.out.println(
                "ASM finds BrokenComponent: "
                        + asmFindsBroken
        );

        System.out.println(
                "Reflection finds BrokenComponent: "
                        + reflectionFindsBroken
        );

        System.out.println(
                "ASM special components: "
                        + asmSpecial.size()
        );

        System.out.println(
                "Reflection special components: "
                        + reflectionSpecial.size()
        );

        // FINAL SUMMARY
        System.out.println(
                "\n=========================================="
        );
        System.out.println(
                "              FINAL RESULTS"
        );
        System.out.println(
                "=========================================="
        );

        System.out.println(
                "Components ASM: " + asm.size()
        );
        System.out.println(
                "Components Reflection: " + reflection.size()
        );
        System.out.println(
                "Same components: " + identical
        );

        System.out.println(
                "Static initialization ASM: "
                        + initializedAfterAsm
        );
        System.out.println(
                "Static initialization Reflection: "
                        + initializedAfterReflection
        );

        System.out.println(
                "Classes loaded ASM: " + asmLoaded
        );
        System.out.println(
                "Classes loaded Reflection: "
                        + reflectionLoaded
        );

        System.out.println(
                "Missing dependency ASM: "
                        + asmFindsBroken
        );
        System.out.println(
                "Missing dependency Reflection: "
                        + reflectionFindsBroken
        );

        System.out.println(
                "=========================================="
        );
        System.out.println(
                "ALL EXPERIMENTS COMPLETED"
        );
        System.out.println(
                "=========================================="
        );
    }

    // Runs a scanner in a separate JVM and counts
    // generated classes from JVM class-loading logs.
    private static int runAndCountLoadedClasses(
            String mode) throws Exception {

        Path logFile = Files.createTempFile(
                "lab2-" + mode + "-", ".log"
        );

        try {
            String javaExecutable = Path.of(
                    System.getProperty("java.home"),
                    "bin",
                    System.getProperty("os.name")
                            .toLowerCase()
                            .contains("win")
                            ? "java.exe" : "java"
            ).toString();

            ProcessBuilder builder = new ProcessBuilder(
                    javaExecutable,
                    "-Xlog:class+load=info:file="
                            + logFile.toAbsolutePath(),
                    "-cp",
                    System.getProperty("java.class.path"),
                    Main.class.getName(),
                    mode
            );

            builder.redirectErrorStream(true);

            Process process = builder.start();

            String output = new String(
                    process.getInputStream().readAllBytes()
            );

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new IllegalStateException(
                        "Child JVM failed:\n" + output
                );
            }

            Set<String> loadedClasses = new HashSet<>();

            for (String line : Files.readAllLines(logFile)) {

                if (!line.contains("[class,load]")) {
                    continue;
                }

                Matcher matcher =
                        GENERATED_CLASS_PATTERN.matcher(line);

                if (matcher.find()) {
                    loadedClasses.add(matcher.group());
                }
            }

            return loadedClasses.size();

        } finally {
            Files.deleteIfExists(logFile);
        }
    }

    // Simulates a missing dependency using an
    // isolated class loader that refuses to load it.
    private static Set<String> scanWithMissingDependency(
            Path specialRoot) throws Exception {

        Set<String> components = new HashSet<>();

        URL classesUrl = classesDirectory
                .toUri()
                .toURL();

        try (URLClassLoader loader = new URLClassLoader(
                new URL[]{classesUrl},
                ClassLoader.getPlatformClassLoader()
        ) {

            @Override
            protected Class<?> loadClass(
                    String name,
                    boolean resolve)
                    throws ClassNotFoundException {

                if (name.equals(MISSING_DEPENDENCY)) {
                    throw new ClassNotFoundException(
                            "Simulated missing dependency: "
                                    + name
                    );
                }

                return super.loadClass(name, resolve);
            }

        }) {

            try (Stream<Path> files =
                         Files.walk(specialRoot)) {

                for (Path file : files.filter(
                        p -> p.toString().endsWith(".class")
                ).toList()) {

                    String filename =
                            file.getFileName().toString();

                    String className =
                            PACKAGE + ".special."
                                    + filename.substring(
                                    0,
                                    filename.length() - 6
                            );

                    try {
                        Class<?> clazz = Class.forName(
                                className,
                                true,
                                loader
                        );

                        // Check annotation by name because
                        // this loader has its own annotation class.
                        boolean annotated = false;

                        for (var annotation :
                                clazz.getDeclaredAnnotations()) {

                            if (annotation.annotationType()
                                    .getName()
                                    .equals(
                                            MyComponent.class.getName()
                                    )) {

                                annotated = true;
                                break;
                            }
                        }

                        if (annotated) {
                            components.add(className);
                        }

                    } catch (ClassNotFoundException
                             | LinkageError e) {

                        System.out.println(
                                "[Missing dependency test] "
                                        + className
                                        + " failed: "
                                        + e.getClass().getSimpleName()
                        );
                    }
                }
            }
        }

        return components;
    }
}
