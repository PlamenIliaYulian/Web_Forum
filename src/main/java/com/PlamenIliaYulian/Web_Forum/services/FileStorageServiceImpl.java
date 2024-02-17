package com.PlamenIliaYulian.Web_Forum.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import com.PlamenIliaYulian.Web_Forum.models.Avatar;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.AvatarRepository;
import com.PlamenIliaYulian.Web_Forum.services.contracts.FileStorageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import java.util.Map;



@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final Path root = Paths.get("C:/Developing stuff/Telerik Academy/04. Web/Web_Forum/uploads/");

    private final String pathToFile = "C:/Developing stuff/Telerik Academy/04. Web/Web_Forum/uploads/";

    private final String CLOUDINARY_URL = "cloudinary://242857587276945:B5ODyO381gN-4aFLKDNVcrAFzxM@dol3hflxs";

    private final AvatarRepository avatarRepository;

    @Autowired
    public FileStorageServiceImpl(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public String save(MultipartFile file) {
        try {

            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
            return root + file.getOriginalFilename();

        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    @Override
    public Avatar uploadImageToFileSystem(MultipartFile file) {

        String filePath = pathToFile + file.getOriginalFilename();

        Avatar newAvatar = avatarRepository.createAvatar(new Avatar(filePath));

        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return newAvatar;
    }
    @Override
    public byte[] getPhotoByName(String name) {
        Avatar avatar = avatarRepository.getAvatarByName(name);
        String filePath = avatar.getAvatar();
        try {
            byte[] image = Files.readAllBytes(new File(filePath).toPath());
            return image;
        } catch (IOException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public String uploadPictureToCloudinary(File file){
        Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
        cloudinary.config.secure = true;
        System.out.println(cloudinary.config.cloudName);

        try {
            Map params1 = ObjectUtils.asMap(
                    "use_filename", true,
                    "unique_filename", false,
                    "overwrite", true
            );


            return cloudinary
                    .uploader()
                    .upload("https://cloudinary-devs.github.io/cld-docs-assets/assets/images/coffee_cup.jpg", params1)
                    .get("secure_url")
                    .toString();
        }
        catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

}

