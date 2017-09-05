package com.app.koran.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.app.koran.PanduanActivity;
import com.app.koran.R;
import com.app.koran.SlideAdapter;
import com.app.koran.Weather.WeatherActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dhian on 30/04/2017.
 */

public class FragmentHome extends Fragment {

    ProgressDialog dialog;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN= {R.drawable.slider1,R.drawable.slider2};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    @Nullable

    private Button buttoncuaca;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        buttoncuaca = (Button) view.findViewById(R.id.cuaca);
        buttoncuaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WeatherActivity.class); // dari MainActivity/posisi saat ini ke SecondActivity
                startActivity(intent);
            }
        });
//        buttoncuaca = (Button) view.findViewById(R.id.lapora);
//        buttoncuaca.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), LaporActivity.class); // dari MainActivity/posisi saat ini ke SecondActivity
//                startActivity(intent);
//            }
//        });
//        buttoncuaca = (Button) view.findViewById(R.id.profile);
//        buttoncuaca.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ProfileActivity.class); // dari MainActivity/posisi saat ini ke SecondActivity
//                startActivity(intent);
//            }
//        });
        buttoncuaca = (Button) view.findViewById(R.id.panduan);
        buttoncuaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PanduanActivity.class); // dari MainActivity/posisi saat ini ke SecondActivity
                startActivity(intent);
            }
        });
        mPager = (ViewPager) view.findViewById(R.id.slider);
        init();
        return view;
    }

    public void onShowLoading() {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();
    }
    public void onDismissLoading() {
        dialog.dismiss();
    }

    public void onMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
    private void init() {
        for(int i=0;i<XMEN.length;i++)
            XMENArray.add(XMEN[i]);


        mPager.setAdapter(new SlideAdapter(getActivity(),XMENArray));

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
    }




}

