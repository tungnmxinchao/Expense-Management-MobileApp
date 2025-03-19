package tno.hi.expense.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import tno.hi.expense.Models.Expense;
import tno.hi.expense.R;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private List<Expense> expenseList;

    public ExpenseAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.category.setText(expense.getCategory());
        holder.description.setText(expense.getDescription());
        holder.date.setText(expense.getDate());

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedAmount = format.format(expense.getAmount());
        holder.amount.setText(formattedAmount);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView category, description, date, amount;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.expense_category);
            description = itemView.findViewById(R.id.expense_description);
            date = itemView.findViewById(R.id.expense_date);
            amount = itemView.findViewById(R.id.expense_amount);
        }
    }
}
