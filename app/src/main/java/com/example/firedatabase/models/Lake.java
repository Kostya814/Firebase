package com.example.firedatabase.models;

import java.util.HashMap;
import java.util.Map;

public class Lake {
    private String key;
    private String name;
    private int age;

    public Lake()
    {}
    public Lake(String key,String name, int age) {
        this.key = key;
        this.name = name;
        this.age = age;
    }

    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("key",key);
        result.put("name",name);
        result.put("age",age);
        return result;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String id) {
        this.key = id;
    }

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
}
