package com.example.modulus.Model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSort {
    public static Comparator<ModuleModel> idCompare = new Comparator<ModuleModel>() {
        @Override
        public int compare(ModuleModel mod1, ModuleModel mod2) {
            String id1 = mod1.getId();
            String id2 = mod2.getId();

            return id1.compareTo(id2);
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    };
    public static Comparator<ModuleModel> nameCompare = new Comparator<ModuleModel>() {
        @Override
        public int compare(ModuleModel mod1, ModuleModel mod2) {
            String name1 = mod1.getName();
            String name2 = mod2.getName();
            name1 = name1.toLowerCase();
            name2 = name2.toLowerCase();
            return name1.compareTo(name2);
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    };

    public static List<ModuleModel> mergeSortID(List<ModuleModel> a, int n) {
        if (n < 2) {
            return a;
        }
        int mid = n / 2;
        List<ModuleModel> l = new ArrayList<ModuleModel>(mid);
        List<ModuleModel> r = new ArrayList<ModuleModel>(n - mid);

        for (int i = 0; i < mid; i++) {
            l.add(a.get(i));
        }
        for (int i = mid; i < n; i++) {
            r.add(a.get(i));
        }
        l = mergeSortID(l, mid);
        r = mergeSortID(r, n - mid);

        return mergeID(a, l, r, mid, n - mid);
    }
    public static List<ModuleModel> mergeID(List<ModuleModel> a, List<ModuleModel> l, List<ModuleModel> r, int left, int right) {

        int i = 0, j = 0, k = 0;
        while (i < left && j < right) {
            if (idCompare.compare(l.get(i), r.get(i)) <= 0) {
                a.set(k++, l.get(i++));
            }
            else {
                a.set(k++, r.get(j++));
            }
        }
        while (i < left) {
            a.set(k++, l.get(i++));
        }
        while (j < right) {
            a.set(k++, r.get(j++));
        }
        return a;
    }

    public static List<ModuleModel> mergeSortName(List<ModuleModel> a, int n) {
        if (n < 2) {
            return a;
        }
        int mid = n / 2;
        List<ModuleModel> l = new ArrayList<ModuleModel>(mid);
        List<ModuleModel> r = new ArrayList<ModuleModel>(n - mid);

        for (int i = 0; i < mid; i++) {
            l.add(a.get(i));
        }
        for (int i = mid; i < n; i++) {
            r.add(a.get(i));
        }
        l = mergeSortName(l, mid);
        r = mergeSortName(r, n - mid);

        return mergeName(a, l, r, mid, n - mid);
    }
    public static List<ModuleModel> mergeName(List<ModuleModel> a, List<ModuleModel> l, List<ModuleModel> r, int left, int right) {

        int i = 0, j = 0, k = 0;
        while (i < left && j < right) {
            if (nameCompare.compare(l.get(i), r.get(i)) <= 0) {
                a.set(k++, l.get(i++));
            }
            else {
                a.set(k++, r.get(j++));
            }
        }
        while (i < left) {
            a.set(k++, l.get(i++));
        }
        while (j < right) {
            a.set(k++, r.get(j++));
        }
        return a;
    }

}
