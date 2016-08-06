package com.example.mrchenrunfeng.myecg.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.mrchenrunfeng.myecg.R;
import com.example.mrchenrunfeng.myecg.classes.Command;

/**
 * Created by Mr.Chen RunFENG on 2016/7/28.
 */
public class SaveDialogFragmemt extends DialogFragment {
    private EditText editText;
    public interface SaveInputListener
    {
        void onSaveInputComplete(String filename, int cmd);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.save, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("保存",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                SaveInputListener listener = (SaveInputListener) getActivity();
                                listener.onSaveInputComplete(editText
                                        .getText().toString(), Command.intSaveData);
                            }
                        }).setNegativeButton("取消", null);
        return builder.create();
    }
}
