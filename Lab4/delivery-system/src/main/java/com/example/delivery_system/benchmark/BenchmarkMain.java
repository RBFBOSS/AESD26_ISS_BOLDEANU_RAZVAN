package com.example.delivery_system.benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
public class BenchmarkMain {
    public static void main(String[] args) throws Exception {
        Options options=new OptionsBuilder().include(CustomerCacheBenchmark.class.getSimpleName()).build();
        new Runner(options).run();
    }
}
