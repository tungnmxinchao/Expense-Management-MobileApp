package tno.hi.expense.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import tno.hi.expense.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameInput, emailInput, passwordInput, confirmPasswordInput;
    private Button signupButton;
    private TextView backToLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ các thành phần UI
        usernameInput = findViewById(R.id.username_input);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        signupButton = findViewById(R.id.signup_button);
        backToLogin = findViewById(R.id.back_to_login);

        // Xử lý sự kiện đăng ký
        signupButton.setOnClickListener(v -> registerUser());

        // Chuyển về trang đăng nhập nếu đã có tài khoản
        backToLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });


    }

    private void registerUser() {
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // Kiểm tra đầu vào
        if (TextUtils.isEmpty(username)) {
            usernameInput.setError("Vui lòng nhập tên người dùng");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Vui lòng nhập email");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Vui lòng nhập mật khẩu");
            return;
        }
        if (password.length() < 6) {
            passwordInput.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError("Mật khẩu không khớp");
            return;
        }

        // Đăng ký tài khoản trên Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification(); // Gửi email xác thực
                        }
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công. Vui lòng xác minh email!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Lỗi đăng ký: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}