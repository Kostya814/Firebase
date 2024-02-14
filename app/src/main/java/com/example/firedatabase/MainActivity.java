package com.example.firedatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firedatabase.models.Lake;
import com.example.firedatabase.models.LakeSpecies;
import com.example.firedatabase.models.Species;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    EditText etName,etAge,etSpeciesType;
    EditText etSpeciesName;
    private DatabaseReference tableLake;
    private DatabaseReference tableSpecies;
    private DatabaseReference tableLakeSpecies;
    private FirebaseDatabase db;
    MyViewModel myViewModel;
    List<Lake> lakes;
    List<Species> species;
    List<LakeSpecies>lakeSpecies;
    private final String LAKE_NAME= "Lakes";
    private final String Species_NAME= "Species";
    private final String LakeSpecies_NAME= "LakeSpecies";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance();
        tableLake = db.getReference(LAKE_NAME);
        tableSpecies = db.getReference(Species_NAME);
        tableLakeSpecies = db.getReference(LakeSpecies_NAME);
        etName = findViewById(R.id.etNameLake);
        etAge = findViewById(R.id.etAgeLake);
        etSpeciesName = findViewById(R.id.etSpeciesName);
        etSpeciesType = findViewById(R.id.etSpeciesType);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);


        tableLake.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lakes = new ArrayList<>();
                for(DataSnapshot singleSnapshot : snapshot.getChildren()){
                    Lake loadLake = singleSnapshot.getValue(Lake.class);
                    lakes.add(loadLake);
                }
                MutableLiveData<List<Lake>> liveLakes = new MutableLiveData<List<Lake>>();
                liveLakes.setValue(lakes);
                myViewModel.setLakes(liveLakes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        tableSpecies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                species = new ArrayList<>();
                for(DataSnapshot singleSnapshot : snapshot.getChildren()){
                    Species loadSpecies = singleSnapshot.getValue(Species.class);
                    species.add(loadSpecies);
                }
                MutableLiveData<List<Species>> liveSpecies = new MutableLiveData<>();
                liveSpecies.setValue(species);
                myViewModel.setSpecies(liveSpecies);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        tableLakeSpecies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lakeSpecies = new ArrayList<>();
                for(DataSnapshot singleSnapshot : snapshot.getChildren()){
                    LakeSpecies loadLakeSpecies = singleSnapshot.getValue(LakeSpecies.class);
                    if(species != null && lakes != null)
                    {
                        loadLakeSpecies.setLake(lakes);
                        loadLakeSpecies.setSpecies(species);
                    }else {
                        loadLakeSpecies.loadData();
                    }

                    lakeSpecies.add(loadLakeSpecies);
                }
                myViewModel.addLakeSpecies(lakeSpecies);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void save(View view)
    {
        if (etName.getText().toString().isEmpty() || etAge.getText().toString().isEmpty()) return;

        String idLake = tableLake.push().getKey();
        String idSpecies = tableSpecies.push().getKey();
        String idLakeSpecies = tableLakeSpecies.push().getKey();

        Lake lake = new Lake(idLake,etName.getText().toString(),Integer.parseInt(etAge.getText().toString()));
        Species species = new Species(idSpecies,etSpeciesName.getText().toString(),etSpeciesType.getText().toString());
        LakeSpecies lakeSpecies = new LakeSpecies(idLakeSpecies,idLake,idSpecies);

        tableLake.child(idLake).setValue(lake.toMap());
        tableSpecies.child(idSpecies).setValue(species.toMap());
        tableLakeSpecies.child(idLakeSpecies).setValue(lakeSpecies.toMap());

        Toast.makeText(this,"Запись добавлена успешно", Toast.LENGTH_LONG).show();


    }
    public void open(View view)
    {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("Name")
                .add(R.id.fragment_container_view, ListFragment.class, null)
                .commit();
    }
}