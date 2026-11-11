package com.example.delivery_system.config;
import com.example.delivery_system.entity.Customer;
import com.example.delivery_system.entity.Delivery;
import jakarta.persistence.EntityManager;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.time.LocalDateTime;
@Component
@Profile("seed")
public class FakeDataSeeder implements CommandLineRunner {
    private final EntityManager em;
    private final TransactionTemplate tx;
    @Value("${app.seed.customers:1000}") private int customerCount;
    @Value("${app.seed.deliveries:10000}") private int deliveryCount;
    public FakeDataSeeder(EntityManager em, PlatformTransactionManager manager) {
        this.em=em; this.tx=new TransactionTemplate(manager);
    }
    @Override public void run(String... args) {
        if(customerCount<1 || deliveryCount<0 || customerCount>100000 || deliveryCount>1000000)
            throw new IllegalArgumentException("Invalid seed size");
        Faker faker=new Faker(new Locale("en"));
        Random random=new Random(42);
        String runId=UUID.randomUUID().toString().substring(0,8);
        List<Long> ids=new ArrayList<>();
        for(int start=0;start<customerCount;start+=250) {
            int begin=start, end=Math.min(customerCount,start+250);
            tx.executeWithoutResult(status -> {
                for(int i=begin;i<end;i++) {
                    Customer c=new Customer(faker.name().fullName(),"fake-"+runId+"-"+i+"@example.invalid",
                        faker.phoneNumber().subscriberNumber(10),faker.address().streetAddress()+", "+faker.address().city(),
                        "47."+(1000+i%8000)+",27."+(1000+i%8000));
                    em.persist(c); em.flush(); ids.add(c.getId());
                }
                em.clear();
            });
        }
        for(int start=0;start<deliveryCount;start+=500) {
            int begin=start,end=Math.min(deliveryCount,start+500);
            tx.executeWithoutResult(status -> {
                for(int i=begin;i<end;i++) {
                    Customer c=em.getReference(Customer.class,ids.get(random.nextInt(ids.size())));
                    Delivery d=new Delivery(c,1+random.nextInt(1000),faker.lorem().sentence());
                    d.setTimestamp(LocalDateTime.now().minusDays(random.nextInt(365)));
                    em.persist(d);
                }
                em.flush();em.clear();
            });
        }
        System.out.printf("DataFaker inserted %d customers and %d deliveries%n",customerCount,deliveryCount);
    }
}
