package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.lambda.iith.dashboard.R;

import java.util.ArrayList;

import structs.BsItem;

public class CustomAdapter extends ArrayAdapter<BsItem> {

    public CustomAdapter(Context context, ArrayList<BsItem> strings) {
        super(context,0,strings);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_spinner_dropdown,parent,false
            );
        }

        TextView dropdownText = convertView.findViewById(R.id.textview_dropdown);
        ImageView dropdownImage = convertView.findViewById(R.id.image_dropdown);

        BsItem currentItem = getItem(position);
        if(currentItem != null) {
            dropdownImage.setImageResource(currentItem.getBackGroundImage());
            dropdownText.setText(currentItem.getBsType());
        }
        return convertView;
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_spinner,parent,false
            );
        }

        ImageView imageViewbackground = convertView.findViewById(R.id.image_background_spinner);
        TextView textViewSpinner = convertView.findViewById(R.id.textview_name);

        BsItem currentItem = getItem(position);
        if(currentItem != null) {
            imageViewbackground.setImageResource(currentItem.getBackGroundImage());
            textViewSpinner.setText(currentItem.getBsType());
        }
        return convertView;
    }

    @Override
    public int getCount(){
        int count = super.getCount();
        return count > 0 ? count -1:count;
    }



}
