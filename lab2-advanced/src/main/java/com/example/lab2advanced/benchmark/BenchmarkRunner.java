package com.example.lab2advanced.benchmark;

import com.example.lab2advanced.scanner.AsmScanner;
import com.example.lab2advanced.scanner.ReflectionScanner;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;

public class BenchmarkRunner {

    private static final int WARMUP_RUNS = 5;
    private static final int MEASURED_RUNS = 10;

    public static void run(Path root, String packageName)
            throws Exception {

        System.out.println("\n=== BENCHMARK ===");

        // Warm-up
        for (int i = 0; i < WARMUP_RUNS; i++) {
            AsmScanner.scan(root, packageName);
            ReflectionScanner.scan(root, packageName);
        }

        System.out.println("Warm-up completed.");

        long[] reflectionTimes = new long[MEASURED_RUNS];
        long[] asmTimes = new long[MEASURED_RUNS];

        for (int i = 0; i < MEASURED_RUNS; i++) {

            if (i % 2 == 0) {
                reflectionTimes[i] = measureReflection(
                        root, packageName);
                asmTimes[i] = measureAsm(root, packageName);
            } else {
                asmTimes[i] = measureAsm(root, packageName);
                reflectionTimes[i] = measureReflection(
                        root, packageName);
            }
        }

        System.out.println("\nExecution times (ms):");
        System.out.println("Reflection: "
                + Arrays.toString(toMilliseconds(reflectionTimes)));
        System.out.println("ASM:        "
                + Arrays.toString(toMilliseconds(asmTimes)));

        double reflectionAverage = average(reflectionTimes);
        double asmAverage = average(asmTimes);

        System.out.printf(
                "%nReflection average: %.3f ms%n",
                reflectionAverage
        );

        System.out.printf(
                "ASM average: %.3f ms%n",
                asmAverage
        );

        System.out.printf(
                "Reflection / ASM ratio: %.2fx%n",
                reflectionAverage / asmAverage
        );
    }

    private static long measureReflection(
            Path root, String packageName) throws Exception {

        long start = System.nanoTime();

        Set<String> result = ReflectionScanner.scan(
                root, packageName);

        long elapsed = System.nanoTime() - start;

        if (result.isEmpty()) {
            throw new IllegalStateException(
                    "Reflection found no components.");
        }

        return elapsed;
    }

    private static long measureAsm(
            Path root, String packageName) throws Exception {

        long start = System.nanoTime();

        Set<String> result = AsmScanner.scan(
                root, packageName);

        long elapsed = System.nanoTime() - start;

        if (result.isEmpty()) {
            throw new IllegalStateException(
                    "ASM found no components.");
        }

        return elapsed;
    }

    private static double average(long[] times) {
        return Arrays.stream(times)
                .average()
                .orElse(0.0) / 1_000_000.0;
    }

    private static double[] toMilliseconds(long[] times) {
        return Arrays.stream(times)
                .mapToDouble(t -> t / 1_000_000.0)
                .toArray();
    }
}
