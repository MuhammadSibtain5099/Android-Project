package com.example.smartintruderalertsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Home extends AppCompatActivity {
    Switch aSwitch;
    TextView lock,persontxt;
    DatabaseReference ref;
    int maxid=0;
 LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);
        aSwitch = findViewById(R.id.SwitchLock);
        lock = findViewById(R.id.lockTxt);
        linearLayout = findViewById(R.id.personLinear);
        persontxt = findViewById(R.id.personTxt);

        ref = FirebaseDatabase.getInstance().getReference().child("User");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        startService();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "My Notification");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    maxid = Integer.parseInt(snapshot.child("1").child("door").getValue().toString());
                   int  personid = Integer.parseInt(snapshot.child("1").child("person").getValue().toString());
                     if (personid==1){
                         persontxt.setBackgroundColor(Color.parseColor("#FFF80000"));
                         persontxt.setText("Person Detected");
                         persontxt.setTextColor(Color.parseColor("#FFFFFF"));
                     }else{
                         persontxt.setBackgroundColor(Color.parseColor("#FFFFFF"));
                         persontxt.setText("Safe ");
                         persontxt.setTextColor(Color.parseColor("#000000"));

                     }
                    if (maxid == 0) {
                        lock.setText("Unlocked");
                        aSwitch.setChecked(false);
                    } else {
                        lock.setText("Locked");
                        aSwitch.setChecked(true);
                    }
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("door", 1);
                    ref.child("1").updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            lock.setText("Locked");
                        }
                    });
                } else {
                    HashMap hashMap = new HashMap();
                    hashMap.put("door", 0);
                    ref.child("1").updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                            lock.setText("Unlocked");
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            Toast.makeText(getApplicationContext(), "Some Thing Went Wrong,Try Again Later", Toast.LENGTH_LONG).show();
                            aSwitch.setChecked(true);

                        }

                    });
                }
            }
        });

    }
        public void startService() {
               }

}