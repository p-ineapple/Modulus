//package com.example.modulus.Class;
//
//import java.util.Comparator;
//import java.util.List;
//
//public class MergeSort {
//    public static Comparator<Module> idCompare = new Comparator<Module>() {
//        @Override
//        public int compare(Module mod1, Module mod2) {
//            String id1 = mod1.getId();
//            String id2 = mod2.getId();
//
//            return id1.compareTo(id2);
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            return false;
//        }
//    };
//    public Comparator<Module> nameCompare = new Comparator<Module>() {
//        @Override
//        public int compare(Module mod1, Module mod2) {
//            String name1 = mod1.getName();
//            String name2 = mod2.getName();
//            name1 = name1.toLowerCase();
//            name2 = name2.toLowerCase();
//            return name1.compareTo(name2);
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            return false;
//        }
//    };
//
//    public static void mergeSort(List<Module> a, int n) {
//        if (n < 2) {
//            return;
//        }
//        int mid = n / 2;
//        int[] l = new int[mid];
//        int[] r = new int[n - mid];
//
//        for (int i = 0; i < mid; i++) {
//            l[i] = a[i];
//        }
//        for (int i = mid; i < n; i++) {
//            r[i - mid] = a[i];
//        }
//        mergeSort(l, mid);
//        mergeSort(r, n - mid);
//
//        merge(a, l, r, mid, n - mid);
//    }
//    public static void merge(List<Module> a, List<Module> l, List<Module> r, int left, int right) {
//
//        int i = 0, j = 0, k = 0;
//        while (i < left && j < right) {
//            if (idCompare.compare(l.get(i), r.get(i))) {
//                a[k++] = l[i++];
//            }
//            else {
//                a[k++] = r[j++];
//            }
//        }
//        while (i < left) {
//            a[k++] = l[i++];
//        }
//        while (j < right) {
//            a[k++] = r[j++];
//        }
//    }
//
//}
