/**
 * Copyright (C), 2015-2020, 大道至简
 * FileName: MyExcelUtil
 * Author:   ddzj
 * Date:     2020/4/2 11:00
 * Description: excel工具类
 * Version: 0.1.0
 */
package org.ddzj.poi.excel;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import cn.hutool.poi.excel.cell.CellUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.ddzj.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/myExcelUtil")
public class MyExcelUtil extends BaseController {

    //        格式化统计
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private static ReportProperty reportProperty = null;
    private static List<Map<String, Object>> rows = new ArrayList<>();
    private static List<Object> _rows = new ArrayList<>();

    static {
        Map<String, Object> row = null;
        for (int i = 0; i < 100000; i++) {
            row = new LinkedHashMap<>();
            row.put("brch", "机构" + (int) Math.floor(i));
            row.put("rule", "规则规则规则规则规则规则规则规则规则" + i);
            row.put("alm_inf", i);
            row.put("content", "内容" + i);
            row.put("result", "结果" + i);
            row.put("gy", 0);
            row.put("fhy", 2);
//        row1.put("sqy", "授权员1");
            row.put("time", DateUtil.date());

            rows.add(row);
        }

        ReportProperty _reportProperty = null;
        for (int i = 0; i < 10000; i++) {
            _reportProperty = new ReportProperty();
            _reportProperty.setColFreezePane(i);
            _reportProperty.setRowFreezePane(i);
            _reportProperty.setResponse(null);
            _reportProperty.setStatDate("statDate");
            _reportProperty.setReportName("报表名称名称名称");
            _reportProperty.setDataList(null);
            _reportProperty.setExportType(1010);
            _reportProperty.setReportExportName("不知道");
            _reportProperty.setReportSavePath("就是路径");

            _rows.add(_reportProperty);
        }

        reportProperty = new ReportProperty();
        reportProperty.setExportType(1);
        reportProperty.setReportName("差错违规明细");
        reportProperty.setStatDate("2020-03-01 至 2020-03-31");
        reportProperty.setReportExportName("差错违规明细.xlsx");
        reportProperty.setReportSavePath("");
//        reportProperty.setReportSavePath("D:\\");
        reportProperty.setResponse(null);
        reportProperty.setDataList(rows);
        reportProperty.setColFreezePane(2);
        String[] fields = {"AA", "BB", "CC", "DD", "EE", "FF", "II", "HH"};
        reportProperty.setReportFields(fields);

    }

    /**
     * @param reportProperty 参数0；
     * @return boolean
     * @description 校验报表属性字段
     * @author ddzj
     * @修改人及修改内容
     * @since 0.1.0
     */
    public static boolean validateReportProperty(ReportProperty reportProperty) {
        if (null == reportProperty) {
            System.out.println("reportProperty对象为null，无法导出");
            return false;
        }
        if (StrUtil.isBlank(reportProperty.getReportName())) {
            System.out.println("ReportName属性为空，无法导出");
            return false;
        }
        if (StrUtil.isBlank(reportProperty.getReportSavePath())) {
            System.out.println("ReportSavePath属性为空，无法导出");
            return false;
        }
        if (StrUtil.isBlank(reportProperty.getStatDate())) {
            System.out.println("StatDate属性为空，无法导出");
            return false;
        }
        if (null == reportProperty.getDataList()) {
            System.out.println("DataList属性为空，无法导出");
            return false;
        }
        if (null == reportProperty.getReportFields() || reportProperty.getReportFields().length <= 0) {
            System.out.println("ReportFields属性字段有为空的，无法导出");
            return false;
        }
        if (StrUtil.isBlank(reportProperty.getReportExportName())) {
            System.out.println("ReportExportName属性字段有为空的，无法导出");
            return false;
        }

        return true;
    }


