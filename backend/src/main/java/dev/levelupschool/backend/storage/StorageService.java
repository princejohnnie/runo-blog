package dev.levelupschool.backend.storage;

import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.nio.file.Path;

public interface StorageService {
    void init();

    Path store(MultipartFile file);

    URL store(MultipartFile file, String path);

    void deleteAll();
}
