package com.ur.seminar.payload;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * This class models the response sent by the following APIS:
 * @see com.ur.seminar.controller.FileController#uploadFile(MultipartFile)
 * @see com.ur.seminar.controller.FileController#uploadMultipleFiles(MultipartFile[])
 * */
public class UploadFileResponse {

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

    /**
     * Constructor of the Response class
     * @param fileName name of the uploaded file
     * @param fileDownloadUri uri used in the getCalculation API
     * @see com.ur.seminar.controller.FileController#getCalculation(String, HttpServletRequest)
     * @param fileType type of the file (recommended .msg)
     * @param size of the file in bytes*/
    public UploadFileResponse(String fileName, String fileDownloadUri, String fileType, long size){
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }

    public String getFileName(){
        return this.fileName;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public long getSize() {
        return size;
    }
}
