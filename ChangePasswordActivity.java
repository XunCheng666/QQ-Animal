package com.example.qq;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edtUsername, edtOldPassword, edtNewPassword, edtConfirmNewPassword;
    private Button btnChangePassword;
    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edtUsername = findViewById(R.id.edtUsername);
        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmNewPassword = findViewById(R.id.edtConfirmNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        userDatabase = new UserDatabase(this);

        btnChangePassword.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        String username = edtUsername.getText().toString();
        String oldPassword = edtOldPassword.getText().toString();
        String newPassword = edtNewPassword.getText().toString();
        String confirmNewPassword = edtConfirmNewPassword.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmNewPassword)) {
            Toast.makeText(this, "请输入所有字段内容", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(this, "两次输入密码不相同", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validateOldPassword(username, oldPassword)) {
            Toast.makeText(this, "旧密码不正确", Toast.LENGTH_SHORT).show();
            return;
        }

        updatePasswordInDatabase(username, newPassword);
        Toast.makeText(this, "密码修改成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean validateOldPassword(String username, String oldPassword) {
        SQLiteDatabase db = userDatabase.getReadableDatabase();
        String query ="SELECT * FROM " + UserDatabase.TABLE_USERS + " WHERE " +
                UserDatabase.COLUMN_USERNAME + "=? AND " + UserDatabase.COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, oldPassword});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    // 更新数据库中的密码
    private void updatePasswordInDatabase(String username, String newPassword) {
        SQLiteDatabase db = userDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDatabase.COLUMN_PASSWORD, newPassword);

        // 使用用户名作为条件来更新密码
        db.update(UserDatabase.TABLE_USERS, values, UserDatabase.COLUMN_USERNAME + "=?", new String[]{username});
        db.close();
    }
}
