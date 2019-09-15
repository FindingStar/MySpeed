package com.example.myspeed.download.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.myspeed.R;
import com.example.myspeed.base.MyFragmentManager;
import com.example.myspeed.base.MyFragmentTag;
import com.example.myspeed.download.entrance.DownloadEntrance;
import com.example.myspeed.main.DownloadFragment;

public class FabDialogFragment extends DialogFragment {

    private Context context;
    private EditText url_edit;

    public FabDialogFragment(Context context){
        this.context=context;
    }

    private DialogInterface.OnClickListener clickListener=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    Toast.makeText(context,"创建成功",Toast.LENGTH_SHORT).show();

                    String url = url_edit.getText().toString();



                    getFragmentManager().beginTransaction()
                            .remove(FabDialogFragment.this)
                            .commit();
                    new DownloadEntrance().download(url,null,"ordinary_download");

                    break;
            }
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // 设置 对话框view
        View view= LayoutInflater.from(context)
                .inflate(R.layout.fab_detail,null);
        url_edit=view.findViewById(R.id.url_edit);

        return new AlertDialog.Builder(context)
                .setView(view)
                .setTitle(R.string.title_dialog_add)
                .setPositiveButton(android.R.string.ok,clickListener)
                .create();
    }
}
