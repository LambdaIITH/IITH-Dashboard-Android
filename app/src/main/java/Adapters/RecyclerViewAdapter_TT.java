package Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lambda.iith.dashboard.EditNameDialog;
import com.lambda.iith.dashboard.R;
import com.lambda.iith.dashboard.Timetable;

import java.util.List;
import java.util.prefs.PreferenceChangeEvent;

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
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Boolean b = sharedPreferences.getBoolean("Cname" , true);
        final int j = i;
        myViewHolder.courseName.setText(lectures.get(i).getCourse());
        myViewHolder.courseID.setText(lectures.get(i).getCourseId());
        if (myViewHolder.courseID.getText().toString() == "9" || myViewHolder.courseID.getText().toString() == "10"|| myViewHolder.courseID.getText().toString() == "11"||myViewHolder.courseID.getText().toString() == "12"||myViewHolder.courseID.getText().toString() == "14:30" ||myViewHolder.courseID.getText().toString() == "16" ||myViewHolder.courseID.getText().toString() == "17:30" ||myViewHolder.courseID.getText().toString() == "19"){

           myViewHolder.Time.setVisibility(View.VISIBLE);
           myViewHolder.dayText.setText(myViewHolder.courseID.getText().toString());
           if(!b){
               ViewGroup.LayoutParams params = myViewHolder.Time.getLayoutParams();
               myViewHolder.courseName.setVisibility(View.GONE);
               params.height = (int) (50 * scale + 0.5f);
               params.width = (int) (35 * scale + 0.5f);
               myViewHolder.Time.setLayoutParams(params);
           }

        }
        else if ( myViewHolder.courseID.getText().toString() == "MON" || myViewHolder.courseID.getText().toString() == "TUE"|| myViewHolder.courseID.getText().toString() == "WED"||myViewHolder.courseID.getText().toString() == "THU"||myViewHolder.courseID.getText().toString() == "FRI" ){

            myViewHolder.Day.setVisibility(View.VISIBLE);
            myViewHolder.timeText.setText(myViewHolder.courseID.getText().toString());
            if(!b){
                ViewGroup.LayoutParams params = myViewHolder.Day.getLayoutParams();
                myViewHolder.courseName.setVisibility(View.GONE);
                params.height = (int) (50 * scale + 0.5f);
                params.width = (int) (80 * scale + 0.5f);
                myViewHolder.Day.setLayoutParams(params);
            }

        }
        else if(myViewHolder.courseID.getText().toString() == "Day"){
           myViewHolder.blank.setVisibility(View.VISIBLE);
            if(!b){
                ViewGroup.LayoutParams params = myViewHolder.blank.getLayoutParams();
                myViewHolder.courseName.setVisibility(View.GONE);
                params.height = (int) (50 * scale + 0.5f);
                params.width = (int) (35 * scale + 0.5f);
                myViewHolder.blank.setLayoutParams(params);
            }
        }

        else{
            myViewHolder.cardView.setVisibility(View.VISIBLE);
            if(!b){
                ViewGroup.LayoutParams params = myViewHolder.cardView.getLayoutParams();
                myViewHolder.courseName.setVisibility(View.GONE);
                params.height = (int) (50 * scale + 0.5f);
                params.width = (int) (80 * scale + 0.5f);
                myViewHolder.cardView.setLayoutParams(params);
            }
        }


        myViewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!myViewHolder.courseID.getText().toString().equals("")) {


                    Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show();
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
                Timetable.edit(myViewHolder.courseID.getText().toString() , myViewHolder.editname.getText().toString());
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
            System.out.println("!2" +myViewHolder.courseID.getText().toString() );
            Timetable.Delete(myViewHolder.courseID.getText().toString());
            myViewHolder.courseID.setVisibility(View.VISIBLE);
            myViewHolder.courseName.setVisibility(View.VISIBLE);

            myViewHolder.courseID.setVisibility(View.VISIBLE);
            myViewHolder.courseName.setVisibility(View.VISIBLE);                myViewHolder.editname.setVisibility(View.GONE);
            myViewHolder.save.setVisibility(View.GONE);
            myViewHolder.del.setVisibility(View.GONE);

            Toast.makeText(mContext , "Deleted , Please reopen this tab " , Toast.LENGTH_SHORT).show();
        }
    });
}

    @Override
    public int getItemCount() {
        return lectures.size();
    }

public static class MyViewHolder extends RecyclerView.ViewHolder{

    TextView courseName;
    TextView courseID , dayText , timeText;
    CardView cardView , Day , Time, blank;
    EditText editname;
    Button save , del;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        save = itemView.findViewById(R.id.saver);
        courseName = itemView.findViewById(R.id.courseNameID);
        editname = itemView.findViewById(R.id.editName);
            courseID = (TextView) itemView.findViewById(R.id.courseCodeID);
            cardView = (CardView) itemView.findViewById(R.id.cardView_TT);
            del = itemView.findViewById(R.id.DeleteCourse);
        Day = (CardView) itemView.findViewById(R.id.DayCard);
        Time = itemView.findViewById(R.id.timecard);
        blank = itemView.findViewById(R.id.blank);
        dayText = itemView.findViewById(R.id.timeText);
        timeText = itemView.findViewById(R.id.day);


        }
    }

}
