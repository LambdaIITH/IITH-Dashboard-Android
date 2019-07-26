package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lambda.iith.dashboard.R;
import java.util.List;

import Model.Lecture;

public class RecyclerViewAdapter_TT extends RecyclerView.Adapter<RecyclerViewAdapter_TT.MyViewHolder>{

    private Context mContext;
    private List<Lecture> lectures;

    public RecyclerViewAdapter_TT(Context context, List<Lecture> lectures) {
        this.mContext = context;
        this.lectures = lectures;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.segment_timetable,viewGroup,false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.courseName.setText(lectures.get(i).getCourse());
        myViewHolder.courseID.setText(lectures.get(i).getCourseId());

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"Clicked",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lectures.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView courseName;
        TextView courseID;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            courseName = (TextView) itemView.findViewById(R.id.courseNameID);

            courseID = (TextView) itemView.findViewById(R.id.courseCodeID);
            cardView = (CardView) itemView.findViewById(R.id.cardView_TT);

        }
    }

}
