package com.example.xxxmes.entity;

public class ProcessHandOver {

    private long handOverId;      // HandOverID
    private int sourceProcessId;  // SourceProcessID
    private int targetProcessId;  // TargetProcessID
    private String handOverDate;   // HandOverDate
    private int quantity;          // Quantity
    private String status;         // Status
    private int operatorId;        // OperatorID
    private String remarks;        // Remarks
    private int productId;         // ProductID

    public ProcessHandOver() {
    }

    public ProcessHandOver(long handOverId, int sourceProcessId, int targetProcessId, String handOverDate,
                           int quantity, String status, int operatorId, String remarks, int productId) {
        this.handOverId = handOverId;
        this.sourceProcessId = sourceProcessId;
        this.targetProcessId = targetProcessId;
        this.handOverDate = handOverDate;
        this.quantity = quantity;
        this.status = status;
        this.operatorId = operatorId;
        this.remarks = remarks;
        this.productId = productId;
    }

    public long getHandOverId() {
        return handOverId;
    }

    public void setHandOverId(long handOverId) {
        this.handOverId = handOverId;
    }

    public int getSourceProcessId() {
        return sourceProcessId;
    }

    public void setSourceProcessId(int sourceProcessId) {
        this.sourceProcessId = sourceProcessId;
    }

    public int getTargetProcessId() {
        return targetProcessId;
    }

    public void setTargetProcessId(int targetProcessId) {
        this.targetProcessId = targetProcessId;
    }

    public String getHandOverDate() {
        return handOverDate;
    }

    public void setHandOverDate(String handOverDate) {
        this.handOverDate = handOverDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
