package Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lambda.iith.dashboard.Launch;
import com.lambda.iith.dashboard.R;
import com.lambda.iith.dashboard.Timetable.Timetable;

import java.util.List;

import Model.Lecture;

public class RecyclerViewAdapter_TT extends RecyclerView.Adapter<RecyclerViewAdapter_TT.MyViewHolder> {

    private final Context mContext;
    private final List<Lecture> lectures;
    private List<ColorInt> colours;
    private final int temp = 0;
    private int width;

    public RecyclerViewAdapter_TT(Context context, List<Lecture> lectures) {
        this.mContext = context;
        this.lectures = lectures;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.segment_timetable, viewGroup, false);

        width = (int) Math.floor(convertPxToDp(mContext, Launch.width/ 5));



        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Boolean b = sharedPreferences.getBoolean("Cname", true);
        final int j = i;
        myViewHolder.courseName.setText(lectures.get(i).getCourse());
        myViewHolder.courseID.setText(lectures.get(i).getCourseId());
        myViewHolder.parent.removeView(myViewHolder.cardView);
        myViewHolder.parent.removeView(myViewHolder.Time);

        ViewGroup.LayoutParams params1 = myViewHolder.cardView.getLayoutParams();
        params1.width = (int) ((width - 10) * scale + 0.5f);
        myViewHolder.cardView.setLayoutParams(params1);


        if (myViewHolder.courseID.getText().toString() == "9" || myViewHolder.courseID.getText().toString() == "10" || myViewHolder.courseID.getText().toString() == "11" || myViewHolder.courseID.getText().toString() == "12" || myViewHolder.courseID.getText().toString() == "14:30" || myViewHolder.courseID.getText().toString() == "16" || myViewHolder.courseID.getText().toString() == "17:30" || myViewHolder.courseID.getText().toString() == "19") {
            myViewHolder.parent.addView(myViewHolder.Time);
            myViewHolder.Time.setVisibility(View.VISIBLE);
            myViewHolder.dayText.setText(myViewHolder.courseID.getText().toString());
            if (!b) {
                ViewGroup.LayoutParams params = myViewHolder.Time.getLayoutParams();

                myViewHolder.courseName.setVisibility(View.GONE);
                params.height = (int) (50 * scale + 0.5f);
                //params.width = (int) (35 * scale + 0.5f);

                myViewHolder.Time.setLayoutParams(params);
            }


        } else {


            myViewHolder.parent.addView(myViewHolder.cardView);
            myViewHolder.cardView.setVisibility(View.VISIBLE);
            myViewHolder.cardView.setBackgroundColor(lectures.get(i).getCourseColor());


            if (!b) {
                ViewGroup.LayoutParams params = myViewHolder.cardView.getLayoutParams();
                myViewHolder.courseName.setVisibility(View.GONE);
                params.height = (int) (50 * scale + 0.5f);
                //params.width = (int) (80 * scale + 0.5f);
                myViewHolder.cardView.setLayoutParams(params);

            }


        }


        myViewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!myViewHolder.courseID.getText().toString().equals("")) {
                    myViewHolder.courseID.setVisibility(View.GONE);
                    myViewHolder.del.setVisibility(View.VISIBLE);
                    myViewHolder.courseName.setVisibility(View.GONE);
                    myViewHolder.editname.setVisibility(View.VISIBLE);
                    myViewHolder.save.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;

            }
        });


        myViewHolder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timetable.edit(myViewHolder.courseID.getText().toString(), myViewHolder.editname.getText().toString());
                myViewHolder.editname.setVisibility(View.GONE);
                myViewHolder.courseID.setVisibility(View.VISIBLE);
                myViewHolder.courseName.setVisibility(View.VISIBLE);
                myViewHolder.save.setVisibility(View.GONE);
                myViewHolder.del.setVisibility(View.GONE);
                myViewHolder.courseName.setText(myViewHolder.editname.getText().toString());


            }
        });

        myViewHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Timetable.Delete(myViewHolder.courseID.getText().toString());
                myViewHolder.courseID.setVisibility(View.VISIBLE);
                myViewHolder.courseName.setVisibility(View.VISIBLE);

                myViewHolder.courseID.setVisibility(View.VISIBLE);
                myViewHolder.courseName.setVisibility(View.VISIBLE);
                myViewHolder.editname.setVisibility(View.GONE);
                myViewHolder.save.setVisibility(View.GONE);
                myViewHolder.del.setVisibility(View.GONE);

                //Toast.makeText(mContext , "Deleted , Please reopen this tab " , Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return lectures.size();
    }

    public double convertPxToDp(Context context, double px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView courseName;
        TextView courseID, dayText, timeText;
        LinearLayout cardView, Time;
        EditText editname;
        Button save, del;
        LinearLayout parent;
        Space space;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.timetablelinear);
            save = itemView.findViewById(R.id.saver);
            courseName = itemView.findViewById(R.id.courseNameID);
            editname = itemView.findViewById(R.id.editName);
            courseID = itemView.findViewById(R.id.courseCodeID);
            cardView = itemView.findViewById(R.id.cardView_TT);
            del = itemView.findViewById(R.id.DeleteCourse);


            Time = itemView.findViewById(R.id.timecard);

            dayText = itemView.findViewById(R.id.timeText);


        }
    }

}
