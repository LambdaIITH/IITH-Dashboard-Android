package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lambda.iith.dashboard.R;

import java.util.List;

import Model.Lecture;

public class RecyclerViewAdapter2_TT extends RecyclerView.Adapter<RecyclerViewAdapter2_TT.MyViewHolder> {

    private Context mContext;
    private List<Lecture> lectures;
    private List<String> T1;
    private List<String> T2;

    public RecyclerViewAdapter2_TT(Context context, List<Lecture> lectures, List<String> T1, List<String> T2) {
        this.mContext = context;
        this.lectures = lectures;
        this.T1 = T1;
        this.T2 = T2;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.timetable_dailyview, viewGroup, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        myViewHolder.textView4.setText(lectures.get(i).getCourse());
        myViewHolder.textView3.setText(lectures.get(i).getCourseId());
        myViewHolder.textView.setText(T1.get(i));
        myViewHolder.textView2.setText(T2.get(i));


    }

    @Override
    public int getItemCount() {
        return lectures.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView, textView2, textView3, textView4;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.time1);
            textView2 = itemView.findViewById(R.id.time2);
            textView3 = itemView.findViewById(R.id.CCode);
            textView4 = itemView.findViewById(R.id.Cname);


        }
    }

}
