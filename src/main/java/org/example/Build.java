package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Build {

    public BuildResult runGradleBuild(File projectDir) {
        try {
            // Create a process builder for Gradle
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(projectDir);

            // Set the Gradle command
            processBuilder.command("./gradlew", "build");

            // Start the Gradle build process
            Process process = processBuilder.start();

            // Read the output of the Gradle build process
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            // Wait for the Gradle process to finish
            int exitCode = process.waitFor();

            // Return the build result
            return new BuildResult(exitCode == 0, output.toString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new BuildResult(false, "Error occurred during build: " + e.getMessage());
        }
    }

    static class BuildResult {
        private final boolean success;
        private final String output;

        public BuildResult(boolean success, String output) {
            this.success = success;
            this.output = output;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getOutput() {
            return output;
        }
    }
}
