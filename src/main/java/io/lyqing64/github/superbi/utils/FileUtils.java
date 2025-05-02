package io.lyqing64.github.superbi.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Slf4j
public class FileUtils {

    /**
     * 读取本地Excel文件，解析为CSV后调用AI助手
     */
    public static String processExcelToCsvStr(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return "";
        }

        ExcelTypeEnum excelType = determineExcelType(filePath);

        try (InputStream inputStream = new FileInputStream(file)) {
            List<Map<Integer, String>> dataList = EasyExcel.read(inputStream)
                    .excelType(excelType)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();

            if (dataList == null || dataList.isEmpty()) {
                return "";
            }

            // 转换为CSV格式
            StringBuilder csvBuilder = new StringBuilder();
            if (!dataList.isEmpty()) {
                Map<Integer, String> firstRow = dataList.get(0);
                for (Integer key : firstRow.keySet()) {
                    csvBuilder.append(firstRow.get(key)).append(",");
                }
                csvBuilder.deleteCharAt(csvBuilder.length() - 1).append("\n");
            }

            for (Map<Integer, String> row : dataList) {
                for (String value : row.values()) {
                    csvBuilder.append(value).append(",");
                }
                csvBuilder.deleteCharAt(csvBuilder.length() - 1).append("\n");
            }

            String csvContent = csvBuilder.toString();
            return csvContent;

        } catch (IOException e) {
            log.error("读取Excel文件失败: {}", e.getMessage());
            return "";
        }
    }

    /**
     * 从输入流读取Excel文件，解析为CSV字符串
     *
     * @param file             临时文件
     * @param originalFilename 原始文件名（用于判断Excel类型）
     * @return CSV格式字符串，解析失败返回空字符串
     */
    public static String processExcelToCsvStr(File file, String originalFilename) throws Exception {
        if (file == null) {
            log.error("输入流供应器为空");
            return "";
        }

        ExcelTypeEnum excelType = determineExcelType(originalFilename);

        InputStream inputStream = new FileInputStream(file);
        List<Map<Integer, String>> dataList = EasyExcel.read(inputStream)
                .excelType(excelType)
                .autoCloseStream(true)
                .sheet()
                .headRowNumber(0)
                .doReadSync();

        if (dataList == null || dataList.isEmpty()) {
            return "";
        }

        StringBuilder csvBuilder = new StringBuilder();

        Map<Integer, String> firstRow = dataList.get(0);
        for (Integer key : firstRow.keySet()) {
            csvBuilder.append("COL_").append(key).append(",");
        }
        csvBuilder.deleteCharAt(csvBuilder.length() - 1).append("\n");

        for (Map<Integer, String> row : dataList) {
            for (Integer key : row.keySet()) {
                csvBuilder.append(escapeCsv(row.get(key))).append(",");
            }
            csvBuilder.deleteCharAt(csvBuilder.length() - 1).append("\n");
        }

        return csvBuilder.toString();
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }

    public static ExcelTypeEnum determineExcelType(String filename) {
        if (filename.endsWith(".xls")) {
            return ExcelTypeEnum.XLS;
        } else if (filename.endsWith(".xlsx")) {
            return ExcelTypeEnum.XLSX;
        }
        throw new IllegalArgumentException("Unsupported file type");
    }
}
