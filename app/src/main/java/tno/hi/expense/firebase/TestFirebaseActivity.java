package tno.hi.expense.firebase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import tno.hi.expense.R;

public class TestFirebaseActivity extends AppCompatActivity {
    EditText edtName, edtAge;
    Button btnSave;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test_firebase);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtName = findViewById(R.id.edtName);
        edtAge = findViewById(R.id.edtAge);
        btnSave = findViewById(R.id.btnSave);

        // Khởi tạo Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Xử lý sự kiện khi nhấn nút lưu
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
    }

    private void insertData() {
        String name = edtName.getText().toString().trim();
        String ageStr = edtAge.getText().toString().trim();

        if (name.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);

        // Tạo dữ liệu để lưu vào Firestore
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("age", age);

        // Thêm vào Firestore trong collection "Users"
        db.collection("Users")
                .add(user)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(TestFirebaseActivity.this, "Lưu thành công!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(TestFirebaseActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}