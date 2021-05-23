package com.example.smartintruderalertsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


     DatabaseReference ref;
     EditText name,phone,email;
     int maxid=0;
     Member member;
    Button button;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    name = findViewById(R.id.name);
    phone =findViewById(R.id.ed_phone);
    email = findViewById(R.id.ed_email);
    button = findViewById(R.id.btnSend);
    member = new Member();
    ref = FirebaseDatabase.getInstance().getReference().child("User");
    ref.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                maxid = (int) snapshot.getChildrenCount();
            }else{

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        member.setName(name.getText().toString());
        member.setEmail(email.getText().toString());
        member.setPhone(phone.getText().toString());
        ref.child(String.valueOf(maxid+1)).setValue(member);
    }
});
    }
}