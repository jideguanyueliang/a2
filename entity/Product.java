package com.example.xxxmes.entity;

public class Product {
    private int productId;               // 产品ID
    private String name;                  // 产品名称
    private String specifications;        // 规格
    private String material;              // 材料
    private double unitWeight;            // 单位重量

    public Product() {
    }

    public Product(int productId, String name, String specifications, String material, double unitWeight) {
        this.productId = productId;
        this.name = name;
        this.specifications = specifications;
        this.material = material;
        this.unitWeight = unitWeight;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
