package com.epam.lab7.v1.documentreader;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping("/documents/upload")
    public void uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        files.stream()
                .filter(file -> !file.isEmpty())
                .map(this::getUploadedFile)
                .forEach(this::processFile);
    }

    @PostMapping("/documents/query")
    public String queryDocument(@RequestBody String query) {
        return documentService.queryDocument(query);
    }

    private File getUploadedFile(MultipartFile file) {
        Path uploadPath = getUploadPath();
        Path filePath = copyUploadedFile(file, uploadPath);
        return filePath.toFile();
    }

    private Path getUploadPath() {
        Path uploadPath = Paths.get("target", "uploads");

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create upload directory", e);
            }
        }
        return uploadPath;
    }

    private Path copyUploadedFile(MultipartFile file, Path uploadPath) {
        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        try {
            Files.copy(file.getInputStream(), filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy uploaded file", e);
        }
        return filePath;
    }

    private void processFile(File uploadedFile) {
        try {
            documentService.addDocument(uploadedFile);
        } finally {
            uploadedFile.delete();
        }
    }
}
