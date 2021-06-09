package com.open.boss.utils;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

public class EasyExcelUtils {

    private static final Logger logger = LoggerFactory.getLogger(EasyExcelUtils.class);


    public static void createExcelStreamMutilByEaysExcel(HttpServletResponse response, Map<String, List<? extends BaseRowModel>> SheetNameAndDateList, ExcelTypeEnum type, String filename) {
        if (checkParam(SheetNameAndDateList, type)) {
            return;
        }
        try {
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename + type.getValue());
            ServletOutputStream out = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(out, type, true);
            setSheet(SheetNameAndDateList, writer);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            logger.error("Exception:{}", e);
        }
    }

    public static void createExcelHandlerStyleByEasyExcel(HttpServletResponse response, Map<String, List<? extends BaseRowModel>> SheetNameAndDateList, ExcelTypeEnum type, String filename) {
        if (checkParam(SheetNameAndDateList, type)) {
            return;
        }
        try {
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename + type.getValue());
            ServletOutputStream out = response.getOutputStream();
            StyleExcelHandler handler = new StyleExcelHandler();
            ExcelWriter writer = new ExcelWriter(null, out, type, true, handler);
            setSheet(SheetNameAndDateList, writer);
            writer.finish();
            out.flush();
        } catch (IOException e) {
            logger.error("Exception:{}", e);
        }
    }
    private static boolean checkParam(Map<String, List<? extends BaseRowModel>> SheetNameAndDateList, ExcelTypeEnum type) {
        if (CollectionUtils.isEmpty(SheetNameAndDateList)) {
            logger.error("SheetNameAndDateList不能为空");
            return true;
        } else if (type == null) {
            logger.error("导出的excel类型不能为空");
            return true;
        }
        return false;
    }

    private static void setSheet(Map<String, List<? extends BaseRowModel>> SheetNameAndDateList, ExcelWriter writer) {
        int sheetNum = 1;
        for (Map.Entry<String, List<? extends BaseRowModel>> stringListEntry : SheetNameAndDateList.entrySet()) {
            Sheet sheet = new Sheet(sheetNum, 0, stringListEntry.getValue().get(0).getClass());
            sheet.setSheetName(stringListEntry.getKey());
            writer.write(stringListEntry.getValue(), sheet);
            sheetNum++;
        }
    }
}
