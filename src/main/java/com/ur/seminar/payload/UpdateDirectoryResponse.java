package com.ur.seminar.payload;

import org.springframework.web.multipart.MultipartFile;

/**
 * This class models the response sent by the following APIS:
 * @see com.ur.seminar.controller.FileController#addToDirectory(MultipartFile)
 * @see com.ur.seminar.controller.FileController#addMultipleToDirectory(MultipartFile[]) */
public class UpdateDirectoryResponse {

    private String fileName;

    private String fileType;

    private long size;

    private int databaseCount;

    /**
     * Constructor of the Response class
     * @param fileName name of the uploaded file
     * @param fileType type of the uploaded file (recommended type .msg)
     * @param size size of the upload file in bytes
     * @param databaseCount the number of files in the ./directory package */
    public UpdateDirectoryResponse(String fileName, String fileType, long size, int databaseCount) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.size = size;
        this.databaseCount = databaseCount;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public long getSize() {
        return size;
    }

    public int getDatabaseCount() {
        return databaseCount;
    }
}
