package com.example.modulus.Home;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public interface OnDialogCloseListener {
    @SuppressLint("MissingInflatedId")
    void onCreate(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    void onDialogClose(DialogInterface dialogInterface);

}
