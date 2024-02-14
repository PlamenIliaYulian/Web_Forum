//package com.PlamenIliaYulian.Web_Forum.controllers.REST;
//
//import com.PlamenIliaYulian.Web_Forum.services.contracts.FileStorageService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RequestMapping("/photos")
//@RestController
//public class FileUploadRestController {
//
//    private final FileStorageService fileService;
//
//    @Autowired
//    public FileUploadRestController(FileStorageService storageService) {
//        this.fileService = storageService;
//    }
//
//    @GetMapping("/new")
//    public void newFile(Model model) {
//        return "upload_form";
//    }
//
//    @PostMapping("/files/upload")
//    public void uploadFile(@RequestParam("file") MultipartFile file) {
//    }
//
//    @GetMapping("/files")
//    public String getListFiles(Model model) {
//    ...
//
//        return "files";
//    }
//
//    @GetMapping("/files/{filename:.+}")
//    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
//    ...
//
//        // return File
//    }
//}
