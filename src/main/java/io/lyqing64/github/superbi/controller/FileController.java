package io.lyqing64.github.superbi.controller;

import io.lyqing64.github.superbi.common.Response;
import io.lyqing64.github.superbi.service.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public Response<?> uploadFile(MultipartFile file) {
        return fileService.uploadAndParseFile(file);
    }

}