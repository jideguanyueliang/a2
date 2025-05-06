package com.example.xxxmes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.xxxmes.utils.JDBCUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DengluActivity extends AppCompatActivity {

    private EditText employeeIdEdit;
    private EditText passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.denglu); // 使用你提供的 XML 布局文件

        employeeIdEdit = findViewById(R.id.EmployeeIdEdit);
        passwordEdit = findViewById(R.id.PassWordEdit);
        Button loginButton = findViewById(R.id.LoginButton);

        loginButton.setOnClickListener(this::login); // 设置登录按钮的点击事件
    }

    public void login(View view) {
        String employeeId = employeeIdEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        // 创建新的线程进行数据库操作
        new Thread(() -> {
            String sql = "SELECT * FROM Employee WHERE EmployeeID = ? AND Password = ?";
            int msg = 0; // 0: 登录失败, 1: 登录成功, 2: 用户不存在

            try (Connection connection = JDBCUtils.getConn();
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, Integer.parseInt(employeeId));
                ps.setString(2, password);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    msg = 1; // 登录成功

                    // 在登录成功后存储员工ID到 SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("employee_id", Integer.parseInt(employeeId));
                    editor.apply();

                } else {
                    msg = 2; // 用户不存在或密码错误
                }
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 处理登录结果 (需要在主线程中更新 UI)
            int finalMsg = msg;
            runOnUiThread(() -> {
                if (finalMsg == 1) {
                    Toast.makeText(DengluActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    // 登录成功后跳转到 MainActivity
                    Intent intent = new Intent(DengluActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // 关闭当前活动
                } else {
                    Toast.makeText(DengluActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
