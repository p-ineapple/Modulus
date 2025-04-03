package com.example.modulus.Planner;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.modulus.Class.Module;
import com.example.modulus.Class.Planner;
//import com.example.modulus.Adapter.PlannerAdaptor;
import com.example.modulus.Insights.ModuleDetailsActivity;
import com.example.modulus.R;

import java.util.ArrayList;

public class PlannerFragment extends Fragment {

    Planner term1, term2, term3, term4, term5, term6, term7, term8;
    ListView list1, list2, list3, list4, list5, list6 ,list7, list8;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_planner, container, false);
//        list1 = view.findViewById(R.id.plannerList1);
//        Module hass1 = new Module("02.003", "Social Science: Understanding Behaviour, Culture & Society");
//        Module mod1 = new Module("10.013", "Modelling and Analysis");
//        Module mod2 = new Module("10.014", "Computational Thinking for Design");
//        Module mod3 = new Module("10.015", "Physical World");
//        ArrayList<Module> term1mods = new ArrayList<>();
//        term1mods.add(hass1); term1mods.add(mod1); term1mods.add(mod2); term1mods.add(mod3);
//        term1 = new Planner("1");
//        term1.setModules(term1mods);
//        PlannerAdaptor adaptor = new PlannerAdaptor(getContext(), 0, term1.getModules());
//        list1.setAdapter(adaptor);
//        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Module selectModule = (Module) (list1.getItemAtPosition(position));
//                Intent showDetail = new Intent(getContext(), ModuleDetailsActivity.class);
//                showDetail.putExtra("id", selectModule.getId());
//                startActivity(showDetail);
//            }
//        });
        return view;
    }
}