package com.honeymade.hkng;

import android.app.Notification;
import android.content.Intent;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    static RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    static KNotificationAdapter myAdapter;

    static Spinner spinner;
    static ArrayAdapter<String> spinneradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        KtalkGrabber.selectedChatroom = "All";

        Intent svcIntent = new Intent(this, KtalkGrabber.class);
        startService(svcIntent);
        System.out.println("Service started");

        mRecyclerView = findViewById(R.id.rcyclr);
        mRecyclerView.setHasFixedSize(true);

        spinner = findViewById(R.id.spinner);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        myAdapter = new KNotificationAdapter();
        spinneradapter = new ArrayAdapter<String>(this, R.layout.spinnerlayout, R.id.chatroomspinner, KtalkGrabber.senderVector);

        mRecyclerView.setAdapter(myAdapter);
        spinner.setAdapter(spinneradapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                KtalkGrabber.selectedChatroom = KtalkGrabber.senderVector.get(position);
                TextView tv = findViewById(R.id.current);
                tv.setText(KtalkGrabber.selectedChatroom);
                mRecyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    protected void onClearBtnClick(View v) {
        KtalkGrabber.ktNotification.clear();
        KtalkGrabber.senderVector.clear();
        KtalkGrabber.senderVector.add("All");
        KtalkGrabber.senderSet.clear();

        mRecyclerView.setAdapter(myAdapter);
        System.out.println("Clear!");
    }

    protected void onRefreshBtnClick(View v) {

        mRecyclerView.scrollToPosition(myAdapter.getItemCount() - 1);

    }

}
