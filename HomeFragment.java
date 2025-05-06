package com.example.xxxmes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.xxxmes.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomeFragment extends Fragment {

    private DatePicker datePicker;
    private Button searchButton;
    private TextView resultTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);

        datePicker = view.findViewById(R.id.datePicker);
        searchButton = view.findViewById(R.id.searchButton);
        resultTextView = view.findViewById(R.id.resultTextView);

        searchButton.setOnClickListener(v -> performSearch());

        return view;
    }

    private void performSearch() {
        int year = datePicker.getYear();
        int month = datePicker.getMonth() + 1; // 月份从 0 开始
        int day = datePicker.getDayOfMonth();

        // 格式化日期为 YYYY-MM-DD
        String selectedDate = String.format("%04d-%02d-%02d", year, month, day);
        Log.d("HomeFragment", "Selected date: " + selectedDate);

        // 从 SharedPreferences 中获取员工ID
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        int employeeId = sharedPreferences.getInt("employee_id", -1);
        Log.d("HomeFragment", "Employee ID: " + employeeId);

        // 在子线程中执行数据库查询
        new Thread(() -> {
            try (Connection connection = JDBCUtils.getConn()) {
                if (connection != null) {
                    // 查询员工的 ProcessID
                    String processIdQuery = "SELECT ProcessID FROM employee WHERE EmployeeID = ?";
                    try (PreparedStatement processStmt = connection.prepareStatement(processIdQuery)) {
                        processStmt.setInt(1, employeeId);
                        ResultSet processRs = processStmt.executeQuery();
                        if (processRs.next()) {
                            int processId = processRs.getInt("ProcessID");
                            Log.d("HomeFragment", "Process ID: " + processId);

                            // 查询指定日期和对应 ProcessID 的任务
                            String productionTaskQuery = "SELECT * FROM productiontask WHERE StartTime BETWEEN ? AND ? AND ProcessID = ?";
                            String startTime = selectedDate + " 00:00:00"; // 开始时间
                            String endTime = selectedDate + " 23:59:59";   // 结束时间

                            try (PreparedStatement taskStmt = connection.prepareStatement(productionTaskQuery)) {
                                taskStmt.setString(1, startTime);
                                taskStmt.setString(2, endTime);
                                taskStmt.setInt(3, processId);

                                ResultSet productionTaskRs = taskStmt.executeQuery();
                                StringBuilder result = new StringBuilder();

                                // 处理 productiontask 表查询结果
                                while (productionTaskRs.next()) {
                                    int taskId = productionTaskRs.getInt("TaskID");
                                    String batchNo = productionTaskRs.getString("BatchNo");
                                    String status = productionTaskRs.getString("Status");
                                    String startTimeDB = productionTaskRs.getString("StartTime");
                                    String endTimeDB = productionTaskRs.getString("EndTime");
                                    result.append("任务ID: ").append(taskId)
                                            .append(", 批次号: ").append(batchNo)
                                            .append(", 状态: ").append(status)
                                            .append(", 开始时间: ").append(startTimeDB)
                                            .append(", 结束时间: ").append(endTimeDB)
                                            .append("\n");
                                }

                                // 在主线程中更新UI
                                updateUI(result.toString());
                            }
                        } else {
                            Log.d("HomeFragment", "No ProcessID found for EmployeeID: " + employeeId);
                        }
                    }
                } else {
                    Log.e("HomeFragment", "Database connection failed");
                }
            } catch (SQLException e) {
                Log.e("HomeFragment", "SQL Exception: " + e.getMessage());
            }
        }).start();
    }


    private void updateUI(String result) {
        getActivity().runOnUiThread(() -> resultTextView.setText(result));
    }
}
