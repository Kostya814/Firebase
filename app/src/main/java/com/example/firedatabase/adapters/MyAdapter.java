package com.example.firedatabase.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.firedatabase.R;
import com.example.firedatabase.models.LakeSpecies;

import java.util.List;


public class MyAdapter extends ArrayAdapter<LakeSpecies> {
    private LayoutInflater inflater;
    private int layout;
    private List<LakeSpecies> lakeSpecies;
    public MyAdapter(Context context, int resource, List<LakeSpecies> states) {
        super(context, resource, states);
        this.lakeSpecies = states;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(this.layout, parent, false);
        TextView tvNameLake, tvAgeLake, tvNameSpecies, tvTypeSpecies;
        tvNameLake = view.findViewById(R.id.tvNameLake);
        tvAgeLake = view.findViewById(R.id.tvAgeLake);
        tvNameSpecies= view.findViewById(R.id.tvNameSpecies);
        tvTypeSpecies = view.findViewById(R.id.tvTypeSpecies);

        LakeSpecies currentLakeSpecies = lakeSpecies.get(position);

        String name = currentLakeSpecies.getLake().getName().toString();
        int age = currentLakeSpecies.getLake().getAge();
        String nameSpecies = currentLakeSpecies.getSpecies().getSpeciesName();
        String type = currentLakeSpecies.getSpecies().getSpeciesType();

        tvNameLake.setText(name);

        tvAgeLake.setText(""+age);
        tvNameSpecies.setText(nameSpecies);
        tvTypeSpecies.setText(type);

        return view;
    }
    public LakeSpecies getLakeSpeciesByPosition(int position)
    {
        return lakeSpecies.get(position);
    }


}
