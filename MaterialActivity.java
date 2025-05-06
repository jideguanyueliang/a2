package com.example.xxxmes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.xxxmes.utils.JDBCUtils;
import com.journeyapps.barcodescanner.CaptureActivity;

import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MaterialActivity extends AppCompatActivity {

    private int taskId;
    private int employeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);

        Intent intent = getIntent();
        taskId = intent.getIntExtra("TASK_ID", -1);
        employeeId = intent.getIntExtra("EMPLOYEE_ID", -1); // 接收员工ID

        TextView employeeIdTextView = findViewById(R.id.employeeIdTextView);
        employeeIdTextView.setText("员工ID: " + employeeId); // 设置员工ID文本

        ImageView scanQRCodeImageView = findViewById(R.id.scanQRCodeImageView);
        scanQRCodeImageView.setOnClickListener(v -> {
            Intent scanIntent = new Intent(this, CaptureActivity.class); // CaptureActivity 是扫描二维码的活动
            startActivityForResult(scanIntent, 100); // 100 是请求码
        });

        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText typeEditText = findViewById(R.id.typeEditText);
        EditText unitPriceEditText = findViewById(R.id.unitPriceEditText);
        EditText totalQuantityEditText = findViewById(R.id.totalQuantityEditText);
        EditText receivedQuantityEditText = findViewById(R.id.receivedQuantityEditText);
        Button submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String type = typeEditText.getText().toString().trim();
            String unitPriceString = unitPriceEditText.getText().toString().trim();
            String totalQuantityString = totalQuantityEditText.getText().toString().trim();
            String receivedQuantityString = receivedQuantityEditText.getText().toString().trim();

            if (name.isEmpty() || type.isEmpty() || unitPriceString.isEmpty() ||
                    totalQuantityString.isEmpty() || receivedQuantityString.isEmpty()) {
                Toast.makeText(MaterialActivity.this, "请填写所有字段", Toast.LENGTH_SHORT).show();
            } else {
                double unitPrice = Double.parseDouble(unitPriceString);
                int totalQuantity = Integer.parseInt(totalQuantityString);
                int receivedQuantity = Integer.parseInt(receivedQuantityString);
                insertMaterial(name, type, unitPrice, employeeId, totalQuantity, receivedQuantity);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            String result = data.getStringExtra("SCAN_RESULT"); // 获取扫描结果
            populateFields(result); // 填充数据
        }
    }

    private void populateFields(String result) {
        // 假设结果格式为 "name,type,unitPrice,totalQuantity"
        String[] parts = result.split(",");
        if (parts.length == 4) {
            EditText nameEditText = findViewById(R.id.nameEditText);
            EditText typeEditText = findViewById(R.id.typeEditText);
            EditText unitPriceEditText = findViewById(R.id.unitPriceEditText);
            EditText totalQuantityEditText = findViewById(R.id.totalQuantityEditText);

            nameEditText.setText(parts[0]); // 物料名称
            typeEditText.setText(parts[1]); // 物料类型
            unitPriceEditText.setText(parts[2]); // 单价
            totalQuantityEditText.setText(parts[3]); // 总数
        } else {
            Toast.makeText(this, "扫描结果格式错误", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertMaterial(String name, String type, double unitPrice, int employeeId, int totalQuantity, int receivedQuantity) {
        new Thread(() -> {
            String sql = "INSERT INTO material (Name, Type, UnitPrice, EmployeeID, TotalQuantity, ReceivedQuantity) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection connection = JDBCUtils.getConn();
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, type);
                ps.setDouble(3, unitPrice);
                ps.setInt(4, employeeId);
                ps.setInt(5, totalQuantity);
                ps.setInt(6, receivedQuantity);
                ps.executeUpdate();
                runOnUiThread(() -> Toast.makeText(MaterialActivity.this, "物料添加成功", Toast.LENGTH_SHORT).show());
                finish(); // 返回到上一个活动
            } catch (SQLException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MaterialActivity.this, "添加物料失败: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
