//package UserPage.resources;
//
//import jakarta.enterprise.context.ApplicationScoped;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardCopyOption;
//import java.util.UUID;
//
//@ApplicationScoped
//public class UserService {
//    private final Path storageDirectory = Path.of("storage");
//
//    public UserService() {
//        try {
//            Files.createDirectories(storageDirectory);
//        } catch (IOException e) {
//            throw new RuntimeException("Could not create storage directory", e);
//        }
//    }
//
//    public String storeFile(InputStream fileInputStream, String originalFileName) {
//        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
//        String newFileName = UUID.randomUUID() + fileExtension;
//        Path filePath = storageDirectory.resolve(newFileName);
//
//        try {
//            Files.copy(fileInputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
//            return newFileName;
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to store file", e);
//        }
//    }
//}
