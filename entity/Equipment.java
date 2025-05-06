package com.example.xxxmes.entity;

public class Equipment {

    private int equipmentId;        // 设备ID
    private String name;            // 设备名称
    private String status;          // 状态
    private String qrCode;          // 二维码

    public Equipment() {
    }

    public Equipment(int equipmentId, String name, String status, String qrCode) {
        this.equipmentId = equipmentId;
        this.name = name;
        this.status = status;
        this.qrCode = qrCode;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
