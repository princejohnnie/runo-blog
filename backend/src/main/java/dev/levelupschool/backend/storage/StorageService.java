package dev.levelupschool.backend.storage;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {
    void init();

    Path store(MultipartFile file);

    void deleteAll();
}
