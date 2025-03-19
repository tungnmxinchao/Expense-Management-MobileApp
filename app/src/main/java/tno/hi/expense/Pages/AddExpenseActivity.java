package tno.hi.expense.Pages;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import tno.hi.expense.R;

public class AddExpenseActivity extends AppCompatActivity {
    private EditText dateInput, amountInput, descriptionInput;
    private Spinner categorySpinner;
    private Button saveButton, cancelButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_expense);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        dateInput = findViewById(R.id.date_input);
        amountInput = findViewById(R.id.amount_input);
        descriptionInput = findViewById(R.id.description_input);
        categorySpinner = findViewById(R.id.category_input);
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);

        // Set up window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.expense_categories,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Set up date picker
        dateInput.setOnClickListener(v -> showDatePickerDialog());

        // Set up buttons
        saveButton.setOnClickListener(v -> saveExpense());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    dateInput.setText(date);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void saveExpense() {
        String date = dateInput.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        String amountStr = amountInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();

        // Validation
        if (date.isEmpty()) {
            dateInput.setError("Vui lòng chọn ngày");
            return;
        }
        if (amountStr.isEmpty()) {
            amountInput.setError("Vui lòng nhập số tiền");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            amountInput.setError("Số tiền không hợp lệ");
            return;
        }

        // Get current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để thêm chi tiêu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create expense data
        Map<String, Object> expense = new HashMap<>();
        expense.put("date", date);
        expense.put("category", category);
        expense.put("amount", amount);
        expense.put("description", description);
        expense.put("userId", currentUser.getUid());
        expense.put("timestamp", System.currentTimeMillis());

        // Save to Firestore
        db.collection("expenses")
                .add(expense)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Đã thêm chi tiêu thành công", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi thêm chi tiêu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}