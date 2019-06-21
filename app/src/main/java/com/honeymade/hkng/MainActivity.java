package com.honeymade.hkng;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import static com.honeymade.hkng.KtalkGrabber.getChatRoom;
import static com.honeymade.hkng.KtalkGrabber.getMessage;
import static com.honeymade.hkng.KtalkGrabber.getSender;
import static com.honeymade.hkng.KtalkGrabber.selectedChatroom;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    public static boolean autoRefresh = true;

    static RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    static KNotificationAdapter myAdapter;

    static Spinner spinner;
    static ArrayAdapter<String> spinneradapter;

    public static LayoutInflater inflater = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setContentView(R.layout.activity_main);

        KtalkGrabber.selectedChatroom = "All";

        Intent svcIntent = new Intent(this, KtalkGrabber.class);
        startService(svcIntent);
        System.out.println("Service started");

        ToggleButton toggleRefreshBtn = findViewById(R.id.toggle_refresh);
        toggleRefreshBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) { autoRefresh = true; }
                else {autoRefresh = false; }
            }
        });

        mRecyclerView = findViewById(R.id.rcyclr);
        mRecyclerView.setHasFixedSize(true);

        spinner = findViewById(R.id.spinner);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        myAdapter = new KNotificationAdapter(this);
        spinneradapter = new ArrayAdapter<String>(this, R.layout.spinnerlayout, R.id.chatroomspinner, KtalkGrabber.senderVector);

        mRecyclerView.setAdapter(myAdapter);
        spinner.setAdapter(spinneradapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                KtalkGrabber.selectedChatroom = KtalkGrabber.senderVector.get(position);
                TextView tv = findViewById(R.id.current);
                tv.setText(KtalkGrabber.selectedChatroom);

                KtalkGrabber.currentNotification = getCurrentList();

                mRecyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void onRealRefreshBtnClick(View v) {
        mRecyclerView.setAdapter(myAdapter);
    }

    public void onClearBtnClick(View v) {
        KtalkGrabber.ktNotification.clear();
        KtalkGrabber.senderVector.clear();
        KtalkGrabber.senderVector.add("All");
        KtalkGrabber.senderSet.clear();

        KtalkGrabber.currentNotification = KtalkGrabber.ktNotification;

        mRecyclerView.setAdapter(myAdapter);
        System.out.println("Clear!");
    }

    public void onRefreshBtnClick(View v) {

        mRecyclerView.scrollToPosition(myAdapter.getItemCount() - 1);

    }

    Vector<Notification> getCurrentList() {

        Vector<Notification> result = new Vector<Notification>();

        if(KtalkGrabber.selectedChatroom.equals("All"))
            return KtalkGrabber.ktNotification;
        else if (KtalkGrabber.selectedChatroom.contains("[개인]")) {
            for (int i = 0; i < KtalkGrabber.ktNotification.size(); i++) {
                if((getChatRoom(KtalkGrabber.ktNotification.get(i)) == null) &&
                        getSender(KtalkGrabber.ktNotification.get(i)).equals(KtalkGrabber.selectedChatroom.substring(4))) {

                    result.add(KtalkGrabber.ktNotification.get(i));
                }
            }
        } else {
            for (int i = 0; i < KtalkGrabber.ktNotification.size(); i++) {
                if(checkEqual(getChatRoom(KtalkGrabber.ktNotification.get(i)), KtalkGrabber.selectedChatroom))
                    result.add(KtalkGrabber.ktNotification.get(i));
            }
        }

        return result;

    }

    private boolean checkEqual(String a, String b) {
        if (a == null || b == null)
            return false;
        else
        if(a.equals(b))
            return true;
        else
            return false;
    }

}
