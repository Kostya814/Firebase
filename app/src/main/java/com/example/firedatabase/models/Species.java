package com.example.firedatabase.models;

import java.util.HashMap;
import java.util.Map;

public class Species {
    private String key;
    private String speciesName;
    private String speciesType;

    public Species(){}

    public Species(String key, String speciesName, String speciesType) {
        this.key = key;
        this.speciesName = speciesName;
        this.speciesType = speciesType;
    }
    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("key",key);
        result.put("speciesName",speciesName);
        result.put("speciesType",speciesType);
        return result;
    }
    public String getkey() {
        return key;
    }

    public void setkey(String key) {
        this.key = key;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public String getSpeciesType() {
        return speciesType;
    }

    public void setSpeciesType(String speciesType) {
        this.speciesType = speciesType;
    }
}
