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

import com.langtool.repository.CollectionRepository;
import com.langtool.repository.PhotoRepository;
import com.langtool.repository.TextGroupRepository;

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

	public static void main(String[] args) {
		SpringApplication.run(LangtoolApplication.class, args);
		// boolean cleanOutTablesAndFiles = true; // Set this to false if you don't want to clean out the database on startup

        // ConfigurableApplicationContext context = SpringApplication.run(LangtoolApplication.class, args);

        // if (cleanOutTablesAndFiles) {
        //     // Retrieve the DataCleaner bean and run it
        //     DataCleaner dataCleaner = context.getBean(DataCleaner.class);
        //     dataCleaner.run();
        // }
    }
}

// @Component
// class DataCleaner implements CommandLineRunner {

//     @Autowired
//     private EntityManager entityManager;

//     @Autowired
//     private CollectionRepository collectionRepository;

//     @Autowired
//     private PhotoRepository photoRepository;

//     @Autowired
//     private TextGroupRepository textGroupRepository;

//     private List<JpaRepository<?, ?>> repositories;

// 	@Value("${file.upload-dir}")
//     private String BASE_UPLOAD_DIR;

//     @Value("${file.temp-dir}")
//     private String TEMP_DIR;

//     @Override
//     @Transactional
//     public void run(String... args) {
//         repositories = Arrays.asList(collectionRepository, photoRepository, textGroupRepository);
//         deleteAllFromRepositories(repositories);
//         deleteFilesInDirectories();
//     }

//     private void deleteAllFromRepositories(List<JpaRepository<?, ?>> repositories) {
//         for (JpaRepository<?, ?> repository : repositories) {
//             Class<?> entityClass = getEntityClass(repository);
//             if (entityClass == null) {
//                 System.out.println("Could not determine entity class for repository: " + repository.getClass().getSimpleName());
//                 continue;
//             }

//             Table tableAnnotation = entityClass.getAnnotation(Table.class);
//             String tableName = (tableAnnotation != null && !tableAnnotation.name().isEmpty()) 
//                 ? tableAnnotation.name() 
//                 : entityClass.getSimpleName();

//             entityManager.createNativeQuery("DELETE FROM " + tableName).executeUpdate();
//             System.out.println("Deleted all rows from table: " + tableName);
//         }
//     }

//     private Class<?> getEntityClass(JpaRepository<?, ?> repository) {
//         return repository.getClass().getInterfaces()[0].getGenericInterfaces()[0].getClass();
//     }

// 	private void deleteFilesInDirectories() {
// 		deleteFilesInDirectory(BASE_UPLOAD_DIR);
//         deleteFilesInDirectory(TEMP_DIR);
//     }

//     private void deleteFilesInDirectory(String directoryPath) {
//         Path directory = Paths.get(directoryPath);
//         if (!Files.exists(directory)) {
//             System.out.println("Directory does not exist: " + directoryPath);
//             return;
//         }

//         try (Stream<Path> files = Files.list(directory)) {
//             files.forEach(file -> {
//                 try {
//                     Files.delete(file);
//                     System.out.println("Deleted file: " + file);
//                 } catch (IOException e) {
//                     System.err.println("Failed to delete file: " + file);
//                     e.printStackTrace();
//                 }
//             });
//         } catch (IOException e) {
//             System.err.println("Error accessing directory: " + directoryPath);
//             e.printStackTrace();
//         }
//     }
// }