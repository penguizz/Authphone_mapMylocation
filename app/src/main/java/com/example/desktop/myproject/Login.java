package com.example.desktop.myproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private Button buttonSignin;
    private EditText editTextemail,editTextpassword;
    private TextView textViewSignup;

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

//        if(mAuth.getCurrentUser() != null){
//            //profile activity here
//            finish();
//            startActivity(new Intent(getApplicationContext(), MapActivity.class));
//        }
        buttonSignin = (Button) findViewById(R.id.b_Signin);
        editTextemail =(EditText) findViewById(R.id.editTextemail);
        editTextpassword =(EditText) findViewById(R.id.editTextpassword);
        textViewSignup =(TextView) findViewById(R.id.textViewSignup);

        buttonSignin.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == buttonSignin){
            userLogin();
        }
        if(v == textViewSignup){
            finish();
            startActivity(new Intent(this, First_page.class));
        }
    }

    private void userLogin(){
        String emaill =  editTextemail.getText().toString().trim();
        String password =  editTextpassword.getText().toString().trim();

        if(TextUtils.isEmpty(emaill)){
            Toast.makeText(this,"กรุณากรอกอีเมล",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"กรุณากรอกรหัสผ่าน",Toast.LENGTH_SHORT).show();
            return;
        }

        //if validations are ok
        //we will first show a progressbar

        progressDialog.setMessage("สมัครสมาชิก");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(emaill,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), MapActivity.class));
                        }
                    }
                });
    }
}
