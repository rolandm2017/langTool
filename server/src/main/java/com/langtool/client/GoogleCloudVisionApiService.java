package com.langtool.client;


import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

public class GoogleCloudVisionApiService {

    public String analyzeImageUsingGoogle(File someUserPhoto) throws IOException {
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            // Reads the image file into memory
            ByteString imgBytes;
            try (FileInputStream fileInputStream = new FileInputStream(someUserPhoto)) {
                imgBytes = ByteString.readFrom(fileInputStream);
            }

            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();
            requests.add(request);

            // Performs text detection on the image file
            AnnotateImageResponse response = vision.batchAnnotateImages(requests).getResponses(0);

            if (response.hasError()) {
                throw new IOException(response.getError().getMessage());
            }

            return response.getTextAnnotationsList().isEmpty() ? null : 
                   response.getTextAnnotations(0).getDescription();
        }
    }

    // public static void main(String[] args) {
    //     try {
    //         File photoFile = new File("path/to/your/image.jpg");
    //         String result = analyzeImageUsingGoogle(photoFile);
    //         if (result != null) {
    //             System.out.println("Detected text: " + result);
    //         } else {
    //             System.out.println("No text detected.");
    //         }
    //     } catch (IOException e) {
    //         System.err.println("Error: " + e.getMessage());
    //     }
    // }
}
