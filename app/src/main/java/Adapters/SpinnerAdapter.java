package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.lambda.iith.dashboard.R;

public class SpinnerAdapter extends ArrayAdapter<String> {
    private String[] objects;

    public SpinnerAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
    private View getCustomView(final int position, View convertView, ViewGroup parent) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_spinner, parent, false);
        final TextView label=(TextView)row.findViewById(R.id.tv_spinnervalue);
        label.setText(objects[position]);
        return row;
    }
}