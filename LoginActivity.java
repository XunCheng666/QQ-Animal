package com.example.qq;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnRegister, btnChangePassword;
    private SharedPreferences sharedPreferences;
    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        userDatabase = new UserDatabase(this);

        // 读取记住的用户名和密码
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");
        edtUsername.setText(savedUsername);
        edtPassword.setText(savedPassword);

        btnLogin.setOnClickListener(v -> login());
        btnRegister.setOnClickListener(v -> navigateToRegister());
        btnChangePassword.setOnClickListener(v -> navigateToChangePassword());
    }

    private void login() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();

        if (validateCredentials(username, password)) {
            // 保存用户名和密码到SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.apply();

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        } else {
            Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateCredentials(String username, String password) {
        // 从数据库中查询用户名和密码是否匹配
        SQLiteDatabase db = userDatabase.getReadableDatabase();
        String query = "SELECT * FROM " + UserDatabase.TABLE_USERS + " WHERE " +
                UserDatabase.COLUMN_USERNAME + "=? AND " + UserDatabase.COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    private void navigateToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void navigateToChangePassword() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }

}
