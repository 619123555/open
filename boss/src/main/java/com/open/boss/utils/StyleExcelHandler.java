package com.open.boss.utils;


import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;

@Log4j2
public class StyleExcelHandler implements CellWriteHandler {


    public void sheet(int i, Sheet sheet) {

    }

    public void row(int i, Row row) {

    }

    public void cell(int i, Cell cell) {
        // 从第二行开始设置格式，第一行是表头
        log.info("-----------------------------------////////////////");
        Workbook workbook = cell.getSheet().getWorkbook();
        CellStyle cellStyle = createStyle(workbook);
        if (cell.getRowIndex() > 0) {
            log.info("开始更改格式");
            cell.getRow().getCell(i).setCellStyle(cellStyle);
        }
    }

    /**
     * 实际中如果直接获取原单元格的样式进行修改, 最后发现是改了整行的样式, 因此这里是新建一个样* 式
     */
    private CellStyle createStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        // 下边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        // 左边框
        cellStyle.setBorderLeft(BorderStyle.THIN);
        // 上边框
        cellStyle.setBorderTop(BorderStyle.THIN);
        // 右边框
        cellStyle.setBorderRight(BorderStyle.THIN);
        // 水平对齐方式
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 垂直对齐方式
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer integer, Integer integer1, Boolean aBoolean) {

    }

    @Override
    public void afterCellCreate(
            WriteSheetHolder writeSheetHolder,
            WriteTableHolder writeTableHolder,
            Cell cell,
            Head head,
            Integer integer,
            Boolean aBoolean) {
        log.info("第" + cell.getRowIndex() + "行，第{" + cell.getColumnIndex() + "}列写入完成。");
        //        if (!isHead && cell.getStringCellValue().contains("个")) {
        if (cell.getRowIndex() >= 0) {
            Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
            CellStyle cellStyle = workbook.createCellStyle();
//            Font cellFont = workbook.createFont();
//            cellFont.setBold(true);
//            cellStyle.setFont(cellFont);
            cellStyle.setDataFormat(workbook.createDataFormat().getFormat("@"));
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(cell.getStringCellValue());


        }
    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }
}
