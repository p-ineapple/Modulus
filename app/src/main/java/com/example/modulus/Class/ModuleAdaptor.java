package com.example.modulus.Class;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.modulus.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
public class ModuleAdaptor extends ArrayAdapter<Module>{
    public ModuleAdaptor(@NonNull Context context, int resource, @NonNull List<Module> objects) {
        super(context, resource, objects);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Module module = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.module_cell, parent, false);
        }
        TextView moduleName = (TextView) convertView.findViewById(R.id.moduleName);
        moduleName.setText(module.getId() + " - " + module.getName());

        TextView moduleTermProf = (TextView) convertView.findViewById(R.id.moduleTermProf);
        moduleTermProf.setText("Term(s): " + String.join(", ", module.getTerm())
                + " | " + String.join(", ", module.getProf()));

        ChipGroup chipGroup = (ChipGroup) convertView.findViewById(R.id.moduleTags);
        for(String tag : module.getTags()) {
            Chip chip = new Chip(parent.getContext());
            chip.setText(tag);
            chip.setEnsureMinTouchTargetSize(false);
            chip.setTextStartPadding(0);
            chip.setTextEndPadding(0);
            chipGroup.addView(chip);
        }
        return convertView;
    }
}