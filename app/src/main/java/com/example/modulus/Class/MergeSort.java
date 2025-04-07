package com.example.modulus.Class;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSort {
    public static Comparator<Module> idCompare = new Comparator<Module>() {
        @Override
        public int compare(Module mod1, Module mod2) {
            String id1 = mod1.getId();
            String id2 = mod2.getId();

            return id2.compareTo(id1);
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    };
    public static Comparator<Module> nameCompare = new Comparator<Module>() {
        @Override
        public int compare(Module mod1, Module mod2) {
            String name1 = mod1.getName();
            String name2 = mod2.getName();
            name1 = name1.toLowerCase();
            name2 = name2.toLowerCase();
            return name2.compareTo(name1);
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    };

    public static void mergeSortID(List<Module> a, int n) {
        if (n < 2) {
            return;
        }
        int mid = n / 2;
        List<Module> l = new ArrayList<Module>(mid);
        List<Module> r = new ArrayList<Module>(n - mid);

        for (int i = 0; i < mid; i++) {
            l.set(i, a.get(i));
        }
        for (int i = mid; i < n; i++) {
            r.set(i - mid, a.get(i));
        }
        mergeSortID(l, mid);
        mergeSortID(r, n - mid);

        mergeID(a, l, r, mid, n - mid);
    }
    public static void mergeID(List<Module> a, List<Module> l, List<Module> r, int left, int right) {

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
    }

    public static void mergeSortName(List<Module> a, int n) {
        if (n < 2) {
            return;
        }
        int mid = n / 2;
        List<Module> l = new ArrayList<Module>(mid);
        List<Module> r = new ArrayList<Module>(n - mid);

        for (int i = 0; i < mid; i++) {
            l.set(i, a.get(i));
        }
        for (int i = mid; i < n; i++) {
            r.set(i - mid, a.get(i));
        }
        mergeSortName(l, mid);
        mergeSortName(r, n - mid);

        mergeName(a, l, r, mid, n - mid);
    }
    public static void mergeName(List<Module> a, List<Module> l, List<Module> r, int left, int right) {

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
    }

}