    /**
     * @param ；
     * @return void
     * @description 导出大数据量excel
     * @author ddzj
     * @修改人及修改内容
     * @since 0.2.0
     */
    @RequestMapping(value = "/exportBigExcel")
    public static void exportBigExcel() {
        if (validateReportProperty(reportProperty)) {
            long startTime = System.currentTimeMillis();

            ExcelWriter writer = null;
            try {
                //写表头字段
                String[] reportFields = reportProperty.getReportFields();
                int lastColumn = reportFields.length - 1;
                //通过工具类创建ExcelWriter
                writer = ExcelUtil.getBigWriter();
                writer.renameSheet(0, reportProperty.getReportName());
                writer.merge(0, 0, 0, lastColumn, reportProperty.getReportName(), true);
                writer.merge(1, 1, 0, lastColumn, "统计时间  " + reportProperty.getStatDate(), true);
//                writer.merge(1, 1, 2, lastColumn,reportProperty.getStatDate() , true);
                //表头单元格样式
                CellStyle headCellStyle = writer.getHeadCellStyle();
                headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                headCellStyle.setAlignment(HorizontalAlignment.CENTER);
                Font font = writer.createFont();
                font.setFontHeightInPoints((short) 16);
                font.setFontName("宋体");
                font.setBold(true);
                headCellStyle.setFont(font);

                CellStyle fieldCellStyle = writer.getCellStyle();
                fieldCellStyle.setFillForegroundColor(headCellStyle.getFillBackgroundColor());
                Font fieldFont = writer.createFont();
                font.setFontHeightInPoints((short) 16);
                font.setFontName("宋体");
                font.setBold(true);
                fieldCellStyle.setFont(fieldFont);

//                Row orCreateRow = writer.getOrCreateRow(2);
//                orCreateRow.setRowStyle(headCellStyle);
                //写表头字段
                for (int f = 0; f < reportFields.length; f++) {
                    Cell cell = writer.getOrCreateCell(f, 2);
                    cell.setCellStyle(fieldCellStyle);
                    cell.setCellValue(reportFields[f]);
                }

                //获取当前Sheet
                Sheet sheet = writer.getSheet();
                // cellNum:表示要冻结的列数；rowNum:表示要冻结的行数；firstCellNum:表示被固定列右边第一列的列号；firstRollNum :表示被固定行下边第一列的行号;
//            sheet.createFreezePane(1,3,6,8);
                //冻结2列3行
                sheet.createFreezePane(reportProperty.getColFreezePane(), reportProperty.getRowFreezePane());
                //过滤+排序
//            sheet.setAutoFilter(CellRangeAddress.valueOf("C3:D3"));
                //过滤第3行至第3行，第1列至最后1列
                sheet.setAutoFilter(new CellRangeAddress(2, 2, 0, lastColumn));

                //普通单元格样式
                CellStyle cellStyle = writer.getCellStyle();
                cellStyle.setAlignment(HorizontalAlignment.LEFT);
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                Font cellFont = writer.createFont();
                cellFont.setFontHeightInPoints((short) 14);
                cellFont.setFontName("宋体");
                cellStyle.setFont(cellFont);


                //设置当前列
                writer.setCurrentRow(3);
                List<Map<String, Object>> dataList = reportProperty.getDataList();
                if (dataList.size() > 0) {
                    writer.write(dataList, false);
                }


                //设置所有列为自动列宽，需在指定列数据写完后设置
//            writer.autoSizeColumnAll();
                //设置第2列为自动列宽，不考虑合并单元格
//           writer.autoSizeColumn(2);
                Map<Integer, Integer> cloumWidthMap = reportProperty.getCloumWidthMap();
                if (null != cloumWidthMap && cloumWidthMap.size() > 0) {
                    for (Integer cloum : cloumWidthMap.keySet()) {
                        //设置cloum列固定宽
                        writer.setColumnWidth(cloum, cloumWidthMap.get(cloum));
                    }
                }
                //默认行高
                writer.setDefaultRowHeight(20);


                if (1 == reportProperty.getExportType()) {
                    String reportSaveFullPath = reportProperty.getReportSavePath() + reportProperty.getReportExportName();
                    //需先删除
                    FileUtil.del(reportSaveFullPath);
                    writer.flush(FileUtil.touch(reportSaveFullPath));
                } else { //以流方式返回至客户端
                    //将流刷入文件并关闭流
                    HttpServletResponse response = reportProperty.getResponse();
                    response.setContentType("application/vnd.ms-excel;charset=utf-8");
                    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(reportProperty.getReportExportName(), "UTF-8"));
                    //将writer写入输出流
                    ServletOutputStream outputStream = response.getOutputStream();
                    writer.flush(outputStream, true);
                    //此处记得关闭输出Servlet流
                    IoUtil.close(outputStream);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                writer.close();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("~~~~~~~~~~~~~" + (endTime - startTime) + "ms");
        }
    }

    /**
     * @param
     * @return void
     * @description 从模板读取excel并写入到某个路径【注意，1.添加小计的目标列需要排序。2.计算占比所用到列需小计，且用到的列要在小计之后 3.小计目标列目前只支持1列】
     * 【报表设计 ：
     * 字段名&是否为小计目标（0否，1是）&（0不统计，1小计（包括合计），2占比（5-4-3 第5列计算占比，公式为4列/3列）
     * 字体 12 14 18
     * 行高 20
     * 表头 浅灰 冻结 筛选】
     * @author ddzj
     * @修改人及修改内容
     * @since 2.6.0
     */
//    @RequestMapping(value = "/exportExcelFromTemplate")
    public static void exportExcelFromTemplate() {
        long startTime = System.currentTimeMillis();
        ExcelReader reader = null;
        ExcelWriter writer = null;
        //传入参数
        int exportType = 1;
        String reportTemplateName = "差错违规明细-.xlsx";
        String reportExportName = reportTemplateName.replace("-", "[" + DateUtil.format(DateUtil.date(), DatePattern.NORM_DATE_PATTERN) + "]");
        String statDate = "2020-03-01 至 2020-03-31";
        HttpServletResponse response = getResponse();

        try {
            //模板位置
            String templatePath = FileUtil.getWebRoot() + File.separator + "resources" + File.separator + "reportTemplates" + File.separator + reportTemplateName;
            File file = FileUtil.file(templatePath);
            if (!file.exists()) {
                System.out.println("报表模板不存在：" + templatePath);
                return;
                //        return false;
            }

            //保存文件目标位置
            String destFilePath = "D:\\" + reportExportName;

            //将模板以流方式读成ExcelReader
            BufferedInputStream inputStream = FileUtil.getInputStream(templatePath);
            reader = ExcelUtil.getReader(inputStream);
            inputStream.close();

            //以文件方式读成ExcelReader
//            File destFile = FileUtil.copy(templatePath, destFilePath, true);
//            reader = ExcelUtil.getReader(destFile);

            int tempLastRowNum = reader.getPhysicalRowCount();
            //模板最后一行
            List<Object> tempLastRow = reader.readRow(tempLastRowNum - 1);

            //字段名
            List<String> fieldList = new LinkedList<>();
            //是否小计目标
            List<String> isSubtotalTargList = new LinkedList<>();
            //统计类型（0不统计，1小计（包括合计），2占比）
            List<String> statTypeList = new LinkedList<>();

            /** 字段名&是否为小计目标（0否，1是）&（0不统计，1小计（包括合计），2占比（2-4-3 给当前列添加占比，公式为4列/3列） **/
            for (int column = 0; column < tempLastRow.size(); column++) {
                String str = tempLastRow.get(column) + "";
                String[] columnStr = str.split("&");
                fieldList.add(columnStr[0]);
                isSubtotalTargList.add(columnStr[1]);
                statTypeList.add(columnStr[2]);
            }

            //将目标文件读取为ExcelWriter
//            writer = ExcelUtil.getWriter(destFile);
//            writer = ExcelUtil.getBigWriter(destFile);
            //从ExcelRead对象中获取ExcelWriter
            writer = reader.getWriter();

            //统计时间
            writer.writeCellValue(1, 1, statDate);
            //总数据行
            int totalDataRows = (null == rows ? 0 : rows.size());
            //总列数
            int totaColuNum = fieldList.size();

            //合计
            BigDecimal[] total = new BigDecimal[totaColuNum];
            //小计
            BigDecimal[] subtotal = new BigDecimal[totaColuNum];
            //是否有要小计和合计的列
            boolean statType_1Flag = (statTypeList.indexOf("1") > -1) ? true : false;
            //是否添加合计行标识位（如果有要合计的列，则留出一行给合计）
            boolean totaRowFlag = statType_1Flag;
            //临时单元格值
            String[] tempFieldValue = new String[totaColuNum];
            //当前行号
            int currRow = tempLastRowNum - 1;
            if (totalDataRows > 0) {
                for (int rowNum = 0; rowNum < totalDataRows; rowNum++) {
                    Map<String, Object> dataMap = rows.get(rowNum);
                    //每循环一次，当前行号增加一次(首次循环加1默认空出了合计行)
                    currRow++;
                    //如果不需要合计则减掉空出的合计行
                    if (!totaRowFlag) {
                        currRow--;
                        totaRowFlag = true;
                    }

                    for (int currColu = 0; currColu < totaColuNum; currColu++) {
                        //小计标识位
                        boolean subTotaFlag = false;
                        //单元格键
                        String fieldName = fieldList.get(currColu);
                        //单元格值
                        Object fieldValue = dataMap.get(fieldName);
                        //是否小计目标
                        String isSubtotaTarg = isSubtotalTargList.get(currColu);
                        //是否小计
                        String currStatType = statTypeList.get(currColu);

                        //有要小计的列
                        if (statType_1Flag) {
                            //是小计目标列且值不一样时表示需要添加小计（首行不比较）
                            if (0 != rowNum && "1".equals(isSubtotaTarg) && !tempFieldValue[currColu].equals(fieldValue + "")) {
                                for (int subColu = 0; subColu < totaColuNum; subColu++) {
                                    BigDecimal subValue = subtotal[subColu];
                                    // 统计类型为小计
                                    String _statType = statTypeList.get(subColu);
                                    Cell cell1 = writer.getCell(subColu, currRow, true);
                                    //当前列要小计，则设置值，否则设置空
                                    if ("1".equals(_statType)) {
                                        //保留2位
                                        BigDecimal _subValue = NumberUtil.round(subValue, 2);
                                        //去除小数后多余0
                                        cell1.setCellValue(NumberUtil.toStr(_subValue, "0"));
                                    } else if ((_statType.startsWith("2-"))) { //当前列为占比，设置占比格式
                                        String[] propStrs = _statType.split("-");
                                        if (3 == propStrs.length) {
                                            int aColNum = Integer.parseInt(propStrs[1]);
                                            int bColNum = Integer.parseInt(propStrs[2]);
                                            BigDecimal aColValue = subtotal[aColNum - 1];
                                            BigDecimal bColValue = subtotal[bColNum - 1];
                                            if (bColValue.compareTo(BigDecimal.ZERO) == 0) {
                                                cell1.setCellValue("0%");
                                            } else {
                                                //保留4位小数，转换成百分比后剩2位
                                                BigDecimal _fieldValue = NumberUtil.div(aColValue, bColValue, 4);
                                                cell1.setCellValue(NumberUtil.decimalFormat("#.##", NumberUtil.mul(_fieldValue, 100)) + "%");
                                            }
                                        } else {
                                            cell1.setCellValue("0%");
                                        }
                                    } else {
                                        //只在第一列小计（防止多次添加小计字样）
                                        if (0 == subColu) {
                                            cell1.setCellValue(tempFieldValue[0] + "（小计）");
                                        } else {
                                            cell1.setCellValue(" ");
                                        }
                                    }
                                }
                                subTotaFlag = true;
                                // 因小计占用一行，所以将已经读取的数据下移一行
                                currRow++;
                            }

                            //添加过小计后重置小计数组
                            if (subTotaFlag) {
                                subtotal = new BigDecimal[totaColuNum];
                            }

                            //当前统计类型是小计和合计
                            if ("1".equals(currStatType)) {
                                //给当前列累加小计（对非数字处理为默认值0）
                                subtotal[currColu] = NumberUtil.add(subtotal[currColu], NumberUtil.toBigDecimal(NumberUtil.isNumber(fieldValue + "") ? (fieldValue + "") : "0"));
                                //给当前列累加合计（对非数字处理为默认值0）
                                total[currColu] = NumberUtil.add(total[currColu], NumberUtil.toBigDecimal(NumberUtil.isNumber(fieldValue + "") ? (fieldValue + "") : "0"));
                            } else if (currStatType.startsWith("2-")) { //当前统计类型是占比(例：2-4-3，给当前列添加占比，计算方式为4列除3列)（因为未给当前的占比列设置值 ，所以一直为0）
                                String[] propStrs = currStatType.split("-");
                                if (3 == propStrs.length) {
                                    int aColNum = Integer.parseInt(propStrs[1]);
                                    int bColNum = Integer.parseInt(propStrs[2]);
                                    //非小计和合计行的占比需从临时存储的数据中取
                                    BigDecimal aColValue = NumberUtil.isNumber(tempFieldValue[aColNum - 1]) ? NumberUtil.toBigDecimal(tempFieldValue[aColNum - 1]) : new BigDecimal(0);
                                    BigDecimal bColValue = NumberUtil.isNumber(tempFieldValue[bColNum - 1]) ? NumberUtil.toBigDecimal(tempFieldValue[bColNum - 1]) : new BigDecimal(0);
                                    if (bColValue.compareTo(BigDecimal.ZERO) == 0) {
                                        fieldValue = "0%";
                                    } else {
                                        //保留4位小数，转换成百分比后剩2位
                                        BigDecimal _fieldValue = NumberUtil.div(aColValue, bColValue, 4);
                                        fieldValue = NumberUtil.decimalFormat("#.##", NumberUtil.mul(_fieldValue, 100)) + "%";
                                    }
                                } else {
                                    fieldValue = "0%";
                                }
                            }
                        }

                        //单元格赋值(x指列，y指行）
                        Cell cell = writer.getCell(currColu, currRow, true);
//                        cell.setCellStyle(getCellStyle(writer,0));
                        CellUtil.setCellValue(cell, fieldValue, null, false);
//                        CellUtil.setCellValue(cell, fieldValue, getStyleSet(writer,0), false);

                        //有要小计和合计的列，并且到最后一行且最后一列时，在最后一行添加小计，在空出的合计行添加合计，
                        if (statType_1Flag && rowNum == (totalDataRows - 1) && currColu == (totaColuNum - 1)) {
                            currRow++;
                            /** 添加小计行 **/
                            for (int subColu = 0; subColu < totaColuNum; subColu++) {
                                BigDecimal subValue = subtotal[subColu];
                                //统计类型
                                String _statType = statTypeList.get(subColu);
                                Cell cell1 = writer.getCell(subColu, currRow, true);

                                //当前列要小计，则设置值，否则设置空
                                if ("1".equals(_statType)) {
                                    //保留2位
                                    BigDecimal _subValue = NumberUtil.round(subValue, 2);
                                    //去除小数后多余0
                                    cell1.setCellValue(NumberUtil.toStr(_subValue, "0"));
                                } else if ((_statType.startsWith("2-"))) { //当前列为占比，设置占比格式
                                    String[] propStrs = _statType.split("-");
                                    if (3 == propStrs.length) {
                                        int aColNum = Integer.parseInt(propStrs[1]);
                                        int bColNum = Integer.parseInt(propStrs[2]);
                                        BigDecimal aColValue = subtotal[aColNum - 1];
                                        BigDecimal bColValue = subtotal[bColNum - 1];
                                        if (bColValue.compareTo(BigDecimal.ZERO) == 0) {
                                            cell1.setCellValue("0%");
                                        } else {
                                            //保留4位小数，转换成百分比后剩2位
                                            BigDecimal _fieldValue = NumberUtil.div(aColValue, bColValue, 4);
                                            cell1.setCellValue(NumberUtil.decimalFormat("#.##", NumberUtil.mul(_fieldValue, 100)) + "%");
                                        }
                                    } else {
                                        cell1.setCellValue("0%");
                                    }
                                } else {
                                    //只在第一列小计（防止多次添加小计字样）
                                    if (0 == subColu) {
                                        cell1.setCellValue(tempFieldValue[0] + "（小计）");
                                    } else {
                                        cell1.setCellValue(" ");
                                    }
                                }
                            }

                            /** 添加合计行 **/
                            for (int totaColu = 0; totaColu < totaColuNum; totaColu++) {
                                BigDecimal totalValue = total[totaColu];
                                //是否合计
                                String _statType = statTypeList.get(totaColu);
                                //在合计行添加
                                Cell cell2 = writer.getCell(totaColu, (tempLastRowNum - 1), true);
                                //当前列要合计，则设置值，否则设置空
                                if ("1".equals(_statType)) {
                                    //保留2位
                                    BigDecimal _totalValue = NumberUtil.round(totalValue, 2);
                                    //去除小数后多余0
                                    cell2.setCellValue(NumberUtil.toStr(_totalValue, "0"));
                                } else if ((_statType.startsWith("2-"))) { //当前列为占比，设置占比格式
                                    String[] propStrs = _statType.split("-");
                                    if (3 == propStrs.length) {
                                        int aColNum = Integer.parseInt(propStrs[1]);
                                        int bColNum = Integer.parseInt(propStrs[2]);
                                        BigDecimal aColValue = total[aColNum - 1];
                                        BigDecimal bColValue = total[bColNum - 1];
                                        if (bColValue.compareTo(BigDecimal.ZERO) == 0) {
                                            cell2.setCellValue("0%");
                                        } else {
                                            //保留4位小数，转换成百分比后剩2位
                                            BigDecimal _fieldValue = NumberUtil.div(aColValue, bColValue, 4);
                                            cell2.setCellValue(NumberUtil.decimalFormat("#.##", NumberUtil.mul(_fieldValue, 100)) + "%");
                                        }
                                    } else {
                                        cell2.setCellValue("0%");
                                    }
                                } else {
                                    //只在第一列合计（防止多次添加合计字样）
                                    if (0 == totaColu) {
                                        cell2.setCellValue("合计");
                                    } else {
                                        cell2.setCellValue(" ");
                                    }
                                }
                            }
                        }

                        //存储此列值，此备比较
                        tempFieldValue[currColu] = fieldValue + "";
                    }
                }
            } else {
                /** 当没有数据时，把最后一行清空 **/
                for (int c = 0; c < totaColuNum; c++) {
                    writer.writeCellValue(c, currRow, null);
                }
            }

            //释放内存
            tempLastRow = null;
            fieldList = null;
            isSubtotalTargList = null;
            statTypeList = null;
            total = null;
            subtotal = null;
            tempFieldValue = null;
            //rows = null;

            //以文件形式保存
            if (1 == exportType) {
                writer.flush(FileUtil.touch(destFilePath));
            } else { //以流方式返回至客户端
                //将流刷入文件并关闭流
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(reportExportName, "UTF-8"));
                //将writer写入输出流
                ServletOutputStream outputStream = response.getOutputStream();
                writer.flush(outputStream, true);
                //此处记得关闭输出Servlet流
                IoUtil.close(outputStream);
            }
        } catch (Exception e) {
            System.out.println("【生成报表异常】：Message=" + e.getMessage() + "；Cause=" + e.getCause());
            e.getStackTrace();
//            return false;
        } finally {
            //关闭流，释放内存
            writer.close();
            reader.close();
        }
//        return true;
        long endTime = System.currentTimeMillis();
        System.out.println("~~~~~~~~~~~~~~~~~~用时" + (endTime - startTime) + "ms");
    }

