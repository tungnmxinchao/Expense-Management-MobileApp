package tno.hi.expense.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.Locale;

import tno.hi.expense.Models.Expense;
import tno.hi.expense.R;

public class ExpenseDetailsActivity extends AppCompatActivity {
    private TextView dateDetail, categoryDetail, amountDetail, descriptionDetail;
    private Button editButton, deleteButton, backButton;
    private FirebaseFirestore db;
    private String expenseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        dateDetail = findViewById(R.id.date_detail);
        categoryDetail = findViewById(R.id.category_detail);
        amountDetail = findViewById(R.id.amount_detail);
        descriptionDetail = findViewById(R.id.description_detail);
        editButton = findViewById(R.id.edit_button);
        deleteButton = findViewById(R.id.delete_button);
        backButton = findViewById(R.id.back_button);

        // Get data from intent
        Intent intent = getIntent();
        expenseId = intent.getStringExtra("expenseId");
        String date = intent.getStringExtra("date");
        String category = intent.getStringExtra("category");
        String amount = intent.getStringExtra("amount");
        String description = intent.getStringExtra("description");

        // Set data to views
        dateDetail.setText(date);
        categoryDetail.setText(category);
        amountDetail.setText(amount);
        descriptionDetail.setText(description);

        // Set up buttons
        backButton.setOnClickListener(v -> finish());
        deleteButton.setOnClickListener(v -> showDeleteConfirmDialog());
        editButton.setOnClickListener(v -> editExpense());
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadExpenseDetails();
    }

    private void loadExpenseDetails() {
        db.collection("expenses").document(expenseId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Expense expense = documentSnapshot.toObject(Expense.class);
                        if (expense != null) {
                            dateDetail.setText(expense.getDate());
                            categoryDetail.setText(expense.getCategory());

                            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                            String formattedAmount = format.format(expense.getAmount());
                            amountDetail.setText(formattedAmount);

                            descriptionDetail.setText(expense.getDescription());
                        }
                    } else {
                        Toast.makeText(this, "Chi tiêu không tồn tại", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi tải chi tiêu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showDeleteConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc chắn muốn xóa chi tiêu này không?")
                .setPositiveButton("Có", (dialog, which) -> deleteExpense())
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void deleteExpense() {
        db.collection("expenses").document(expenseId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Chi tiêu đã được xóa", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi xóa chi tiêu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void editExpense() {
        Intent intent = new Intent(ExpenseDetailsActivity.this, EditExpenseActivity.class);
        intent.putExtra("expenseId", expenseId);
        intent.putExtra("date", dateDetail.getText().toString());
        intent.putExtra("category", categoryDetail.getText().toString());
        intent.putExtra("amount", amountDetail.getText().toString());
        intent.putExtra("description", descriptionDetail.getText().toString());
        startActivity(intent);
    }
}