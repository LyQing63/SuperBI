package io.lyqing64.github.superbi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.lyqing64.github.superbi.domain.FileUpload;
import io.lyqing64.github.superbi.mapper.FileUploadMapper;
import io.lyqing64.github.superbi.service.FileUploadService;
import org.springframework.stereotype.Service;

/**
 * @author qingly
 * @description 针对表【file_upload】的数据库操作Service实现
 * @createDate 2025-04-30 16:35:54
 */
@Service
public class FileUploadServiceImpl extends ServiceImpl<FileUploadMapper, FileUpload>
        implements FileUploadService {
    // MyBatis-Plus 会自动生成 save 方法的实现
}
