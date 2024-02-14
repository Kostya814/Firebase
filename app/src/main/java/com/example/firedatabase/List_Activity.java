package com.example.firedatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.firedatabase.adapters.MyAdapter;
import com.example.firedatabase.models.Lake;
import com.example.firedatabase.models.LakeSpecies;
import com.example.firedatabase.models.Species;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class List_Activity extends AppCompatActivity {

    List<Lake>lakes;
    List<Species> species;
    List<LakeSpecies> lakeSpecies;
    private MyViewModel myViewModel;
    private MyAdapter myAdapter;
    private ListView lwList;
    private FirebaseDatabase db;
    private DatabaseReference tableLake;
    private DatabaseReference tableSpecies;
    private DatabaseReference tableLakeSpecies;
    private Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        lwList = findViewById(R.id.lwList);
        btnBack = findViewById(R.id.btnBack);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        db = FirebaseDatabase.getInstance();
        tableSpecies = db.getReference("LakeSpecies");
        tableLake = db.getReference("Lakes");
        tableSpecies = db.getReference("Species");

        lakeSpecies = myViewModel.getLakeSpecies().getValue();
        myAdapter = new MyAdapter(getApplication(),R.layout.list_item,lakeSpecies);

        lwList.setAdapter(myAdapter);
        myViewModel.getLakeSpecies().observe(this, new Observer<List<LakeSpecies>>() {
            @Override
            public void onChanged(List<LakeSpecies> lakeSpecie) {
                lakeSpecies = lakeSpecie;
                myAdapter = new MyAdapter(getApplication(),R.layout.list_item,lakeSpecie);
                lwList.setAdapter(myAdapter);
            }
        });
        lwList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               LakeSpecies foundLakeSpecies =  myAdapter.getLakeSpeciesByPosition(position);
               tableSpecies.child(foundLakeSpecies.getKey()).removeValue();
               return true;
            }
        });
        lwList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LakeSpecies foundLakeSpecies =  myAdapter.getLakeSpeciesByPosition(position);
                openDialog(view, foundLakeSpecies);
            }
        });
    }
    public void openDialog (View view, LakeSpecies lakeSpecies)
    {
        final Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_edit);
        final EditText et = dialog.findViewById(R.id.et);
        final EditText et2 = dialog.findViewById(R.id.et2);
        final EditText et3 = dialog.findViewById(R.id.et3);
        final EditText et4 = dialog.findViewById(R.id.et4);

        et.setText(lakeSpecies.getLake().getName());
        et2.setText(lakeSpecies.getLake().getAge());
        et3.setText(lakeSpecies.getSpecies().getSpeciesName());
        et4.setText(lakeSpecies.getSpecies().getSpeciesType());

        Button btnok = (Button) dialog.findViewById(R.id.btnok);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et.getText().toString().isEmpty() || et2.getText().toString().isEmpty()
                        || et3.getText().toString().isEmpty() || et4.getText().toString().isEmpty()) return;
                Lake lake = new Lake(lakeSpecies.getLakeKKey(),et.getText().toString(),Integer.parseInt(et2.getText().toString()));
                tableLake.child(lakeSpecies.getLakeKKey()).setValue(lake);
                Species species1 = new Species(lakeSpecies.getSpeciesKey(),et3.getText().toString(),et4.getText().toString());
                tableSpecies.child(lakeSpecies.getSpeciesKey()).setValue(species1);

                dialog.dismiss();
            }
        });
        Button btncn = (Button) dialog.findViewById(R.id.btncn);
        btncn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void back(View view)
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}