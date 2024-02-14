package com.example.firedatabase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.firedatabase.models.Lake;
import com.example.firedatabase.models.LakeSpecies;
import com.example.firedatabase.models.Species;

import java.util.List;

public class MyViewModel extends ViewModel {
    private MutableLiveData<List<Lake>> lakes;
    private MutableLiveData< List<LakeSpecies>> lakeSpecies;
    private MutableLiveData<List<Species>> species;

    public MyViewModel()
    {
            lakeSpecies = new MutableLiveData<>();
            lakes = new MutableLiveData<>();
            species = new MutableLiveData<>();
    }
    public LiveData<List<Lake>> getLakes() {
        return lakes;
    }

    public void setLakes(MutableLiveData<List<Lake>> lakes) {
        this.lakes = lakes;
    }

    public LiveData<List<LakeSpecies>> getLakeSpecies() {
        return lakeSpecies;
    }

    public void setLakeSpecies(MutableLiveData<List<LakeSpecies>> lakeSpecies) {
        this.lakeSpecies = lakeSpecies;
    }
    public void addLakeSpecies(List<LakeSpecies> lakeSpecies)
    {
        this.lakeSpecies.postValue(lakeSpecies);
    }

    public LiveData<List<Species>> getSpecies() {
        return species;
    }

    public void setSpecies(MutableLiveData<List<Species>> species) {
        this.species = species;
    }
}
