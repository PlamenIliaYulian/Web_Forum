package com.PlamenIliaYulian.Web_Forum.controllers.REST;

import com.PlamenIliaYulian.Web_Forum.services.contracts.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/photos")
@RestController
public class PhotosRestController {

    private final FileStorageService fileService;

    @Autowired
    public PhotosRestController(FileStorageService storageService) {
        this.fileService = storageService;
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> getPhotoByName(@PathVariable String fileName) {
        byte[] photo = fileService.getPhotoByName(fileName);
        String contentType = determineContentType(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(contentType))
                .body(photo);
    }

    private String determineContentType(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        switch (fileExtension.toLowerCase()) {
            case "png":
                return "image/png";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            default:
                return "application/octet-stream";
        }
    }

}
