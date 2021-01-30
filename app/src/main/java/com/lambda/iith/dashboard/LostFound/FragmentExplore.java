package com.lambda.iith.dashboard.LostFound;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lambda.iith.dashboard.R;

import java.util.ArrayList;

public class FragmentExplore extends Fragment {

    View view;


    public FragmentExplore() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.explore_fragment, container, false);

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
        ListView listView = view.findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);


        ImageView home = view.findViewById(R.id.add_id_circle);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(getActivity(), Main3Activity.class);
                startActivity(homeIntent);
            }
        });

        return view;

    }


}
