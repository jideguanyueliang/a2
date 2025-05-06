package com.example.xxxmes.models;

public class Material {
    private int materialId;      // 物料ID
    private String name;         // 物料名称
    private String type;         // 物料类型
    private double unitPrice;    // 单价
    private int employeeId;      // 员工ID
    private int totalQuantity;    // 总数
    private int receivedQuantity; // 领取数量

    // 构造方法
    public Material(int materialId, String name, String type, double unitPrice, int employeeId, int totalQuantity, int receivedQuantity) {
        this.materialId = materialId;
        this.name = name;
        this.type = type;
        this.unitPrice = unitPrice;
        this.employeeId = employeeId;
        this.totalQuantity = totalQuantity;
        this.receivedQuantity = receivedQuantity;
    }

    // Getter 和 Setter 方法
    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(int receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }
}
