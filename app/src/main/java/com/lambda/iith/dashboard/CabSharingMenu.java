package com.lambda.iith.dashboard;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class CabSharingMenu extends DialogFragment {
    private FloatingActionButton Register , Snooze, Cancel;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cab_sharing_menu, container , false);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;


        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Register = (FloatingActionButton) view.findViewById(R.id.cs_register);
       Snooze = (FloatingActionButton) view.findViewById(R.id.cs_snooze);
        Cancel = (FloatingActionButton) view.findViewById(R.id.cs_cancel);



       Register.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.fragmentLayout, new CabSharingRegister()).commit();
                getDialog().hide();
            }
       });

        return  view;

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentLayout, new CabSharing()).commit();
    }

    public static CabSharingMenu newInstance(String title) {
        CabSharingMenu frag = new CabSharingMenu();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }
}
