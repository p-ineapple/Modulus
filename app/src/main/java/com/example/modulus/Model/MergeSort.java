package com.example.modulus.Model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSort{
    private Comparator comparator;
    public MergeSort(Comparator comparator){
        this.comparator = comparator;
    }

    public List mergeSort(List a, int n) {
        if (n < 2) {
            return a;
        }
        int mid = n / 2;
        List l = new ArrayList<>(mid);
        List r = new ArrayList<>(n - mid);

        for (int i = 0; i < mid; i++) {
            l.add(a.get(i));
        }
        for (int i = mid; i < n; i++) {
            r.add(a.get(i));
        }
        l = mergeSort(l, mid);
        r = mergeSort(r, n - mid);

        return merge(a, l, r, mid, n - mid);
    }
    public List merge(List a, List l, List r, int left, int right) {

        int i = 0, j = 0, k = 0;
        while (i < left && j < right) {
            if (comparator.compare(l.get(i), r.get(i)) <= 0) {
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
