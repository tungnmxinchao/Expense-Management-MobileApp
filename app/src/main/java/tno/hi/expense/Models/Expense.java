package tno.hi.expense.Models;

import java.util.UUID;

public class Expense {
    private String id;
    private long amount;
    private String category;
    private String date;
    private String description;
    private long timestamp;
    private String userId;

    public Expense() {
        this.id = UUID.randomUUID().toString();
    }
    public Expense(long amount, String category, String date, String description, long timestamp, String userId) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
        this.timestamp = timestamp;
        this.userId = userId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
