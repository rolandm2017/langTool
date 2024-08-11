package com.langtool.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

@Service
public class PhotoToTextFacade {
    
    private static final Logger logger = LoggerFactory.getLogger(PhotoToTextFacade.class);

    private final boolean useRealApi = false;

    @Value("${file.dummy-photos}")
    private String dummyPhotosPath;

    private final String[] expectedFileNames = getFileNamesFromLesChatsFolder();

    @Autowired
    GoogleCloudVisionApiService cloudVisionApi;

    @EventListener(ApplicationReadyEvent.class)
    public void logWarningOnStartup() {
        if (useRealApi) {
            String warningMessage = "\n" +
                "██╗    ██╗ █████╗ ██████╗ ███╗   ██╗██╗███╗   ██╗ ██████╗ ██╗\n" +
                "██║    ██║██╔══██╗██╔══██╗████╗  ██║██║████╗  ██║██╔════╝ ██║\n" +
                "██║ █╗ ██║███████║██████╔╝██╔██╗ ██║██║██╔██╗ ██║██║  ███╗██║\n" +
                "██║███╗██║██╔══██║██╔══██╗██║╚██╗██║██║██║╚██╗██║██║   ██║╚═╝\n" +
                "╚███╔███╔╝██║  ██║██║  ██║██║ ╚████║██║██║ ╚████║╚██████╔╝██╗\n" +
                " ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝╚═╝  ╚═══╝ ╚═════╝ ╚═╝\n" +
                "                                                             \n" +
                "WARNING: Real Google Cloud Vision API is being used!         \n" +
                "Please ensure all necessary precautions are taken:           \n" +
                "                                                             \n" +
                "1. Check your API usage and billing settings.                \n" +
                "2. Ensure proper authentication and security measures.       \n" +
                "3. Be aware of data privacy and compliance requirements.     \n" +
                "4. Monitor API calls to prevent unexpected costs.            \n" +
                "                                                             \n" +
                "Proceed with caution!                                        \n";

            logger.warn(warningMessage);
        }
    }

    public String convertPhotoToTextUsingGoogle(File someUserPhoto) throws IOException {
        if (useRealApi) {
            return cloudVisionApi.analyzeImageUsingGoogle(someUserPhoto);
        }
        String targetFileName = someUserPhoto.getName();
        boolean validDummyDataFileName = Arrays.stream(expectedFileNames).anyMatch(targetFileName::equals);
        if (validDummyDataFileName) {
            return getAssociatedCsvForFile(targetFileName);
        }
        return "No associated data found for the given file.";
    }

    private String[] getFileNamesFromLesChatsFolder() {
        File directory = new File(dummyPhotosPath);
        return directory.list();
    }

    private String getAssociatedCsvForFile(String fileName) throws IOException {
        String csvPath = dummyPhotosPath + fileName;
        String csvContent = new String(Files.readAllBytes(Paths.get(csvPath)));
        return csvContent;
    }
}