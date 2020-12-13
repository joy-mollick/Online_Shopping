package com.example.online_shopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.online_shopping.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Forget extends AppCompatActivity {
    private EditText user_name,pass_wrd;
    private Button cnfirm;
    private ProgressDialog loadingBar;
    String us,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        user_name=(EditText)findViewById(R.id.nam);
        pass_wrd=(EditText)findViewById(R.id.pss);
        cnfirm=(Button)findViewById(R.id.cnfrm);
        loadingBar = new ProgressDialog(this);
        cnfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phone = pass_wrd.getText().toString();
                final String nme = user_name.getText().toString();
                if (TextUtils.isEmpty(phone))
                {
                    Toast.makeText(Forget.this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(nme))
                {
                    Toast.makeText(Forget.this, "Please write your password...", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                        if(internet()) {
                            loadingBar.setTitle("Finding Account");
                            loadingBar.setMessage("Please wait, while we are checking your account.");
                            loadingBar.setCanceledOnTouchOutside(false);
                            loadingBar.show();
                            final DatabaseReference RootRef;
                            RootRef = FirebaseDatabase.getInstance().getReference();
                            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.child("Users").child(phone).exists()) {
                                        Users usersData = snapshot.child("Users").child(phone).getValue(Users.class);
                                        if (usersData.getPhone().equals(phone)) {
                                            if (usersData.getName().equals(nme)) {
                                                loadingBar.dismiss();
                                                String passwrd = usersData.getPassword().toString();

                                                CharSequence options[] = new CharSequence[]{
                                                        "OK"
                                                };
                                                AlertDialog.Builder builder = new AlertDialog.Builder(Forget.this);
                                                builder.setTitle("Your password is " + passwrd);
                                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent it = new Intent(Forget.this, LoginActivity.class);
                                                        startActivity(it);
                                                    }
                                                });
                                                builder.show();
                                            } else {
                                                Toast.makeText(Forget.this, "Username is wrong", Toast.LENGTH_LONG).show();
                                                loadingBar.dismiss();
                                            }
                                        }
                                    } else {
                                        Toast.makeText(Forget.this, "This phone number doesn't exist !", Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    else
                        {
                            Toast.makeText(Forget.this,"No internet",Toast.LENGTH_LONG).show();
                        }
                }
            }
        });
    }

    public boolean internet()
    {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


            return true;

        }

        else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {


            return false;
        }
        return false;
    }
}