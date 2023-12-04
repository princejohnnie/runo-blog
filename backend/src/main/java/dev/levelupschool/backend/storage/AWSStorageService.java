package dev.levelupschool.backend.storage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URL;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AWSStorageService implements StorageService {

    @Autowired
    private AWSProperties awsProperties;

    String bucketName = "john-levelup-bucket";

    S3Client s3Client;

    @Override
    public void init() {
        s3Client = S3Client.builder()
            .region(Region.EU_CENTRAL_1)
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsProperties.getAccessKey(), awsProperties.getSecretKey())))
            .build();
    }

    @Override
    public URL store(MultipartFile file, String path) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }

            String prefix = path +  new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String key = prefix + file.getOriginalFilename();

            s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build(), RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return s3Client.utilities().getUrl(builder ->
                builder.bucket(bucketName).key(key).build());

        } catch (Exception e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Path store(MultipartFile file) {
        return null;
    }

    @Override
    public void deleteAll() {
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
            .bucket(bucketName)
            .build();

        ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

        listResponse.contents().forEach(object -> {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(object.key())
                .build();

            s3Client.deleteObject(deleteRequest);
            System.out.println("Deleted object: " + object.key());
        });
    }
}
