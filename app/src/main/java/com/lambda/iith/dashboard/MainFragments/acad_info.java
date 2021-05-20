package com.lambda.iith.dashboard.MainFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lambda.iith.dashboard.Launch;
import com.lambda.iith.dashboard.R;

public class acad_info extends Fragment {
    private WebView webView;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_acad_info, container, false);

//        ImageView img = (ImageView) view.findViewById(R.id.img);
//        MaterialCardView card= view.findViewById(R.id.card);
//        card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent link=new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/18J6zZm9jnjkY6le8Nbk9kQU9bUXsXSVC/view?usp=sharing"));
//                startActivity(link);
//            }
//        });

//        final Button curriculum = (Button) view.findViewById(R.id.curriculum);
//        ViewGroup.LayoutParams params = curriculum.getLayoutParams();
//        params.width = -5+Launch.width/3;
//        curriculum.setLayoutParams(params);
//
//
//        final Button handbooks = (Button) view.findViewById(R.id.handbooks);
//        ViewGroup.LayoutParams params1 = handbooks.getLayoutParams();
//        params1.width = -5+ Launch.width/3;
//        handbooks.setLayoutParams(params1);
//
//        final Button acad_cal = (Button) view.findViewById(R.id.acad_calender);
//        ViewGroup.LayoutParams params2 = acad_cal.getLayoutParams();
//        params2.width = -5+Launch.width/3;
//        acad_cal.setLayoutParams(params2);

        webView = view.findViewById(R.id.webview);
        //webView.setLayoutParams(new LinearLayout.LayoutParams(Launch.width , Launch.height*));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webSettings.setForceDark(WebSettings.FORCE_DARK_ON);
        String wpix = Integer.toString(Launch.width*400/1080);
        String unencodedHtml = "<iframe src=\"https://calendar.google.com/calendar/embed?height=700&amp;wkst=2&amp;bgcolor=%23ffffff&amp;ctz=Asia%2FKolkata&amp;src=Y19sbDczZW04czgxbmh1OG5ianZnZ3FobWJnY0Bncm91cC5jYWxlbmRhci5nb29nbGUuY29t&amp;color=%23E4C441&amp;showPrint=0&amp;showCalendars=0&amp;showTz=0&amp;showTabs=1&amp;showTitle=0&amp;showNav=1\" style=\"border-width:0\" width=\" " +wpix + "\" height=\"700\" frameborder=\"0\" scrolling=\"no\"></iframe>";
        String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(),
                Base64.NO_PADDING);
        webView.loadData(encodedHtml, "text/html", "base64");

//        curriculum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent curri=new Intent(getActivity(), curriculum.class);
//                startActivity(curri);
//            }
//        });

//        acad_cal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://calendar.google.com/calendar/u/0/r?cid=c_ll73em8s81nhu8nbjvggqhmbgc@group.calendar.google.com")));
//
//            }
//        });

//        handbooks.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://iith.dev/curriculum/academic_handbook.pdf")));
//
//            }
//        });




        return view;
    }
}