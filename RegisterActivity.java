package com.example.qq;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword, edtConfirmPassword;
    private Button btnRegister;
    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        userDatabase = new UserDatabase(this);

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "请再输入一次密码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "两次密码不相同", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userExists(username)) {
            Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to database
        SQLiteDatabase db = userDatabase.getWritableDatabase();
        String insertQuery = "INSERT INTO " + UserDatabase.TABLE_USERS + " (" +
                UserDatabase.COLUMN_USERNAME + ", " + UserDatabase.COLUMN_PASSWORD + ") VALUES (?, ?)";
        db.execSQL(insertQuery, new Object[]{username, password});
        db.close();

        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean userExists(String username) {
        SQLiteDatabase db = userDatabase.getReadableDatabase();
        String query = "SELECT * FROM " + UserDatabase.TABLE_USERS + " WHERE " +
                UserDatabase.COLUMN_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}
