package com.ur.seminar;

import com.ur.seminar.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**The original algorithm and base functions, were created by
 * Marcus
 * The API Expansion was created by
 * @author Daniel
 * @see com.ur.seminar.controller.FileController
 * @see FileStorageProperties
 */
@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class Main {
/**
 * Main method of the program
 * @param args normal part of the main method
 * */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

    }
}
