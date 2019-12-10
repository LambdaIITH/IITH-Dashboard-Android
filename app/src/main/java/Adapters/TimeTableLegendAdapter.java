package Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.lambda.iith.dashboard.R;

import java.util.ArrayList;


public class TimeTableLegendAdapter extends RecyclerView.Adapter<TimeTableLegendAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mCourse = new ArrayList<>();
    private ArrayList<String> mCourseCode = new ArrayList<>();


    private Context mContext;

    public TimeTableLegendAdapter(Context context, ArrayList<String> Names, ArrayList<String> Emails) {
        mCourse = Names;

        mCourseCode = Emails;

        mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.timetable_home_recycler, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


        holder.course.setText(mCourse.get(position));


        holder.time.setText(mCourseCode.get(position));


    }

    @Override
    public int getItemCount() {
        return mCourse.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView course, time;
        ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            course = itemView.findViewById(R.id.CourseName);

            time = itemView.findViewById(R.id.CourseTime);
            parentLayout = itemView.findViewById(R.id.ParentLayout);
        }
    }
}