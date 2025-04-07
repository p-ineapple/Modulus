package com.example.modulus.Class;

import androidx.annotation.NonNull;

import com.google.android.material.chip.Chip;

public class FilterChip {
    String name;
    Chip chip;
    boolean chipCheck;
    public FilterChip(String name, boolean chipCheck){
        this.name = name;
        this.chipCheck = chipCheck;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Chip getChip() {
        return chip;
    }
    public void setChip(Chip chip) {
        this.chip = chip;
    }

    public boolean isChipCheck() {
        return chipCheck;
    }
    public void setChipCheck(boolean chipCheck) {
        this.chipCheck = chipCheck;
        this.chip.setChecked(chipCheck);
    }


    @NonNull
    @Override
    public String toString(){
        return this.name;
    }
}
