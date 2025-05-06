package com.example.xxxmes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.xxxmes.utils.JDBCUtils;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class ReportActivity extends AppCompatActivity {

    private EditText processIdInput;
    private EditText workerNameInput;
    private EditText stationNameInput;
    private EditText taskNumberInput;
    private EditText productIdInput;
    private EditText productNameInput;
    private EditText specificationsInput;
    private EditText materialInput;
    private EditText unitWeightInput;
    private EditText inputQuantityInput;
    private EditText outputQuantityInput;
    private EditText scrapQuantityInput;

    private int receivedEmployeeId;
    private int receivedTaskId;

    private Connection connection;
    public static final String TAG = "ReportActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        receivedEmployeeId = getIntent().getIntExtra("EMPLOYEE_ID", -1);
        receivedTaskId = getIntent().getIntExtra("TASK_ID", -1);

        initializeViews();

        if (receivedEmployeeId!= -1) {
            fetchEmployeeData();
        }

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> submitReport());

        ImageButton scanButton = findViewById(R.id.scanButton);
        scanButton.setOnClickListener(v -> startScan());

        if (receivedTaskId!= -1) {
            taskNumberInput.setText(String.valueOf(receivedTaskId));
        }
    }

    private void initializeViews() {
        processIdInput = findViewById(R.id.processIdInput);
        workerNameInput = findViewById(R.id.workerNameInput);
        stationNameInput = findViewById(R.id.stationNameInput);
        taskNumberInput = findViewById(R.id.taskNumberInput);
        productIdInput = findViewById(R.id.productIDInput);
        productNameInput = findViewById(R.id.productNameInput);
        specificationsInput = findViewById(R.id.specificationsInput);
        materialInput = findViewById(R.id.materialInput);
        unitWeightInput = findViewById(R.id.unitWeightInput);
        inputQuantityInput = findViewById(R.id.inputQuantityInput);
        outputQuantityInput = findViewById(R.id.outputQuantityInput);
        scrapQuantityInput = findViewById(R.id.scrapQuantityInput);
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents()!= null) {
                    String[] scannedData = result.getContents().split(",");
                    if (scannedData.length >= 5) {
                        productIdInput.setText(scannedData[0].trim());
                        productNameInput.setText(scannedData[1].trim());
                        specificationsInput.setText(scannedData[2].trim());
                        materialInput.setText(scannedData[3].trim());
                        unitWeightInput.setText(scannedData[4].trim());
                    } else {
                        Log.e(TAG, "扫描的数据格式不正确");
                    }
                }
            });

    private void startScan() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("请扫描二维码");
        options.setBeepEnabled(true);
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(PortraitCaptureActivity.class);
        barcodeLauncher.launch(options);
    }

    private void fetchEmployeeData() {
        new Thread(() -> {
            try {
                connection = JDBCUtils.getConn();
                String sql = "SELECT ProcessID, Name, Position FROM Employee WHERE EmployeeID =?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setInt(1, receivedEmployeeId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            final String processId = rs.getString("ProcessID");
                            final String employeeName = rs.getString("Name");
                            final String position = rs.getString("Position");

                            runOnUiThread(() -> {
                                processIdInput.setText(processId);
                                workerNameInput.setText(employeeName);
                                stationNameInput.setText(position);
                            });
                        }
                    }
                } catch (SQLException e) {
                    Log.e(TAG, "查询员工信息失败", e);
                }
            } finally {
                closeConnection();
            }
        }).start();
    }

    private void submitReport() {
        String processId1 = processIdInput.getText().toString().trim();
        String taskNumber = taskNumberInput.getText().toString().trim();
        String productId = productIdInput.getText().toString().trim();
        int outputQuantity;

        // 输出获取的 productId 以供调试
        Log.d(TAG, "获取的 productId: " + productId);

        try {
            outputQuantity = Integer.parseInt(outputQuantityInput.getText().toString().trim());
        } catch (NumberFormatException e) {
            Log.e(TAG, "数量输入不正确", e);
            return;
        }

        new Thread(() -> {
            try {
                connection = JDBCUtils.getConn();
                connection.setAutoCommit(false);

                int productionTaskProductId = getProductionTaskProductId(taskNumber);
                // 将获取的 productId 转换为 int 类型
                int productIdInt = Integer.parseInt(productId);
                int processId2 = getNextProcessId(processId1, productionTaskProductId);

                Log.d(TAG, "processId1: " + processId1 + ", productionTaskProductId: " + productionTaskProductId);
                Log.d(TAG, "processId2: " + processId2);

                if (processId2 == 0) {
                    Log.e(TAG, "未找到有效的下一个工序");
                    return;
                }

                // 插入到 report 表
                insertReportData(productIdInt, processId1, outputQuantity);

                insertProcessHandoverData(processId1, processId2, productIdInt, outputQuantity, receivedEmployeeId);
                updateProductionTask(taskNumber, outputQuantity);


                connection.commit();
                Log.d(TAG, "报告数据成功插入到 Report 表中，任务状态更新为 Completed");
            } catch (SQLException e) {
                Log.e(TAG, "数据插入失败", e);
                rollbackTransaction();
            } finally {
                closeConnection();
            }
        }).start();
    }

    private void insertReportData(int productId, String processId, int outputQuantity) throws SQLException {
        String sql = "INSERT INTO reportwork (ProductID, ProcessID, WorkerName, StationName, TaskID, ProductName, Specifications, Material, UnitWeight, InputQuantity, OutputQuantity, ScrapQuantity) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setString(2, processId);
            ps.setString(3, workerNameInput.getText().toString().trim());
            ps.setString(4, stationNameInput.getText().toString().trim());
            ps.setString(5, taskNumberInput.getText().toString().trim());
            ps.setString(6, productNameInput.getText().toString().trim());
            ps.setString(7, specificationsInput.getText().toString().trim());
            ps.setString(8, materialInput.getText().toString().trim());

            // 处理 UnitWeight 输入
            String unitWeightText = unitWeightInput.getText().toString().trim();
            ps.setBigDecimal(9, unitWeightText.isEmpty() ? null : new BigDecimal(unitWeightText));

            ps.setInt(10, Integer.parseInt(inputQuantityInput.getText().toString().trim()));
            ps.setInt(11, outputQuantity);
            ps.setInt(12, Integer.parseInt(scrapQuantityInput.getText().toString().trim()));

            ps.executeUpdate();
        }
    }


    private void insertProcessHandoverData(String processId1, int processId2, int productId, int outputQuantity, int operatorId) throws SQLException {
        String sql = "INSERT INTO ProcessHandOver (SourceProcessID, TargetProcessID, ProductID, HandOverDate, Quantity, Status, OperatorID) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, processId1);
            ps.setInt(2, processId2);
            ps.setInt(3, productId);
            ps.setTimestamp(4, new Timestamp(new Date().getTime()));
            ps.setInt(5, outputQuantity);
            ps.setString(6, "Completed");
            ps.setInt(7, operatorId);
            ps.executeUpdate();
        }
    }

    private int getProductionTaskProductId(String taskNumber) throws SQLException {
        String sql = "SELECT ProductID FROM ProductionTask WHERE TaskID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, taskNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ProductID");
                }
            }
        }
        return -1;
    }

    private int getNextProcessId(String processId1, int productionTaskProductId) throws SQLException {
        String sql = "SELECT ProcessID FROM Process WHERE ProductID =? AND ProcessID >? ORDER BY ProcessID ASC LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, productionTaskProductId);
            ps.setString(2, processId1);
            Log.d(TAG, "SQL: " + sql + ", ProductID: " + productionTaskProductId + ", ProcessID: " + processId1);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ProcessID"); // 注意这里应为 ProcessID
                }
            }
        }
        return 0;
    }


    private void updateProductionTask(String taskNumber, int outputQuantity) throws SQLException {
        // 先获取当前时间戳
        Timestamp currentTimeStamp = new Timestamp(new Date().getTime());

        // 更新当前任务的EndTime和Status
        String sqlUpdateTask = "UPDATE ProductionTask SET EndTime =?, Status = 'Completed' WHERE TaskID =?";
        try (PreparedStatement psUpdateTask = connection.prepareStatement(sqlUpdateTask)) {
            psUpdateTask.setTimestamp(1, currentTimeStamp);
            psUpdateTask.setString(2, taskNumber);
            psUpdateTask.executeUpdate();
        }

        // 获取上一条相同productId的BatchNo
        String batchNo = getPreviousBatchNo(productIdInput.getText().toString().trim());

        // 在ProductionTask表中新增一条数据
        String sqlInsertNewTask = "INSERT INTO ProductionTask (ProductID, BatchNo, Status, StartTime, EndTime, ProcessID) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement psInsertNewTask = connection.prepareStatement(sqlInsertNewTask)) {
            psInsertNewTask.setInt(1, Integer.parseInt(productIdInput.getText().toString().trim()));
            psInsertNewTask.setString(2, batchNo);
            psInsertNewTask.setString(3, "In Progress");
            psInsertNewTask.setTimestamp(4, currentTimeStamp); // 开始时间
            psInsertNewTask.setTimestamp(5, null); // 将结束时间设置为 null
            psInsertNewTask.setInt(6, getNextProcessId(processIdInput.getText().toString().trim(), Integer.parseInt(productIdInput.getText().toString().trim())));
            psInsertNewTask.executeUpdate();
        }

    }

    private String getPreviousBatchNo(String productId) throws SQLException {
        String sql = "SELECT BatchNo FROM ProductionTask WHERE ProductID =? ORDER BY TaskID DESC LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("BatchNo");
                }
            }
        }
        return "";
    }


    private void rollbackTransaction() {
        try {
            if (connection!= null) {
                connection.rollback();
                Log.d(TAG, "回滚事务");
            }
        } catch (SQLException e) {
            Log.e(TAG, "事务回滚失败", e);
        }
    }

    private void closeConnection() {
        if (connection!= null) {
            try {
                connection.close();
            } catch (SQLException e) {
                Log.e(TAG, "关闭数据库连接失败", e);
            }
        }
    }
}