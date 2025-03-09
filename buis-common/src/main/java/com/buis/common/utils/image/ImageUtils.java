package com.buis.common.utils.image;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

public class ImageUtils {
    public static String encodeImage(String imagePath) throws Exception {
        File file = new File(imagePath);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }
}