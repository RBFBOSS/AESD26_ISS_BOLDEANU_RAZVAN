package com.example.lab2advanced.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClassGenerator {

    public static final int TOTAL_CLASSES = 1000;

    public static void main(String[] args) throws IOException {

        Path directory = Path.of(
                "src/main/java/com/example/lab2advanced/generated"
        );

        Files.createDirectories(directory);

        for (int i = 0; i < TOTAL_CLASSES; i++) {

            String className = "GeneratedClass" + i;

            boolean annotated = i % 5 == 0;

            String source =
                    "package com.example.lab2advanced.generated;\n\n"
                            + (annotated
                            ? "import com.example.lab2advanced.annotation.MyComponent;\n\n"
                            + "@MyComponent\n"
                            : "")
                            + "public class " + className + " {\n"
                            + "    public int getNumber() {\n"
                            + "        return " + i + ";\n"
                            + "    }\n"
                            + "}\n";

            Files.writeString(
                    directory.resolve(className + ".java"),
                    source
            );
        }

        System.out.println("Generated " + TOTAL_CLASSES + " classes.");
        System.out.println("Annotated classes: "
                + TOTAL_CLASSES / 5);
    }
}
