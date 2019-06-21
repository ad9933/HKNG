package com.honeymade.hkng;

import android.app.Notification;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import static com.honeymade.hkng.KtalkGrabber.getChatRoom;
import static com.honeymade.hkng.KtalkGrabber.getMessage;
import static com.honeymade.hkng.KtalkGrabber.getSender;

import java.util.Vector;

public class KNotificationAdapter extends RecyclerView.Adapter {

    //public Vector<Notification> ktNotification;

    private int idx = 0;

    static MainActivity mainActivity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView sender;
        TextView chatRoomName;
        TextView message;
        Button moreBtn;
        View popup;

        TextView popupText;
        Button closeBtn;

        PopupWindow pWindow;

        MyViewHolder(View v) {
            super(v);

            this.sender = v.findViewById(R.id.sender);
            this.chatRoomName = v.findViewById(R.id.chatroom);
            this.message = v.findViewById(R.id.messages);
            this.moreBtn = v.findViewById(R.id.show_more_btn);

            popup = MainActivity.inflater.inflate(R.layout.popup_window, (ViewGroup)mainActivity.findViewById(R.id.popup_thing));

            this.popupText = popup.findViewById(R.id.popup_text);
            this.closeBtn = popup.findViewById(R.id.close_btn);

            moreBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    pWindow = new PopupWindow(popup, 800, 900, true);
                    pWindow.showAtLocation(popup, Gravity.CENTER, 0, 0);

                    popupText.setText(message.getText());

                    closeBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            pWindow.dismiss();
                        }
                    });

                }

            });

        }
    }

    public KNotificationAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        //ktNotification = KtalkGrabber.ktNotification;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder mvh = (MyViewHolder)viewHolder;

        mvh.sender.setText(getSender(KtalkGrabber.currentNotification.get(position)));
        mvh.chatRoomName.setText(getChatRoom(KtalkGrabber.currentNotification.get(position)));
        mvh.message.setText(getMessage(KtalkGrabber.currentNotification.get(position)));

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

    public int getItemCount() {

        return KtalkGrabber.currentNotification.size();
    }

}
