package com.example.firedatabase;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.firedatabase.adapters.MyAdapter;
import com.example.firedatabase.models.Lake;
import com.example.firedatabase.models.LakeSpecies;
import com.example.firedatabase.models.Species;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    List<Lake> lakes;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lwList = view.findViewById(R.id.lwList);
        btnBack = view.findViewById(R.id.btnBack);
        myViewModel = new ViewModelProvider(getActivity()).get(MyViewModel.class);

        db = FirebaseDatabase.getInstance();
        tableLakeSpecies = db.getReference("LakeSpecies");
        tableLake = db.getReference("Lakes");
        tableSpecies = db.getReference("Species");

        lakeSpecies = myViewModel.getLakeSpecies().getValue();

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
                    loadLakeSpecies.setLake(lakes);
                    loadLakeSpecies.setSpecies(species);
                    lakeSpecies.add(loadLakeSpecies);
                }
                myViewModel.addLakeSpecies(lakeSpecies);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(lakeSpecies != null)
        {
            myAdapter = new MyAdapter(getContext(),R.layout.list_item,lakeSpecies);
            lwList.setAdapter(myAdapter);
        }
        myViewModel.getLakeSpecies().observe(getViewLifecycleOwner(), new Observer<List<LakeSpecies>>() {
            @Override
            public void onChanged(List<LakeSpecies> lakeSpecie) {
                lakeSpecies = lakeSpecie;
                myAdapter = new MyAdapter(getContext(),R.layout.list_item,lakeSpecie);
                lwList.setAdapter(myAdapter);
            }
        });
        lwList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LakeSpecies foundLakeSpecies =  myAdapter.getLakeSpeciesByPosition(position);
                tableSpecies.child(foundLakeSpecies.getSpeciesKey()).removeValue();
                tableLake.child(foundLakeSpecies.getLakeKKey()).removeValue();
                tableLakeSpecies.child(foundLakeSpecies.getKey()).removeValue();
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
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
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
        et2.setText(""+lakeSpecies.getLake().getAge());
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

                LakeSpecies lakeSpecies1 = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    lakeSpecies1 = new LakeSpecies(lakeSpecies.getKey(),lake.getKey(),species1.getkey(), LocalDateTime.now());
                }
                tableLakeSpecies.child(lakeSpecies.getKey()).setValue(lakeSpecies1.toMap());


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
}