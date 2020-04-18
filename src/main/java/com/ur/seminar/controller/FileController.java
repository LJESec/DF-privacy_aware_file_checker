package com.ur.seminar.controller;
import com.ur.seminar.Differentiator;
import com.ur.seminar.payload.UpdateDirectoryResponse;
import com.ur.seminar.payload.UploadFileResponse;
import com.ur.seminar.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class models the different apis, each represented by its own method:
 * @see #addToDirectory(MultipartFile)
 * @see #addMultipleToDirectory(MultipartFile[])
 * @see #deleteEntry(String)
 * @see #uploadFile(MultipartFile)
 * @see #uploadMultipleFiles(MultipartFile[])
 * @see #clearInputAndOutput()
 * @see #getCalculation(String, HttpServletRequest)
 *
 * */
@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    /**
     * @see FileStorageService for information about the Service class*/
    @Autowired
    private FileStorageService fileStorageService;

    /**POST-API to <b>upload</b> a file to the <b>directory</b> package
     * @param file the multipart file to upload
     * @return a UpdateDirectoryResponse as an HTTP Response
     * @see UpdateDirectoryResponse*/
    @PostMapping("/addToDirectory")
    public UpdateDirectoryResponse addToDirectory(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.addToDirectory(file);
        int databaseCount;
        File databaseDirectory = new File( ".\\directory");

        if(databaseDirectory.listFiles() != null){
            databaseCount = databaseDirectory.listFiles().length;
        }else{
            databaseCount = 0;
        }

        return new UpdateDirectoryResponse(fileName,
                file.getContentType(), file.getSize(),databaseCount);
    }
    /**
     * POST-API to <b>upload multiple</b> files to the <b>directory</b> package
     * @param files an array of multipart files to upload
     * @return a List of UpdateDirectoryResponse as HTTP Responses
     * @see UpdateDirectoryResponse*/
    @PostMapping("/addMultipleToDirectory")
    public List<UpdateDirectoryResponse> addMultipleToDirectory(@RequestParam("files") MultipartFile[] files) {

        return Arrays.stream(files)
                .map(this::addToDirectory)
                .collect(Collectors.toList());
    }

    /**
     * POST-API to <b>upload</b> a file to the <b>uploads</b> directory
     * @param file a file the user wants to upload to the system
     * @return a UploadFileResponse as an HTTP Response
     * @see UploadFileResponse*/
    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        File inputPath = new File("./uploads/" + fileName);
        //File[] inputFiles = inputPath.listFiles();
        //File inputFile = new File;

        String outputName;
       // assert(inputFiles != null);
        try {
            Differentiator differentiator = new Differentiator(inputPath,fileName);
            outputName = differentiator.getOutputName();
        }catch(IOException e){
            e.printStackTrace();
            outputName = "error";
        }

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/getCalculation/")
                .path(outputName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    /**
     * POST-API to <b>upload multiple</b> files to the <b>uploads</b> directory
     * @param files an array of files the user wants to upload to the system
     * @return a List of UploadFileResponse
     * @see UploadFileResponse*/
    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {

        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }
    /**
     * GET-API to <b>download</b> the output file from the results package
     * @param fileName output + "number of the file"
     * @param request an HTTP request
     * @return this method returns a ".csv" file created in the Differentiator class and stored in the results package
     * @see Differentiator
     * */
    @GetMapping("/getCalculation/{fileName:.+}")
    public ResponseEntity<Resource> getCalculation(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    /**
     * API to delete the content of the upload and the results directories
     * @return a HTTP Response containing a simple message showing the number of deleted files*/
    @DeleteMapping("/clearInputAndOutput")
    public ResponseEntity<?> clearInputAndOutput(){
        int counter = fileStorageService.clearDirectories();
        return ResponseEntity.ok()
                .header(HttpHeaders.ACCEPT)
                .body("Directories cleared and " + counter + " files were deleted!" );
    }
    /**
     * API to delete a single entry from the directory package
     * @param fileName filename of the file to delete
     * @return a HTTP Response containing the name of the deleted file*/
    @DeleteMapping("/deleteEntry/{fileName:.+}")
    public ResponseEntity<?> deleteEntry(@PathVariable String fileName){
        fileStorageService.deleteEntry(fileName);
        return ResponseEntity.ok().header(HttpHeaders.ACCEPT)
                .body(fileName + " was deleted from the directory");
    }
}
