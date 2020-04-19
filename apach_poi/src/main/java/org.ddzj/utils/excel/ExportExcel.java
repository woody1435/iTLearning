/**
 * Copyright (C), 2015-2020, 大道至简
 * FileName: ExportExcel
 * Author:   ddzj
 * Date:     2020/4/2 0:31
 * Description: 导出excel
 * Version: 0.1.0
 */
package org.ddzj.utils.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

public class ExportExcel {

    public static void main(String[] args) {
        ExportExcel exportExcel = new ExportExcel();
        try {
            exportExcel.exportExcel2007ToPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * @description 导出2003版excle至某个路径
    * @since 0.1.0
    * @param   ；
    * @return void
    * @author ddzj
    * @修改人及修改内容
    */
    public void exportExcel2003ToPath(){
        String filePath = "";

    }

    /**
    * @description 导出2007版excle至某个路径
    * @since 0.1.0
    * @param   ；
    * @return void
    * @author ddzj
    * @修改人及修改内容
    */
    public void exportExcel2007ToPath() throws Exception {
//        保存文件的路径
        String filePath = "D:\\测试.xlsx";
//        创建xlsx格式文件
        Workbook workbook = new XSSFWorkbook();
//        创建名称为ddzj的工作薄
        Sheet ddzjSheet = workbook.createSheet("ddzj");

        /*样式*/
//        设置单元格样式
        CellStyle cellStyle = workbook.createCellStyle();
//        创建字体
        Font font = workbook.createFont();
//        字体高度
        font.setFontHeightInPoints((short)32);
//        字体名称
        font.setFontName("华文行楷");
//        粗体
        font.setBold(true);
//        颜色
        font.setColor((short) 999);
        cellStyle.setFont(font);

//        边框
        cellStyle.setBorderRight(BorderStyle.DOTTED);
        cellStyle.setBorderLeft(BorderStyle.DOUBLE);
//        单元格对齐方式-水平对齐
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
//        单元格对齐方式-垂直对齐
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

//        创建第3行
        Row row = ddzjSheet.createRow(2);
//        创建第3列
        Cell cell = row.createCell(3);
//        设置内容
        cell.setCellValue("大道至简");

//        合并单元格，从第3行-5行，第3列-5列
        int i = ddzjSheet.addMergedRegion(new CellRangeAddress(2, 4, 3, 5));

//        将样式应用给单元格
        cell.setCellStyle(cellStyle);

//        创建要写入具有指定名称的文件的文件输出流
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
//        将workbook写在输出流中
        workbook.write(fileOutputStream);
//        要关闭流
        fileOutputStream.close();
    }

    /**
     * @description 导出2003版excel为流
     * @since 0.1.0
     * @param   ；
     * @return void
     * @author ddzj
     * @修改人及修改内容
     */
    public void exportExcel2003ToSteam() throws Exception {

    }

    /**
    * @description 导出2007版excel为流
    * @since 0.1.0
    * @param   ；
    * @return void
    * @author ddzj
    * @修改人及修改内容
    */
    public void exportExcel2007ToSteam() throws Exception {

    }

    /**
     * @description 从模板导出2003版excel到某个路径
     * @since 0.1.0
     * @param   ；
     * @return void
     * @author ddzj
     * @修改人及修改内容
     */
    public void exportExcel2003FromTemplateToPath() throws Exception {

    }

    /**
    * @description 从模板导出2007版excel到某个路径
    * @since 0.1.0
    * @param   ；
    * @return void
    * @author ddzj
    * @修改人及修改内容
    */
    public void exportExcel2007FromTemplateToPath() throws Exception {

    }
}
