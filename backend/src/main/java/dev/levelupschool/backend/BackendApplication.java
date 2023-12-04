package dev.levelupschool.backend;

import dev.levelupschool.backend.storage.StorageProperties;
import dev.levelupschool.backend.storage.StorageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

    @Bean
    CommandLineRunner initLocalStorage(@Qualifier("fileSystemStorageService") StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }

    @Bean
    CommandLineRunner initAWSStorage(@Qualifier("AWSStorageService") StorageService storageService) {
        return (args) -> {
            storageService.init();
        };
    }

}
