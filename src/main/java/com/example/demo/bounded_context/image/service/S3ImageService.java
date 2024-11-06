package com.example.demo.bounded_context.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3ImageService {

    private final List<String> imgExtension = Arrays.asList("jpg", "jpeg", "png", "gif");

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    public String uploadImage(MultipartFile image) {
        try {
            return uploadImageToS3(image);
        } catch (IOException e) {
            throw new IllegalArgumentException("ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD");
        }
    }

    private String validateImageFileExtension(MultipartFile image) {
        String fileName = image.getOriginalFilename();
        if(image.isEmpty() || Objects.isNull(fileName)){
            throw new IllegalArgumentException("EMPTY_FILE_EXCEPTION : 파일이 존재하지 않습니다.");
        }

        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new IllegalArgumentException("NO_FILE_EXTENSION : 파일의 확장자가 없습니다.");
        }

        String extension = fileName.substring(lastDotIndex + 1).toLowerCase();
        if (!imgExtension.contains(extension)) {
            throw new IllegalArgumentException("INVALID_FILE_EXTENSION : 파일의 확장자가 유효하지 않습니다.");
        }
        return extension;
    }

    private String uploadImageToS3(MultipartFile image) throws IOException {
        String extension = validateImageFileExtension(image);

        String s3FileName = "image/"+UUID.randomUUID().toString().substring(0, 10) + image.getOriginalFilename(); //변경된 파일 명

        InputStream inputStreams = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(inputStreams);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extension);
        metadata.setContentLength(bytes.length);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try{
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata);
            amazonS3.putObject(putObjectRequest); // put image to S3
        }catch (Exception e){
            throw new IllegalArgumentException("ErrorCode.PUT_OBJECT_EXCEPTION");
        }finally {
            byteArrayInputStream.close();
            inputStreams.close();
        }

        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }

    public void deleteImageFromS3(String imageAddress){
        String key = getKeyFromImageAddress(imageAddress);
        try{
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        }catch (Exception e){
            throw new IllegalArgumentException("ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE");
        }
    }

    private String getKeyFromImageAddress(String imageAddress){
        try{
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        }catch (MalformedURLException | UnsupportedEncodingException e){
            throw new IllegalArgumentException("ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE");
        }
    }
}
