package tno.hi.expense.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

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
        deleteButton.setOnClickListener(v -> deleteExpense());
        editButton.setOnClickListener(v -> editExpense());
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