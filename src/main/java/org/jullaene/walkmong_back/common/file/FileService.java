package org.jullaene.walkmong_back.common.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.jullaene.walkmong_back.common.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {

    @Value("${cloud.aws.bucket}")
    private String bucket;
    private final AmazonS3Client s3Client;

    /**
     * S3 file upload
     * @param multipartFile file
     * @return file url
     */
    public String uploadFile(MultipartFile multipartFile, String dirPath) {
        FileUtils.checkInvalidUploadFile(multipartFile);

        try {
            // Set metadata for the file
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getSize());

            // Construct file path
            String filePath = dirPath + "/" + multipartFile.getOriginalFilename();

            // Upload file to S3
            s3Client.putObject(new PutObjectRequest(bucket, filePath, multipartFile.getInputStream(), objectMetadata));

            // Generate file URL
            String uploadFileUrl = s3Client.getUrl(bucket, filePath).toString();
            log.info("File upload success : {}", uploadFileUrl);

            return uploadFileUrl;
        } catch (IOException e) {
            log.error("File upload failed : {}", e.getMessage(), e);
            throw new RuntimeException("File upload failed", e);
        }
    }

    /**
     * S3 file delete
     * @return Failed Delete Object Counts
     */
    public int deleteFile (Integer idCount, List<DeleteObjectsRequest.KeyVersion> keys) {
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket).withKeys(keys);
        DeleteObjectsResult result = s3Client.deleteObjects(deleteObjectsRequest);
        int count = idCount - result.getDeletedObjects().size();
        log.info("Failed Delete Object Counts = {}", idCount - result.getDeletedObjects().size());
        return count;
    }

    /**
     * S3 file download
     */
    public ResponseEntity<byte[]> downloadFile (String originFileName, String filePath) {
        byte[] bytes = null;
        HttpHeaders httpHeaders = new HttpHeaders();

        try {
            S3Object o = s3Client.getObject(new GetObjectRequest(bucket, filePath));
            if (o == null) throw new CustomException(HttpStatus.NOT_FOUND, ErrorType.INVALID_S3_FILE);

            S3ObjectInputStream objectInputStream = o.getObjectContent();

            bytes = IOUtils.toByteArray(objectInputStream);

            String fileName = URLEncoder.encode(originFileName, String.valueOf(StandardCharsets.UTF_8)).replaceAll("\\+", "%20");
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentLength(bytes.length);
            httpHeaders.setContentDispositionFormData("attachment", fileName);

        } catch (AmazonS3Exception e) {
            log.error("File not find : {}", e.getMessage());
            throw new CustomException(HttpStatus.NOT_FOUND, ErrorType.INVALID_S3_FILE);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("File upload failed : {}", e.getMessage());
        }

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

}
