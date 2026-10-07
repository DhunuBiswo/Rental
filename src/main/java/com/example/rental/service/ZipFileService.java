package com.example.rental.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.rental.entity.ZipFile;
import com.example.rental.repository.ZipFileRepository;

@Service
public class ZipFileService {

    @Autowired
    private ZipFileRepository zipFileRepository;

    public ZipFile uploadFile(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        if (!"application/zip".equalsIgnoreCase(file.getContentType())
                && !file.getOriginalFilename().toLowerCase().endsWith(".zip")) {
            throw new RuntimeException("Only ZIP files are allowed");
        }

        ZipFile zipFile = new ZipFile();

        zipFile.setFileName(file.getOriginalFilename());
        zipFile.setContentType(file.getContentType());
        zipFile.setFileData(file.getBytes());

        return zipFileRepository.save(zipFile);
    }

    public List<ZipFile> getAllFiles() {
        return zipFileRepository.findAll();
    }

    public void deleteAllFiles() {
        zipFileRepository.deleteAll();
    }
}
