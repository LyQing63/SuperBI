package io.lyqing64.github.superbi.service;

import io.lyqing64.github.superbi.common.Response;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    Response<?> uploadAndParseFile(MultipartFile file);
}