package com.gathering.util.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FileUtil {


    public void FileUpload(String serverPath, MultipartFile file) {

        File uploadDir = new File(serverPath);

        // 폴더가 없을경우 폴더 생성
        if(!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        try ( InputStream inputStream = file.getInputStream();
              OutputStream outputStream = new FileOutputStream(serverPath);
        ) {

            int readByte = 0;
            byte[] buffer = new byte[8192];

            // 파일 생성
            while ((readByte = inputStream.read(buffer, 0, 8120)) != -1) {
                outputStream.write(buffer, 0, readByte);
            }

            System.out.println("파일 생성 완료");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
