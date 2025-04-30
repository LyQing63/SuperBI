-- 文件上传记录表
CREATE TABLE IF NOT EXISTS file_upload
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT,
    file_name   VARCHAR(255) NOT NULL,
    file_path   VARCHAR(500) NOT NULL,
    upload_time TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    status      VARCHAR(20) DEFAULT 'PENDING' -- PENDING / PARSING / SUCCESS / FAIL
);

-- 解析后的表格数据
CREATE TABLE IF NOT EXISTS parsed_data
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    file_id      BIGINT NOT NULL,
    content_json JSON   NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (file_id) REFERENCES file_upload (id) ON DELETE CASCADE
);

-- AI生成的图表配置
CREATE TABLE IF NOT EXISTS chart_generate
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    parsed_data_id BIGINT NOT NULL,
    chart_json     JSON   NOT NULL,
    model_used     VARCHAR(100),
    prompt_used    TEXT,
    created_at     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    status         VARCHAR(20) DEFAULT 'SUCCESS', -- SUCCESS / FAILED
    FOREIGN KEY (parsed_data_id) REFERENCES parsed_data (id) ON DELETE CASCADE
);