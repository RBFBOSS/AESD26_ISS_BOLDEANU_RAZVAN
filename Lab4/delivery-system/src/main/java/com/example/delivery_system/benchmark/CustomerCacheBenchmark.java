package com.example.delivery_system.benchmark;
import com.example.delivery_system.DeliverySystemApplication;
import com.example.delivery_system.service.CustomerReadService;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import java.util.concurrent.TimeUnit;
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations=5, time=2)
@Measurement(iterations=8, time=2)
@Fork(value=2)
@State(Scope.Benchmark)
public class CustomerCacheBenchmark {
    private ConfigurableApplicationContext context;
    private CustomerReadService service;
    private long id;
    @Setup(Level.Trial)
    public void setup() {
        context=SpringApplication.run(DeliverySystemApplication.class,
            "--spring.profiles.active=dev", "--spring.main.web-application-type=none");
        service=context.getBean(CustomerReadService.class);
        id=Long.parseLong(System.getProperty("benchmark.customerId","1"));
        if(service.uncached(id)==null) throw new IllegalStateException("Missing benchmark customer ID: "+id);
        service.cached(id); // prime cache outside measurement
    }
    @TearDown(Level.Trial)
    public void tearDown() { context.close(); }
    @Benchmark public void uncachedRead(Blackhole bh) { bh.consume(service.uncached(id)); }
    @Benchmark public void cachedRead(Blackhole bh) { bh.consume(service.cached(id)); }
}
