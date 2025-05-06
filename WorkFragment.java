package com.example.xxxmes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.xxxmes.utils.JDBCUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WorkFragment extends Fragment {

    private LinearLayout tasksContainer;
    private LinearLayout equipmentContainer;
    private int employeeId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.work_fragment, container, false);

        tasksContainer = view.findViewById(R.id.tasksContainer);
        equipmentContainer = view.findViewById(R.id.equipmentContainer);
        Button scanQRCodeButton = view.findViewById(R.id.scanQRCodeButton);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        employeeId = sharedPreferences.getInt("employee_id", -1);

        loadTodayTasks();
        loadIdleEquipment();

        scanQRCodeButton.setOnClickListener(v -> startQRCodeScanner());

        return view;
    }

    // 加载当天任务
// 加载当天任务
    private void loadTodayTasks() {
        new Thread(() -> {
            String sql = "SELECT pt.* FROM productiontask pt " +
                    "JOIN employee e ON pt.ProcessID = e.ProcessID " +
                    "WHERE e.EmployeeID = ? AND pt.Status = 'In Progress' " +
                    "AND DATE(pt.StartTime) = CURDATE()";
            try (Connection connection = JDBCUtils.getConn();
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, employeeId); // 将员工ID作为参数传入
                try (ResultSet rs = ps.executeQuery()) {
                    List<Task> taskList = new ArrayList<>();
                    while (rs.next()) {
                        int taskId = rs.getInt("TaskID");
                        String batchNo = rs.getString("BatchNo");
                        taskList.add(new Task(taskId, batchNo));
                    }

                    getActivity().runOnUiThread(() -> {
                        tasksContainer.removeAllViews();
                        for (Task task : taskList) {
                            LinearLayout taskLayout = new LinearLayout(getContext());
                            taskLayout.setOrientation(LinearLayout.VERTICAL);

                            // 添加任务按钮
                            Button taskButton = new Button(getContext());
                            taskButton.setText("任务ID: " + task.getTaskId() + ", 批次号: " + task.getBatchNo());
                            taskButton.setOnClickListener(v -> openReportActivity(task.getTaskId()));
                            taskLayout.addView(taskButton);

                            // 添加绿色的“领料”按钮
                            Button receiveMaterialButton = new Button(getContext());
                            receiveMaterialButton.setText("领料");
                            receiveMaterialButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), android.R.color.holo_green_light));
                            receiveMaterialButton.setOnClickListener(v -> openReceiveMaterialActivity(task.getTaskId()));
                            taskLayout.addView(receiveMaterialButton);

                            tasksContainer.addView(taskLayout);
                        }
                    });
                }
            } catch (SQLException e) {
                Log.e("WorkFragment", "加载任务时出错: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }


    // 加载空闲设备
    private void loadIdleEquipment() {
        new Thread(() -> {
            String sql = "SELECT e.EquipmentID, e.Name FROM Equipment e " +
                    "JOIN Process p ON p.EquipmentID = e.EquipmentID " +
                    "JOIN Employee em ON em.ProcessID = p.ProcessID " +
                    "WHERE em.EmployeeID = ? AND CONVERT(e.Status USING latin1) = 'Idle'";

            try (Connection connection = JDBCUtils.getConn();
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, employeeId);

                try (ResultSet rs = ps.executeQuery()) {
                    List<String> equipmentList = new ArrayList<>();
                    while (rs.next()) {
                        int equipmentId = rs.getInt("EquipmentID");
                        String equipmentName = rs.getString("Name");
                        equipmentList.add("设备ID: " + equipmentId + ", 名称: " + equipmentName);
                    }

                    getActivity().runOnUiThread(() -> {
                        equipmentContainer.removeAllViews();
                        if (!equipmentList.isEmpty()) {
                            for (String equipmentInfo : equipmentList) {
                                TextView equipmentText = new TextView(getContext());
                                equipmentText.setText(equipmentInfo);
                                equipmentContainer.addView(equipmentText);
                            }
                        } else {
                            TextView noEquipmentText = new TextView(getContext());
                            noEquipmentText.setText("没有空闲设备");
                            equipmentContainer.addView(noEquipmentText);
                        }
                    });
                }
            } catch (SQLException e) {
                Log.e("WorkFragment", "加载空闲设备时出错: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    // 启动二维码扫描
    private void startQRCodeScanner() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("扫描设备二维码");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String equipmentId = result.getContents();
            recordEquipmentTime(equipmentId);
        }
    }

    // 记录设备的开始或结束时间
    private void recordEquipmentTime(String equipmentId) {
        new Thread(() -> {
            try (Connection connection = JDBCUtils.getConn()) {
                String checkSql = "SELECT StartTime, EndTime FROM equipment WHERE EquipmentID = ?";
                try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                    checkStmt.setString(1, equipmentId);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        String updateSql;
                        String newStatus;
                        if (rs.getTimestamp("StartTime") == null) {
                            updateSql = "UPDATE equipment SET StartTime = ?, Status = ? WHERE EquipmentID = ?";
                            newStatus = "Working";
                        } else {
                            updateSql = "UPDATE equipment SET EndTime = ?, Status = ? WHERE EquipmentID = ?";
                            newStatus = "Idle";
                        }
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                            updateStmt.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
                            updateStmt.setString(2, newStatus);
                            updateStmt.setString(3, equipmentId);
                            updateStmt.executeUpdate();
                        }
                    }
                }
            } catch (SQLException e) {
                Log.e("WorkFragment", "更新设备时间失败: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    private void openReportActivity(int taskId) {
        Intent intent = new Intent(getActivity(), ReportActivity.class);
        intent.putExtra("TASK_ID", taskId);
        intent.putExtra("EMPLOYEE_ID", employeeId);  // 传递员工ID
        startActivity(intent);
    }

    private void openReceiveMaterialActivity(int taskId) {
        Intent intent = new Intent(getActivity(), MaterialActivity.class);
        intent.putExtra("TASK_ID", taskId);
        intent.putExtra("EMPLOYEE_ID", employeeId);  // 传递员工ID
        startActivity(intent);
    }

    private static class Task {
        private final int taskId;
        private final String batchNo;

        public Task(int taskId, String batchNo) {
            this.taskId = taskId;
            this.batchNo = batchNo;
        }

        public int getTaskId() {
            return taskId;
        }

        public String getBatchNo() {
            return batchNo;
        }
    }
}
