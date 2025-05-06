package com.example.xxxmes.entity;

import java.util.Date;

public class ProductionTask {
    private int taskId;         // 任务ID
    private int productId;      // 产品ID
    private String batchNo;     // 批号
    private String status;       // 状态
    private Date startTime;     // 开始时间
    private Date endTime;       // 结束时间
    private String productName;  // 产品名称
    private String specifications; // 规格
    private String material;     // 材料
    private double unitWeight;    // 单位重量

    public ProductionTask() {
    }

    public ProductionTask(int taskId, int productId, String batchNo, String status, Date startTime, Date endTime) {
        this.taskId = taskId;
        this.productId = productId;
        this.batchNo = batchNo;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
}
