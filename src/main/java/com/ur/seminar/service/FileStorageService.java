package com.ur.seminar.service;

import com.ur.seminar.exception.FileStorageException;
import com.ur.seminar.exception.MyFileNotFoundException;
import com.ur.seminar.property.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;


import org.springframework.util.StringUtils;


import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
/**
 * This class contains the logic of storing and downloading a file from the file system
 * It is used in some methods of the FileController class
 * @see com.ur.seminar.controller.FileController
 * */
@Service
public class FileStorageService {

    private final Path directoryLocation;
    private final Path fileUploadLocation;
    private final Path fileDownloadLocation;
    /**
     * Constructor of the class
     * @param fileStorageProperties object of the FileStorageProperties class containing different paths
     * */
    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties){
        this.fileUploadLocation = Paths.get(fileStorageProperties.getUpload())
                .toAbsolutePath().normalize();
        this.fileDownloadLocation = Paths.get(fileStorageProperties.getDownload()).toAbsolutePath().normalize();
        this.directoryLocation = Paths.get(fileStorageProperties.getDirectory()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileUploadLocation);
        } catch (Exception ex){
            throw new FileStorageException("Could not create directory where the uploaded file will be stored.", ex);
        }
    }
    /**
     * Stores a file into the directory defined in the fileuploadLocation variable
     * @param file the file to store
     * @return the filename of the stored file*/
    public String storeFile(MultipartFile file){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")){
                throw new FileStorageException("Sorry! Filename contains invalid path sequence" + fileName);
            }

            Path targetLocation = this.fileUploadLocation.resolve(fileName);
            Files.copy(file.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex){
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
    /**
     * Stores a file into the directory defined in the databaseLocation variable
     * @param file the file to store
     * @return the filename of the stored file*/
    public String addToDirectory(MultipartFile file){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")){
                throw new FileStorageException("Sorry! Filename contains invalid path sequence" + fileName);
            }

            Path targetLocation = this.directoryLocation.resolve(fileName);
            Files.copy(file.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex){
            throw new FileStorageException("Could not add file " + fileName + "to database!", ex);
        }
    }
    /**
     * Loads a file from the package defined int fileDownloadLocation variable
     * @param fileName the name of the file to load
     * @return a file packed into a Resource object*/
    public Resource loadFileAsResource(String fileName){
        try {
            Path filePath = this.fileDownloadLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex){
            throw new MyFileNotFoundException("File not found " +  fileName, ex);
        }
    }
    /**
     * Deletes all entries from the differentiator package
     * Deletes all entries from the uploads package
     * @return the number of deleted files*/
    public int clearDirectories(){
        int count = 0;
        File results = new File(".\\results\\differentiator");
        File[] files = results.listFiles();
        if(files != null){
            count += files.length;
            for (File file : files) {
                file.delete();
        }
        }

        File uploads = new File(".\\uploads");
        files = uploads.listFiles();
        if(files != null){{
            count += files.length;
            for(File file : files){
                file.delete();
            }
        }}
        return count;
    }
    /**
     * Deletes a selected entry from the .\directory package
     * @param fileName the name of the file to delete
     * */
    public void deleteEntry(String fileName){
        File databasePath = new File(".\\directory");
        File[] database = databasePath.listFiles();

        if(database != null){
        for(File file : database){
            if(file.getName().equals(fileName)){
                file.delete();
                return;
            }
        }
        }
    }

}
