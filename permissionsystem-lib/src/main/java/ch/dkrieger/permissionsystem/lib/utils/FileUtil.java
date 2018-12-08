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

    public static void createDirectory(String path){
        new File(path).mkdirs();
    }
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
    public static void deleteFile(File file){
        if(file.exists()) file.delete();
    }
    public static void copyFileToDirectory(File file, File dest) throws IOException {
        if(dest == null || file == null) return;
        if(!file.exists()) return;
        if(!dest.exists()) dest.mkdirs();
        File n = new File(dest.getAbsolutePath()+"/"+file.getName());
        Files.copy(file.toPath(), n.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
    public static void copyFilesToDirectory(File from, File dest) throws IOException {
        if(dest == null || from == null) return;
        if(!dest.exists()) dest.mkdirs();
        if(!from.exists()) return;
        File[] files = from.listFiles();
        if(files == null) return;
        for(File file : files) {
            if(file == null) continue;
            if (file.isDirectory()) copyFilesToDirectory(file, new File(dest.getAbsolutePath()+"/"+file.getName()));
            else{
                File n = new File(dest.getAbsolutePath() + "/" + file.getName());
                Files.copy(file.toPath(), n.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
    public static void copyResourceFileToDirectory(String resourcefile, File dest) {
        InputStream localInputStream = FileUtil.class.getClassLoader().getResourceAsStream(resourcefile);
        try {
            Files.copy(localInputStream, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void renameFile(String file, String newfile){
        renameFile(new File(file),new File(newfile));
    }
    public static void renameFile(File file,File newfile){
        try{
            if(file == null || !(file.exists())) return;
            if(newfile.exists()) newfile.delete();
            newfile.createNewFile();
            file.renameTo(newfile);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
    public static void compressToZIP(File src, File dest) throws Exception{
        OutputStream os = Files.newOutputStream(dest.toPath());
            ZipOutputStream zs = new ZipOutputStream(os);
            Path pp = src.toPath();
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        if(Thread.interrupted()) throw new RuntimeException();
                        if(path.equals(dest.toPath())) return;
                        String sp = path.toAbsolutePath().toString().replace(pp.toAbsolutePath().toString(), "");
                        if(sp.length() > 0) sp = sp.substring(1);
                        ZipEntry zipEntry = new ZipEntry(pp.getFileName() + ((sp.length() > 0) ? (File.separator + sp) : ""));
                        try {
                            zs.putNextEntry(zipEntry);
                            zs.write(Files.readAllBytes(path));
                            zs.closeEntry();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
    }
    public static void extractFromZIP(File src, File dest) throws Exception {
        byte[] buffer = new byte[1024];
        FileInputStream fis = new FileInputStream(src);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry ze = zis.getNextEntry();
        while(ze != null){
            if(Thread.interrupted()) throw new RuntimeException();
            File newFile = new File(dest + File.separator + ze.getName());
            File parent = newFile.getParentFile();
            if(!parent.exists() && !parent.mkdirs()) throw new IllegalStateException("Couldn't create dir: " + parent);
            if(ze.isDirectory()){
                newFile.mkdir();
                ze = zis.getNextEntry();
                continue;
            }
            try(FileOutputStream fos = new FileOutputStream(newFile)){
                int len;
                while((len = zis.read(buffer)) > 0) fos.write(buffer, 0, len);
            }
            ze = zis.getNextEntry();
        }
        zis.closeEntry();
    }
    public static byte[] getFileContent(File file){
        if(!file.exists()) return null;
        try{
            FileInputStream input = null;
            byte[] bytes = null;
            try {
                bytes = new byte[(int) file.length()];
                input = new FileInputStream(file);
                input.read(bytes);
                return bytes;
            }finally {
                if(input != null)input.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
