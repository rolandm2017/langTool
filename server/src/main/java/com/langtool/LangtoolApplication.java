package com.langtool;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Stream;

@SpringBootApplication
public class LangtoolApplication {

    // ANSI escape code for yellow text
    public static final String ANSI_YELLOW = "\u001B[33m";
    // ANSI escape code to reset text color
    public static final String ANSI_RESET = "\u001B[0m";


	public static void main(String[] args) {
		boolean resetAllTables = false;
        
        if (resetAllTables) {
            System.out.println("## ## ## ## ## ## ## ## ## ## ## ## ##");
            System.out.println("#/ // // // // // // // // // // // /#");
            System.out.println("#/ // // Resetting all tables // // /#");
            System.out.println("#/ // // // // // // // // // // // /#");
            System.out.println("## ## ## ## ## ## ## ## ## ## ## ## ##");
            ConfigurableApplicationContext context = SpringApplication.run(LangtoolApplication.class, args);
            // Uncomment the following line to reset tables on application start
            context.getBean(TableResetter.class).resetTables();
            // Uncomment the following line to reset uploads on application start
            context.getBean(UploadFolderResetter.class).resetUploadFolders();
        } else {
            SpringApplication.run(LangtoolApplication.class, args);
        }
    }

    /*
     * Cleanup utils
     */

    @Component
    public static class TableResetter {

        @Autowired
        private EntityManager entityManager;

        @Transactional
        public void resetTables() {
            System.out.println("Resetting tables...");

            // Disable all triggers
            entityManager.createNativeQuery("SET session_replication_role = 'replica';").executeUpdate();

            List<String> tablesToReset = Arrays.asList(
                "collections",
                "photos",
                "text_groups",
                "collection_photos"
                // Add more table names as needed
            );

            for (String tableName : tablesToReset) {
                System.out.println("Truncating table: " + tableName);
                entityManager.createNativeQuery("TRUNCATE TABLE " + tableName + " CASCADE").executeUpdate();
                // write your code here GPT!
            }

            // Reset sequences
            resetSequences();

            // Re-enable all triggers
            entityManager.createNativeQuery("SET session_replication_role = 'origin';").executeUpdate();

            String message = "All specified tables have been reset.";
            System.out.println(ANSI_YELLOW + message + ANSI_RESET);
        }

        private void resetSequences() {
            System.out.println("Resetting sequences...");
            // Fetch and reset all sequences in the public schema
            List<String> sequences = entityManager.createNativeQuery(
                "SELECT sequence_name FROM information_schema.sequences WHERE sequence_schema = 'public'")
                .getResultList();
        
            for (Object seq : sequences) {
                String sequenceName = (String) seq;
                System.out.println("Resetting sequence: " + sequenceName);
                entityManager.createNativeQuery("ALTER SEQUENCE " + sequenceName + " RESTART WITH 1").executeUpdate();
            }
        }
        
    }

    @Component
    public static class UploadFolderResetter {

        @Value("${file.upload-dir}")
        private String BASE_UPLOAD_DIR;

        @Value("${file.temp-dir}")
        private String TEMP_DIR;

        public void resetUploadFolders() {
            System.out.println("Resetting upload folders...");

            deleteDirectoryContents(BASE_UPLOAD_DIR);
            deleteDirectoryContents(TEMP_DIR);

            String message = "Upload folders have been reset.";
            System.out.println(ANSI_YELLOW + message + ANSI_RESET);
        }

        private void deleteDirectoryContents(String directoryPath) {
            Path path = Paths.get(directoryPath);
            if (!Files.exists(path)) {
                System.out.println("Directory does not exist: " + directoryPath);
                return;
            }

            try (Stream<Path> walk = Files.walk(path)) {
                walk.sorted((a, b) -> b.toString().length() - a.toString().length()) // Reverse order to delete files before directories
                    .forEach(this::deleteFileOrDirectory);
            } catch (IOException e) {
                System.err.println("Error while deleting directory contents: " + e.getMessage());
            }
        }

        private void deleteFileOrDirectory(Path path) {
            try {
                if (!path.equals(Paths.get(BASE_UPLOAD_DIR)) && !path.equals(Paths.get(TEMP_DIR))) {
                    Files.deleteIfExists(path);
                    System.out.println("Deleted: " + path);
                }
            } catch (IOException e) {
                System.err.println("Failed to delete: " + path + ". Error: " + e.getMessage());
            }
        }
    }
}
