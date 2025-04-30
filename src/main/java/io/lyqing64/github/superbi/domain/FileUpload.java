package io.lyqing64.github.superbi.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName file_upload
 */
@TableName(value = "file_upload")
@Data
public class FileUpload implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "user_id")
    private Long userId;
    @TableField(value = "file_name")
    private String fileName;
    @TableField(value = "file_path")
    private String filePath;
    @TableField(value = "upload_time")
    private Date uploadTime;
    @TableField(value = "status")
    private String status;
}
