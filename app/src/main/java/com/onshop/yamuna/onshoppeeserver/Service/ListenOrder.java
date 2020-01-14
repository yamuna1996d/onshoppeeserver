package com.onshop.yamuna.onshoppeeserver.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onshop.yamuna.onshoppeeserver.Models.Request;
import com.onshop.yamuna.onshoppeeserver.Orderstatus;
import com.onshop.yamuna.onshoppeeserver.R;

import java.util.Random;

import androidx.core.app.NotificationCompat;

import static android.os.Build.VERSION_CODES.P;

public class ListenOrder extends Service implements ChildEventListener {
    FirebaseDatabase dta;
    DatabaseReference ordrs;

    @Override
    public void onCreate() {
        super.onCreate();
        dta=FirebaseDatabase.getInstance();
        ordrs=dta.getReference("Requests");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ordrs.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Request request=dataSnapshot.getValue(Request.class);
        if (request.getStatus().equals("0"))
            showNotification(dataSnapshot.getKey(),request);

    }

    private void showNotification(String key, Request request) {
        Intent i=new Intent(getBaseContext(),Orderstatus.class);
        PendingIntent contentIntent=PendingIntent.getActivity(getBaseContext(),P,i,0);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("Yamuna").setContentInfo("New Order")
                .setContentText("You have new order # " +key)
                .setSmallIcon(R.drawable.cart)
                .setContentIntent(contentIntent);

        NotificationManager manager=(NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        //give unique id for getting many notifications
        int random= new Random().nextInt(9999-1)+1;
        manager.notify(random,builder.build());
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
