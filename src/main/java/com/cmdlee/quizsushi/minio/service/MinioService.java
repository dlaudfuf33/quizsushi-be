package com.cmdlee.quizsushi.minio.service;

import com.cmdlee.quizsushi.global.exception.ErrorCode;
import com.cmdlee.quizsushi.global.exception.GlobalException;
import com.cmdlee.quizsushi.global.infra.minio.MinioProperties;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URI;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;
    private final MinioProperties properties;
    private final String TMP = "public/tmp/";
    private final String QUIZ = "public/quiz/";

    public String uploadTempFile(MultipartFile file) throws Exception {
        String ext = extractExtension(file.getOriginalFilename());
        String objectName = TMP + UUID.randomUUID() + ext;


        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(properties.getBucket())
                            .object(objectName)
                            .stream(is, file.getSize(), -1)
                            .contentType(resolveContentType(file.getOriginalFilename()))
                            .build()
            );
        }
        return buildPublicUrl(objectName);
    }

    public String moveTempFileToQuizFolder(String tmpObjectKey, String mediaKey) {
        String fileName = tmpObjectKey.substring(tmpObjectKey.lastIndexOf("/") + 1);
        String destObjectKey = QUIZ + mediaKey + "/" + fileName;

        try (InputStream is = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(properties.getBucket())
                        .object(tmpObjectKey)
                        .build()
        )) {

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(properties.getBucket())
                            .object(destObjectKey)
                            .stream(is, -1, 10485760)
                            .contentType(resolveContentType(fileName))
                            .build()
            );

            // 기존 tmp 객체 삭제
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(tmpObjectKey)
                    .build());

            return buildPublicUrl(destObjectKey);

        } catch (Exception e) {
            throw new GlobalException(ErrorCode.FILE_MOVE_FAILED);
        }
    }

    public void deleteAllWithPrefix(String id) {
        String prefix = QUIZ+id;
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(properties.getBucket())
                            .prefix(prefix)
                            .recursive(true)
                            .build()
            );
            for (Result<Item> result : results) {
                String objectName = result.get().objectName();
                minioClient.removeObject(RemoveObjectArgs.builder()
                        .bucket(properties.getBucket())
                        .object(objectName)
                        .build());
            }
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.FILE_DELETE_FAILED);
        }
    }

    private String resolveContentType(String filename) {
        if (filename == null) return "application/octet-stream";

        String lower = filename.toLowerCase();
        if (lower.endsWith(".mp4")) return "video/mp4";
        if (lower.endsWith(".webm")) return "video/webm";
        if (lower.endsWith(".mov")) return "video/mp4";
        if (lower.endsWith(".mp3")) return "audio/mpeg";
        if (lower.endsWith(".wav")) return "audio/wav";
        if (lower.endsWith(".m4a")) return "audio/mp4";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".png")) return "image/png";
        return "application/octet-stream";
    }

    public String buildPublicUrl(String objectKey) {
        String url = properties.getEndpoint() + "/" + properties.getBucket() + "/" + objectKey;
        System.out.println(url);
        return url;
    }

    public String rewriteTempMediaLinks(String content, String mediaKey) {
        if (content == null) return "";

        Pattern pattern = Pattern.compile("https?://[^\\s\"')>]+/(quizsushi/)?tmp/[^\\s\"')>]+");
        Matcher matcher = pattern.matcher(content);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String tmpUrl = matcher.group();
            String objectKey = extractObjectKeyFromUrl(tmpUrl);
            String publicUrl = moveTempFileToQuizFolder(objectKey, mediaKey);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(publicUrl));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String extractObjectKeyFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String path = uri.getPath();
            return path.substring(path.indexOf("public/tmp/"));
        } catch (Exception e) {
            throw new IllegalArgumentException("URL 파싱 실패: " + url, e);
        }
    }

    private String extractExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }
}