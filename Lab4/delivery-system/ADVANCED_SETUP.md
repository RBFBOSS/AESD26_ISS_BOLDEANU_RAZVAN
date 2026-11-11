# Advanced coursework additions

## Requirements covered
- **DataFaker:** `FakeDataSeeder`, enabled ONLY by the `seed` Spring profile. Generates configurable customers and deliveries in batches; fake customers intentionally have no login accounts.
- **Criteria API:** `GET /api/v2/deliveries/search?customerId=1&area=Iasi&minQuantity=50&date=2026-11-11&page=0&size=20`. All filters optional; `minQuantity` means strictly greater than the supplied number. Also available at `/api/deliveries/search`.
- **Spring Cache:** `GET /api/v2/customers/{id}` uses a 120-second Caffeine cache; customer updates/deletes invalidate entries. `CustomerReadService.uncached` is the comparison baseline.
- **JMH:** `CustomerCacheBenchmark` includes five warm-up iterations, eight measurement iterations, two forked JVMs, and a Blackhole. It compares the same ID using cached and uncached service calls. **No speedup is claimed until measured on your machine.**
- **Flyway:** V1 core schema, V2 security tables/columns, V3 indexes. SQL initialization (`schema.sql`) has been removed. Flyway baselines existing nonempty databases at version **0** and runs all migrations; back up the database before first use.
- **HATEOAS:** `GET /api/v2/customers` and `GET /api/v2/customers/{id}` include `self`, `customers`, and `deliveries` links.
- **URI versioning:** existing `/api/customers` and `/api/deliveries` remain as compatibility aliases. `/api/v1/customers` is the legacy JSON representation; `/api/v2/customers` returns immutable customer DTOs with HAL links. `/api/v1/deliveries` and `/api/v2/deliveries` currently share the same CRUD representation.

## Setup
1. **Back up PostgreSQL.** Existing schemas can differ; review V1/V2 before running Flyway. In particular, if your old schema has incompatible column types or names, reconcile it first. Hibernate stays on `ddl-auto=validate`.
2. Ensure PostgreSQL `delivery_db` exists and set `DB_PASSWORD` and `JWT_SECRET` as before. Run `mvn clean compile` and then `mvn spring-boot:run`.
3. To populate 1,000 customers and 10,000 deliveries once: `mvn spring-boot:run -Dspring-boot.run.profiles=dev,seed`. **Do not leave the `seed` profile enabled**; each launch inserts another batch.
4. Set `app.seed.customers` and `app.seed.deliveries` in application properties or environment to change sizes.
5. Example: `curl "http://localhost:8080/api/v2/deliveries/search?area=Iasi&minQuantity=100&size=20"`.
6. Example: `curl -H "Accept: application/hal+json" http://localhost:8080/api/v2/customers/1`.
7. Benchmark: `mvn clean package -DskipTests` then run `com.example.delivery_system.benchmark.BenchmarkMain` with the project's dependencies on the classpath (IntelliJ Run main class is easiest). Set JVM option `-Dbenchmark.customerId=1` if needed. Supply DB and JWT environment variables. Benchmark uses dev profile and requires a populated database. Note that JMH forks JVMs; it does **not** measure controller HTTP latency or production load. Caffeine's 120-second TTL is longer than individual measurement iterations, but very long full runs may see expirations; adjust TTL for stable measurements.

## API migration strategy
1. **V1:** Keep `/api/v1/customers` available for clients expecting the original plain customer JSON. Existing `/api/customers` remains as a compatibility alias.
2. **V2:** Move read clients to `/api/v2/customers`, where responses use HAL `_links`. Follow the `deliveries` relation rather than building URLs manually. Write operations remain at `/api/v1/customers` or `/api/customers` for now.
3. **Deprecation:** Announce a removal date and include deprecation/sunset headers when a deadline is decided; track v1 usage before retiring it. Do not remove v1 until consumers have migrated.
4. **Security:** Public GET remains public, while write endpoints remain protected. No version may bypass authorization. Keycloak must be configured separately.

## Notes
- Flyway baseline-on-migrate at version 0 is for coursework migration of existing databases. Review in production; baseline can hide unexpected unmanaged schemas.
- HATEOAS `deliveries` relation points to the filtered search endpoint. Pagination is used for large searches; `/api/v2/customers` currently returns all customers and should be paginated for very large production datasets.
- Fake data can be large; use it on a development database, not production.
- This project has not been runtime-tested against your PostgreSQL instance or Keycloak realm.
