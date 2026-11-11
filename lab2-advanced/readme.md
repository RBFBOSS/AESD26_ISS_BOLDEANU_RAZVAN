## Results summary

| Metric | Reflection API | ASM |
|---|---:|---:|
| Annotated components discovered (normal run) | 202 | 202 |
| Same component set | Yes | Yes |
| Average scan time (10 measured runs) | **5.292 ms** | 30.490 ms |
| Relative repeated-scan speed | **5.76× faster** | 5.76× slower |
| Generated classes loaded (separate JVM runs) | 1,000 | **0** |
| Triggers `StaticComponent` initialization | Yes | No |
| Finds `BrokenComponent` when its superclass is unavailable | No | Yes |
| Special annotated components found with missing dependency | 1 | 2 |

### Test 1 — Correctness and static initialization

```text
========== TEST 1: CORRECTNESS ==========
Static initialized after ASM: false
[STATIC INITIALIZER] StaticComponent initialized!
Static initialized after Reflection: true
ASM components: 202
Reflection components: 202
Only ASM: []
Only Reflection: []
Results identical: true
```

Both scanners found the same 202 components when dependencies were available. ASM did not initialize `StaticComponent`, while the Reflection scanner did. This behavior is specific to the Reflection implementation using `Class.forName(className)`, which initializes classes by default.

### Test 2 — Performance

The benchmark performed **5 warm-up runs** and **10 measured runs** per scanner.

```text
========== TEST 2: PERFORMANCE ==========

=== BENCHMARK ===
Warm-up completed.

Execution times (ms):
Reflection: [6.9037, 5.554399, 5.3326, 5.128099, 4.925, 4.9409, 4.9149, 4.6451, 4.959899, 5.612499]
ASM:        [36.4169, 37.2654, 31.646001, 31.7222, 29.629801, 29.9414, 27.3646, 26.880399, 26.9926, 27.0394]

Reflection average: 5.292 ms
ASM average: 30.490 ms
Reflection / ASM ratio: 0.17x
```

**Interpretation:** Reflection was approximately **5.76 times faster** in this repeated-scan benchmark. After warm-up, Reflection reused already-loaded classes, whereas ASM reread and reparsed `.class` files on each scan. These measurements do **not** prove Reflection is faster for first-time scanning in a fresh JVM, nor do they establish that ASM is always faster.

### Test 3 — JVM class loading

The program ran each scanner in a separate JVM using `-Xlog:class+load` and counted the **generated application classes** appearing in the class-loading logs.

```text
========== TEST 3: CLASS LOADING ==========
Generated classes loaded by ASM: 0
Generated classes loaded by Reflection: 1000
```

ASM inspected the generated classes without loading them. Reflection loaded all 1,000 generated classes. These counts exclude Java runtime and library classes.

### Test 4 — Missing dependency

The experiment used an isolated class loader to make `MissingDependency` unavailable while leaving `BrokenComponent.class` available.

```text
========== TEST 4: MISSING DEPENDENCY ==========
Simulating unavailable MissingDependency...
[Missing dependency test] com.example.lab2advanced.special.BrokenComponent failed: NoClassDefFoundError
[Missing dependency test] com.example.lab2advanced.special.MissingDependency failed: ClassNotFoundException
[STATIC INITIALIZER] StaticComponent initialized!
ASM finds BrokenComponent: true
Reflection finds BrokenComponent: false
ASM special components: 2
Reflection special components: 1
```

Reflection could not load `BrokenComponent` because its superclass was unavailable. ASM still detected its annotation directly from its `.class` file. The static initialization message in this test comes from Reflection scanning `StaticComponent` with the isolated class loader.

## Complete final console summary

```text
==========================================
 LAB 2 ADVANCED - COMPLETE EXPERIMENT
==========================================

========== TEST 1: CORRECTNESS ==========
Static initialized after ASM: false
[STATIC INITIALIZER] StaticComponent initialized!
Static initialized after Reflection: true
ASM components: 202
Reflection components: 202
Only ASM: []
Only Reflection: []
Results identical: true

========== TEST 2: PERFORMANCE ==========

=== BENCHMARK ===
Warm-up completed.

Execution times (ms):
Reflection: [6.9037, 5.554399, 5.3326, 5.128099, 4.925, 4.9409, 4.9149, 4.6451, 4.959899, 5.612499]
ASM:        [36.4169, 37.2654, 31.646001, 31.7222, 29.629801, 29.9414, 27.3646, 26.880399, 26.9926, 27.0394]

Reflection average: 5.292 ms
ASM average: 30.490 ms
Reflection / ASM ratio: 0.17x

========== TEST 3: CLASS LOADING ==========
Generated classes loaded by ASM: 0
Generated classes loaded by Reflection: 1000

========== TEST 4: MISSING DEPENDENCY ==========
Simulating unavailable MissingDependency...
[Missing dependency test] com.example.lab2advanced.special.BrokenComponent failed: NoClassDefFoundError
[Missing dependency test] com.example.lab2advanced.special.MissingDependency failed: ClassNotFoundException
[STATIC INITIALIZER] StaticComponent initialized!
ASM finds BrokenComponent: true
Reflection finds BrokenComponent: false
ASM special components: 2
Reflection special components: 1

==========================================
              FINAL RESULTS
==========================================
Components ASM: 202
Components Reflection: 202
Same components: true
Static initialization ASM: false
Static initialization Reflection: true
Classes loaded ASM: 0
Classes loaded Reflection: 1000
Missing dependency ASM: true
Missing dependency Reflection: false
==========================================
ALL EXPERIMENTS COMPLETED
==========================================

Process finished with exit code 0
```

## Conclusion

Both scanners discovered the same annotated components in the normal test. **ASM's demonstrated advantage is metadata inspection without loading or initializing the scanned application classes**, including successful annotation discovery when a required superclass was unavailable. **Reflection was faster in the measured repeated-scan benchmark**, so this experiment supports ASM's class-loading and robustness advantages, not a general claim of superior execution speed.
