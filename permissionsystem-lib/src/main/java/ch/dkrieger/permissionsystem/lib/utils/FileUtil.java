package ch.dkrieger.permissionsystem.lib.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 10.06.18 14:02
 *
 */

public class FileUtil {

    public static void deleteDirectory(String path){
        deleteDirectory(new File(path));
    }

    public static void deleteDirectory(File file){
        if(file.exists()){
            File[] files = file.listFiles();
            if(files == null) return;
            for(File entries : files){
                if(entries.isDirectory()) deleteDirectory(entries.getAbsolutePath());
                else entries.delete();
            }
        }
        file.delete();
    }

    public static void renameFile(String file, String newFile){
        renameFile(new File(file),new File(newFile));
    }

    public static void renameFile(File file,File newFile){
        try{
            if(file == null || !(file.exists())) return;
            if(newFile.exists()) newFile.delete();
            newFile.createNewFile();
            file.renameTo(newFile);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
