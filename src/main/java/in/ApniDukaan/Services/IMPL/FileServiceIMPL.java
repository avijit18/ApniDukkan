package in.ApniDukaan.Services.IMPL;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import in.ApniDukaan.Services.Interfaces.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class FileServiceIMPL implements FileService {

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket.category}")
    private String categoryBucket;

    @Value("${aws.s3.bucket.product}")
    private String productBucket;

    @Value("${aws.s3.bucket.profile}")
    private String profileBucket;

    @Override
    public boolean uploadFileToAwsS3(MultipartFile file, Integer bucketType) {
        String bucketName = null;
        try {
            if (bucketType == 1) {
                bucketName = categoryBucket;
            } else if (bucketType == 2) {
                bucketName = productBucket;
            } else {
                bucketName = profileBucket;
            }
            String fileName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName,
                    inputStream, objectMetadata);
            PutObjectResult saveData = s3Client.putObject(putObjectRequest);
            if (!ObjectUtils.isEmpty(saveData)) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
