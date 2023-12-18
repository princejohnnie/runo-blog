package dev.levelupschool.backend.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ActiveProfiles("test")
public class StorageServiceTests {

    @Qualifier("fileSystemStorageService")
    @Autowired
    private StorageService fileSystemStorageService;

    @Qualifier("AWSStorageService")
    @Autowired
    private StorageService awsStorageService;

    @Value("${aws.access-key}")
    private String accessKey;

    @Value("${aws.secret-key}")
    private String secretKey;


    @Test
    public void givenS3Client_whenUploadObject_thenReturnObjectUrl() throws Exception {
        S3Client s3Client = S3Client.builder()
            .region(Region.EU_CENTRAL_1)
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
            .build();

        String bucketName = "john-levelup-bucket";
        String directoryPath = "avatars/" +  new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
            "avatar", "mocked_file.jpg", MediaType.IMAGE_JPEG_VALUE, "mocked_file.jpg".getBytes());

        String objectKey = directoryPath + mockMultipartFile.getOriginalFilename().hashCode();

        awsStorageService.store(mockMultipartFile, "avatars/");

        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .build();

        HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);

        Assertions.assertNotNull(headObjectResponse.metadata()); // Confirm that object actually exists in bucket
    }

    @Test
    public void givenMultipartFile_whenStoreFileWithFileSystem_thenReturnFilePath() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
            "cover", "mock_file.jpg", MediaType.IMAGE_JPEG_VALUE, "mock_file".getBytes());

        Path path = fileSystemStorageService.store(mockMultipartFile);

        String publicDirectory = "public/";
        Path filePath = Paths.get(publicDirectory, path.toString());

        Assertions.assertTrue(Files.exists(filePath), "Confirm that the multipartfile was actually saved to file system");
    }
}