    /**
     * @param writer ExcelWriter对象；
     * @return org.apache.poi.ss.usermodel.CellStyle
     * @description 获取单元格样式
     * @author ddzj
     * @修改人及修改内容
     * @since 0.1.0
     */
    private StyleSet getStyleSet(ExcelWriter writer, int styleType) {
//    private StyleSet getCellStyle(ExcelWriter writer,int styleType) {
        if (null != writer) {
            StyleSet styleSet = writer.getStyleSet();
            CellStyle cellStyle = styleSet.getCellStyle();
            Font font = writer.createFont();
            switch (styleType) {
                case 1: //统计时间
                    //水平对齐方式
                    cellStyle.setAlignment(HorizontalAlignment.CENTER);
                    //垂直对方方式
                    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    //字体
                    font.setFontName("宋体");
                    //字体大小
                    font.setFontHeightInPoints((short) 14);
                    cellStyle.setFont(font);
                    break;
                default: //默认普通单元格
                    //水平对齐方式
                    cellStyle.setAlignment(HorizontalAlignment.LEFT);
                    //垂直对方方式
                    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    //字体
                    font.setFontName("宋体");
                    //字体大小
                    font.setFontHeightInPoints((short) 12);
                    cellStyle.setFont(font);
                    break;
            }

            //writer.close(); //不能关，由调用方法关闭
            return styleSet;
        } else {
            return null;
        }
    }
}

