package tno.hi.expense.firebase;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String name;
    private int age;
    private String city;

    public User() {}

    // Constructor đầy đủ
    public User(String name, int age, String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }

    // Getter và Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("age", age);
        userMap.put("city", city);
        return userMap;
    }
}
