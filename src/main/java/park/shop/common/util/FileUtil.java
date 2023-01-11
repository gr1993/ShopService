package park.shop.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUtil {

    @Value("${file.dir}")
    private String fileDir;

    public String uploadFile(MultipartFile file) {
        try {
            if(!file.isEmpty()) {
                String storeFileName = createStoreFileName(file.getOriginalFilename());
                String fullPath = fileDir + storeFileName;
                file.transferTo(new File(fullPath));
                return fullPath;
            } else {
                return null;
            }
        } catch (IOException ex) {
            throw new RuntimeException("파일 작업 중 문제 발생", ex);
        }
    }

    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return  originalFilename.substring(pos + 1);
    }
}
