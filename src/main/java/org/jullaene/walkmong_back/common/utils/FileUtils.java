package org.jullaene.walkmong_back.common.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class FileUtils {
    private static final String BASE_DIR = "freeSign";

    /**
     * UUID 파일명 반환
     */
    public static String getUuidFileName() {
        return UUID.randomUUID().toString();
    }


    /**
     * Multipart 의 contentType 값에서 / 이후 확장자만 자름
     * @param contentType ex) image/png
     * @return ex) png
     */
    public static String getFileType (String contentType) {
        if (StringUtils.hasText(contentType)) {
            return contentType.substring(contentType.lastIndexOf('/') + 1);
        }
        return null;
    }

    /**
     * 파일 전체 경로 생성
     * @param fileId 생성된 파일 고유 ID
     * @param fileType 확장자
     */
    public static String createPath (String fileId, String fileType, String folderName, String dirPath) {
        return String.format("%s/%s/%s.%s", BASE_DIR + dirPath, folderName, fileId, fileType);
    }

    /**
     * 년/월/일 폴더명 반환
     */
    public static String getFolderName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String str = sdf.format(date);
        return str.replace("-", "/");
    }

    /**
     * 파일 유효성 체크
     * @param multipartFile file
     */
    public static void checkInvalidUploadFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty() || multipartFile.getSize() == 0) {
            throw new CustomException(HttpStatus.NOT_FOUND, ErrorType.INVALID_FILE);
        }
    }

    public static boolean isFileNull(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty() || multipartFile.getSize() == 0) {
            return true;
        }

        return false;
    }
}
