package tno.hi.expense.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import tno.hi.expense.Adapter.ExpenseAdapter;
import tno.hi.expense.Models.Expense;
import tno.hi.expense.R;

public class HomeActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private List<Expense> expenseList = new ArrayList<>();
    private ExpenseAdapter adapter;

    private TextView totalExpenseView;
    private long totalExpenseThisMonth = 0;

    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        db = FirebaseFirestore.getInstance();


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        }

        totalExpenseView = findViewById(R.id.total_expense);
        adapter = new ExpenseAdapter(expenseList, this);
        RecyclerView recyclerView = findViewById(R.id.recent_expenses_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Button addExpenseButton = findViewById(R.id.add_expense_button);
        addExpenseButton.setOnClickListener(v -> {

            Intent intent = new Intent(HomeActivity.this, AddExpenseActivity.class);
            startActivity(intent);
        });


        fetchExpenses();

    }

    @Override
    protected void onResume() {
        super.onResume();

        fetchExpenses();
    }

    private void fetchExpenses() {
        if (userId == null) {
            return;
        }

        db.collection("expenses")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        expenseList.clear();
                        totalExpenseThisMonth = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Expense expense = document.toObject(Expense.class);
                            expenseList.add(expense);


                            if (isExpenseInCurrentMonth(expense.getTimestamp())) {
                                totalExpenseThisMonth += expense.getAmount();
                            }
                        }
                        adapter.notifyDataSetChanged();
                        updateTotalExpenseView();
                    } else {
                        // Handle error
                    }
                });
    }

    private boolean isExpenseInCurrentMonth(long timestamp) {
        Calendar expenseDate = Calendar.getInstance();
        expenseDate.setTimeInMillis(timestamp);
        Calendar currentDate = Calendar.getInstance();

        return (expenseDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
                expenseDate.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH));
    }

    private void updateTotalExpenseView() {
        // Format total expense as currency
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedTotalExpense = format.format(totalExpenseThisMonth);
        totalExpenseView.setText(formattedTotalExpense);
    }
}