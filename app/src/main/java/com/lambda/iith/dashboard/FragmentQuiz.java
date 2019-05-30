package com.lambda.iith.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;

public class FragmentQuiz extends Fragment {
    View view;
    TextView txtData;

    public FragmentQuiz() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.quiz_fragment, container, false);


//


//        Button myButton = view.findViewById(R.id.button);
//        myButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction fr = getFragmentManager().beginTransaction();
//                fr.replace(R.id.frame_id, new FragmentAdd());
//                fr.commit();
//            }
//        });


        ArrayList<String> words = new ArrayList<String>();
        words.add("one");
        words.add("two");
        words.add("three");
        words.add("four");
        words.add("five");
        words.add("six");
        words.add("seven");
        words.add("eight");
        words.add("nine");
        words.add("ten");

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, words);
        ListView listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);


        ImageView home=(ImageView) view.findViewById(R.id.add_id_circle);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(getActivity(),MainActivity.class);
                startActivity(homeIntent);
            }
        });
        return view;

    }


}

