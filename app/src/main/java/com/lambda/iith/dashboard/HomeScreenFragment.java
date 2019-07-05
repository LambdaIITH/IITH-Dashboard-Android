package com.lambda.iith.dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;

public class HomeScreenFragment extends Fragment {

    //Fragment to be displayed on Home screen which will have 3 cards as layout.
    private CardView cardView2 ;
    private Space space1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_screen , container , false);
        cardView2 = (CardView) view.findViewById(R.id.cardview3);
        space1 = (Space) view.findViewById(R.id.space2);

        //cardView2.setVisibility(View.GONE);
        //space1.setVisibility(View.GONE);
        return view;
    }
}
