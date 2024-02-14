package com.example.firedatabase.models;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LakeSpecies {
    private String key;
    private String lakeKKey;
    private String speciesKey;
    private Lake lake;
    private Species species;
    private DatabaseReference tableLake;
    private DatabaseReference tableSpecies;
    private DatabaseReference tableLakeSpecies;
    private FirebaseDatabase db;
    private DatabaseReference mDatabase;
    private final String LAKE_NAME= "Lakes";
    private final String Species_NAME= "Species";
    private LocalDateTime localDateTime;

    public LakeSpecies(){

    }
    public LakeSpecies(String key, String lakeKKey, String speciesKey) {
        this.key = key;
        this.lakeKKey = lakeKKey;
        this.speciesKey = speciesKey;

    }
    public LakeSpecies(String key, String lakeKKey, String speciesKey, LocalDateTime localDateTime ) {
        this(key,lakeKKey,speciesKey);
        this.localDateTime = localDateTime;

    }
    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("key",key);
        result.put("lakeKKey",lakeKKey);
        result.put("speciesKey",speciesKey);
        if(localDateTime ==null ) return result;
        result.put("localTime",localDateTime);
        return result;
    }

    public Lake getLake() {
        return lake;
    }

    public void setLake(List<Lake> lake) {
        this.lake = lake.stream().filter(u->u.getKey().equals(lakeKKey)).findFirst().orElse(null);
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(List<Species> species) {

        this.species = species.stream().filter(u->u.getkey().equals(speciesKey)).findFirst().orElse(null);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }



    public String getLakeKKey() {
        return lakeKKey;
    }

    public void setLakeKKey(String lakeKKey) {
        this.lakeKKey = lakeKKey;
    }

    public String getSpeciesKey() {
        return speciesKey;
    }

    public void setSpeciesKey(String speciesKey) {
        this.speciesKey = speciesKey;
    }
    public synchronized void  loadData()
    {
        db = FirebaseDatabase.getInstance();
        tableLake = db.getReference(LAKE_NAME);
        tableSpecies = db.getReference(Species_NAME);
        DatabaseReference readerLake = tableLake.child(lakeKKey);
        readerLake.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lake = snapshot.getValue(Lake.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference readerSpecies = tableSpecies.child(speciesKey);
        readerSpecies.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                species = snapshot.getValue(Species.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
