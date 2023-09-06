package com.web_food.library.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUpload {
    private final String UPLOAD_FOLDER= "F:\\Documents\\SPRING MVC\\Web_Food\\Admin\\src\\main\\resources\\static\\img\\PImg";
    public boolean upLoadFile(MultipartFile file){
        boolean isboolean = false;
        try{
            Files.copy(file.getInputStream(), Paths.get(UPLOAD_FOLDER + File.separator + file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            isboolean = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return isboolean;
    }
    public boolean checkexit(MultipartFile file){
        boolean is_exit = false;
        try{
            File file1 = new File(UPLOAD_FOLDER +"\\"+file.getOriginalFilename());
            is_exit = file1.exists();
        }catch (Exception e){
            e.printStackTrace();
        }
        return is_exit;
    }
}
