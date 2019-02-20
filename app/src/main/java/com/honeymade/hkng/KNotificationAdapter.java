package com.honeymade.hkng;

import android.app.Notification;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Vector;

public class KNotificationAdapter extends RecyclerView.Adapter {

    public Vector<Notification> ktNotification;

    private int idx = 0;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView sender;
        TextView chatRoomName;

        TextView message;

        MyViewHolder(View v) {
            super(v);

            this.sender = v.findViewById(R.id.sender);
            this.chatRoomName = v.findViewById(R.id.chatroom);
            this.message = v.findViewById(R.id.messages);

        }
    }

    public KNotificationAdapter() {
        ktNotification = KtalkGrabber.ktNotification;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder mvh = (MyViewHolder)viewHolder;

        if(KtalkGrabber.selectedChatroom.equals("All")) {

            mvh.sender.setText(ktNotification.get(position).extras.getString(Notification.EXTRA_TITLE));
            mvh.chatRoomName.setText(ktNotification.get(position).extras.getString(Notification.EXTRA_SUB_TEXT));
            mvh.message.setText(ktNotification.get(position).extras.getString(Notification.EXTRA_TEXT));

        } else if (KtalkGrabber.selectedChatroom.contains("[개인]")) {

            if (position == 0)
                idx = 0;

            System.out.println(KtalkGrabber.selectedChatroom + " " + idx);
            System.out.println(ktNotification.get(position+idx).extras.getString(Notification.EXTRA_SUB_TEXT));
            System.out.println(ktNotification.get(position+idx).extras.getString(Notification.EXTRA_TITLE));
            System.out.println(ktNotification.get(position+idx).extras.getString(Notification.EXTRA_TEXT));

            while ((!ktNotification.get(position + idx).extras.getString(Notification.EXTRA_TITLE).equals(
                    KtalkGrabber.selectedChatroom.substring(4)) || ktNotification.get(position + idx).extras.getString(Notification.EXTRA_SUB_TEXT) != null
            )) {
                idx++;
            }

            System.out.println("AFT" + KtalkGrabber.selectedChatroom + " " + idx);
            System.out.println("AFT" + ktNotification.get(position+idx).extras.getString(Notification.EXTRA_SUB_TEXT));
            System.out.println("AFT" + ktNotification.get(position+idx).extras.getString(Notification.EXTRA_TITLE));
            System.out.println("AFT" + ktNotification.get(position+idx).extras.getString(Notification.EXTRA_TEXT));

            mvh.sender.setText(ktNotification.get(position+idx).extras.getString(Notification.EXTRA_TITLE));
            mvh.chatRoomName.setText(ktNotification.get(position+idx).extras.getString(Notification.EXTRA_SUB_TEXT));
            mvh.message.setText(ktNotification.get(position+idx).extras.getString(Notification.EXTRA_TEXT));

        } else {

            if (position == 0)
                idx = 0;

            System.out.println(KtalkGrabber.selectedChatroom + " " + idx);
            System.out.println(ktNotification.get(position+idx).extras.getString(Notification.EXTRA_SUB_TEXT));
            System.out.println(ktNotification.get(position+idx).extras.getString(Notification.EXTRA_TITLE));
            System.out.println(ktNotification.get(position+idx).extras.getString(Notification.EXTRA_TEXT));


            while (!checkEqual(ktNotification.get(position + idx).extras.getString(Notification.EXTRA_SUB_TEXT), KtalkGrabber.selectedChatroom))
                idx++;

            mvh.sender.setText(ktNotification.get(position+idx).extras.getString(Notification.EXTRA_TITLE));
            mvh.chatRoomName.setText(ktNotification.get(position+idx).extras.getString(Notification.EXTRA_SUB_TEXT));
            mvh.message.setText(ktNotification.get(position+idx).extras.getString(Notification.EXTRA_TEXT));

        }

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

        int count = 0;

        if (KtalkGrabber.selectedChatroom.equals("All"))
            return ktNotification.size();

        else if (KtalkGrabber.selectedChatroom.contains("[개인]")) {

            for (int i = 0; i < ktNotification.size(); i++) {
                if (KtalkGrabber.selectedChatroom.substring(4).equals(ktNotification.get(i).extras.getString(Notification.EXTRA_TITLE)) && ktNotification.get(i).extras.getString(Notification.EXTRA_SUB_TEXT) == null)
                    count++;
            }

            return count;

        } else {

            for (int i = 0; i < ktNotification.size(); i++) {
                if (checkEqual(KtalkGrabber.selectedChatroom, ktNotification.get(i).extras.getString(Notification.EXTRA_SUB_TEXT)))
                    count++;
            }

            return count;

        }
    }
}
