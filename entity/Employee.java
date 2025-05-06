package com.example.xxxmes.entity;

public class Employee {

    private int employeeId;
    private String name;
    private String position;
    private String password;
    private int experience;
    private int processId; // 新增 ProcessID 字段

    public Employee() {
    }

    public Employee(int employeeId, String name, String position, String password, int experience, int processId) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
        this.password = password;
        this.experience = experience;
        this.processId = processId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }
}
