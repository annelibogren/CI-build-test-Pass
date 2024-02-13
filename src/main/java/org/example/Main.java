package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

public class Main {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public String getGreeting() {
        return "Hello World!";
    }

    public void runBuild() {
        try {
            // Instantiate GitHandler
            GitHandler gitHandler = new GitHandler();

            // Clone the repository if not already cloned
            if (!gitHandler.isRepoCloned()) {
                gitHandler.cloneRepo();
            }

            // Fetch the latest changes from the remote repository
            gitHandler.pull("main");

            // Checkout the desired branch
            gitHandler.checkout("main");

            // Get the local repository directory
            File projectDir = gitHandler.getLocalRepoDirFile();

            // Create an instance of Build
            Build build = new Build();

            // Run the build process
            Build.BuildResult buildResult = build.runGradleBuild(projectDir);

            // Log the build result
            if (buildResult.isSuccess()) {
                logger.info("Build successful");
            } else {
                logger.error("Build failed: {}", buildResult.getOutput());
            }
        } catch (Exception e) {
            logger.error("Error occurred during build: {}", e.getMessage());
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        logger.info(main.getGreeting());
        main.runBuild();
    }
}