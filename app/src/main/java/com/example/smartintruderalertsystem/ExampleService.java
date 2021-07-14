package com.example.smartintruderalertsystem;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.smartintruderalertsystem.App.CHANNEL_ID;
public class ExampleService extends Service {
    DatabaseReference ref;
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ref = FirebaseDatabase.getInstance().getReference().child("User");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "My Notification");
        Intent notificationIntent = new Intent(this, splash.class);
        //notificationIntent.putExtra("Alert",1);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(getApplicationContext(),snapshot.toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    //   Toast.makeText(getApplicationContext(),dataSnapshot.toString(),Toast.LENGTH_LONG).show();
                    int person = 0;
                    person = Integer.parseInt(dataSnapshot.child("person").getValue().toString());
                    if (person == 1) {

                        builder.setSmallIcon(R.mipmap.ic_launcher);
                        builder.setContentTitle("Alert !!!");
                        builder.setContentText("Someone is Entered in the Protected Area ... ");
                        builder.setAutoCancel(true);
                        builder.setPriority(Notification.PRIORITY_LOW);
                        builder.setContentIntent(pendingIntent);

                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                        notificationManagerCompat.notify(1, builder.build());
                    }
                }
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
        });






///
        String input = intent.getStringExtra("inputExtra");

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Info")
                .setContentText(input)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_LOW)
                .build();
        startForeground(1, notification);

        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}