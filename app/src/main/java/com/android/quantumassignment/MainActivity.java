package com.android.quantumassignment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout rlLogin, rlSignUp, rlFooter, rlLoginScreen, rlSignUpScreen;
    TextView tvLogin, tvSignUp, tvFooter, tvRegisterNow, tvSignInn;
    EditText etName, etEmail, etEmailSignUp, etContact, etPassword, etPasswordSignUp;
    CheckBox cbSignUp;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        tvRegisterNow.setOnClickListener(this);
        tvSignInn.setOnClickListener(this);
        rlFooter.setOnClickListener(this);
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }
    }

    public void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        rlLogin = findViewById(R.id.rlLogin);
        rlSignUp = findViewById(R.id.rlSignUp);
        tvLogin = findViewById(R.id.tvLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        rlFooter = findViewById(R.id.rlFooter);
        tvFooter = findViewById(R.id.tvFooter);
        rlLoginScreen = findViewById(R.id.rlLoginScreen);
        rlSignUpScreen = findViewById(R.id.rlSignUpScreen);
        tvRegisterNow = findViewById(R.id.tvRegisterNow);
        tvSignInn = findViewById(R.id.tvSignInn);
        etName = findViewById(R.id.etName);
        etEmailSignUp = findViewById(R.id.etEmailSignUp);
        etContact = findViewById(R.id.etContact);
        etPasswordSignUp = findViewById(R.id.etPasswordSignUp);
        cbSignUp = findViewById(R.id.cbSignUp);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvRegisterNow:
                rlLogin.setBackgroundResource(R.drawable.rl_round_filled_white);
                tvLogin.setTextColor(R.color.black);
                rlSignUp.setBackgroundResource(R.drawable.rl_round_filled_orange);
                tvSignUp.setTextColor(R.color.white);
                rlLoginScreen.setVisibility(View.GONE);
                rlSignUpScreen.setVisibility(View.VISIBLE);
                tvFooter.setText("REGISTER");
                break;
            case R.id.tvSignInn:
                rlLogin.setBackgroundResource(R.drawable.rl_round_filled_orange);
                tvLogin.setTextColor(R.color.white);
                rlSignUp.setBackgroundResource(R.drawable.rl_round_filled_white);
                tvSignUp.setTextColor(R.color.black);
                rlLoginScreen.setVisibility(View.VISIBLE);
                rlSignUpScreen.setVisibility(View.GONE);
                tvFooter.setText("LOGIN");
                break;
            case R.id.rlFooter:
                String txt = tvFooter.getText().toString();
                //Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
                if (txt.equals("REGISTER")) {
                    String name = etName.getText().toString();
                    String email = etEmailSignUp.getText().toString();
                    String contact = etContact.getText().toString();
                    String password = etPasswordSignUp.getText().toString();
                    boolean chk = cbSignUp.isChecked();
                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(contact) || TextUtils.isEmpty(password) || chk == false) {
                        Toast.makeText(this, "Fill Details!", Toast.LENGTH_SHORT).show();
                    } else {
                        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } else {
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                        Toast.makeText(this, "Fill Details!", Toast.LENGTH_SHORT).show();
                    } else {
                        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(MainActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
        }
    }
}