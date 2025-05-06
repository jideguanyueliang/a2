package com.example.xxxmes.entity;

public class Report {
    private int reportID;        // ReportID
    private int productID;       // ProductID
    private int processID;       // ProcessID
    private int taskID;          // TaskID
    private String workerName;   // 报工人员
    private String stationName;   // 工位名称
    private String productName;   // 产品名称
    private String specifications; // 规格
    private String material;      // 材料
    private double unitWeight;    // 单位重量
    private int inputQuantity;    // InputQuantity
    private int outputQuantity;   // OutputQuantity
    private int scrapQuantity;    // ScrapQuantity
    private String remarks;       // 备注

    // 默认构造函数
    public Report() {
    }

    // 带参数的构造函数
    public Report(int reportID, int productID, int processID, int taskID, String workerName, String stationName,
                  String productName, String specifications, String material, double unitWeight,
                  int inputQuantity, int outputQuantity, int scrapQuantity, String remarks) {
        this.reportID = reportID;
        this.productID = productID;
        this.processID = processID;
        this.taskID = taskID;
        this.workerName = workerName;
        this.stationName = stationName;
        this.productName = productName;
        this.specifications = specifications;
        this.material = material;
        this.unitWeight = unitWeight;
        this.inputQuantity = inputQuantity;
        this.outputQuantity = outputQuantity;
        this.scrapQuantity = scrapQuantity;
        this.remarks = remarks;
    }

    // Getter 和 Setter 方法
    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getProcessID() {
        return processID;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public double getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(double unitWeight) {
        this.unitWeight = unitWeight;
    }

    public int getInputQuantity() {
        return inputQuantity;
    }

    public void setInputQuantity(int inputQuantity) {
        this.inputQuantity = inputQuantity;
    }

    public int getOutputQuantity() {
        return outputQuantity;
    }

    public void setOutputQuantity(int outputQuantity) {
        this.outputQuantity = outputQuantity;
    }

    public int getScrapQuantity() {
        return scrapQuantity;
    }

    public void setScrapQuantity(int scrapQuantity) {
        this.scrapQuantity = scrapQuantity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
