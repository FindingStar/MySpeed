package com.example.myspeed.download.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.myspeed.R;

public class FabDialogFragment extends DialogFragment {

    private Context context;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // 设置 对话框view
        View view= LayoutInflater.from(context)
                .inflate(R.layout.fab_detail,null);

        return new AlertDialog.Builder(context)
                .setView(view)
                .setTitle(R.string.title_dialog_add)
                .setPositiveButton(android.R.string.ok,null)
                .create();
    }
}
