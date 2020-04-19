/**
 * Copyright (C), 2015-2020, yourchoice
 * FileName: ReportProperty
 * Author:   ddzj
 * Date:     2020/4/6 17:02
 * Description: 报表属性
 * Version: 0.2.0
 */
package org.ddzj.poi.excel;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class ReportProperty {
    //导出类型
    private int exportType = 0;
    //报表名称
    private String reportName;
    //报表导出名称
    private String reportExportName;
    //保存目标文件的全路径名(D:\\报表\\)
    private String reportSavePath;
    //统计时间
    private String statDate;
    //报表字段
    private String[] reportFields;
    //响应（用于给客户端返回流）
    private HttpServletResponse response;
    //数据集合
    private List<Map<String, Object>> dataList;
//    private List<Object> dataList;
    //列宽属性
    private Map<Integer,Integer> cloumWidthMap;
    //从0行开始，冻结n行
    private int rowFreezePane = 3;
    //从0列开始，冻结n列
    private int colFreezePane = 0;

    public int getExportType() {
        return exportType;
    }

    public void setExportType(int exportType) {
        this.exportType = exportType;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportExportName() {
        return reportExportName;
    }

    public void setReportExportName(String reportExportName) {
        if(StrUtil.isBlank(reportExportName)){
            reportExportName = this.reportName+"[" + DateUtil.format(DateUtil.date(), DatePattern.NORM_DATE_PATTERN) + "].xlxs";
        }
        this.reportExportName = reportExportName;
    }

    public String getReportSavePath() {
        return reportSavePath;
    }

    public void setReportSavePath(String reportSavePath) {
        if(StrUtil.isBlank(reportSavePath)){
            reportSavePath = "D:\\reportDir\\";
        }
        this.reportSavePath = reportSavePath;
    }

    public String[] getReportFields() {
        return reportFields;
    }

    public void setReportFields(String[] reportFields) {
        this.reportFields = reportFields;
    }

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public List<Map<String, Object>> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    public Map<Integer, Integer> getCloumWidthMap() {
        return cloumWidthMap;
    }

    public void setCloumWidthMap(Map<Integer, Integer> cloumWidthMap) {
        this.cloumWidthMap = cloumWidthMap;
    }

    public int getRowFreezePane() {
        return rowFreezePane;
    }

    public void setRowFreezePane(int rowFreezePane) {
        this.rowFreezePane = rowFreezePane;
    }

    public int getColFreezePane() {
        return colFreezePane;
    }

    public void setColFreezePane(int colFreezePane) {
        this.colFreezePane = colFreezePane;
    }
}
