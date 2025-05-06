package com.example.xxxmes.entity;

public class Process {

    private int processId;
    private String name;
    private int equipmentId;
    private String description;
    private int productId; // 新增的字段

    public Process() {
    }

    public Process(int processId, String name, int equipmentId, String description, int productId) {
        this.processId = processId;
        this.name = name;
        this.equipmentId = equipmentId;
        this.description = description;
        this.productId = productId; // 初始化新增字段
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProductId() {
        return productId; // 新增的 getter 方法
    }

    public void setProductId(int productId) {
        this.productId = productId; // 新增的 setter 方法
    }
}
