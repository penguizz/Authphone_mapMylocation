package com.example.desktop.myproject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private Button buttonRegister;
    private EditText editTextfirst_name,editTextlast_name,editTextemail,editTextpassword;

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
//    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        //มีข้อมูลแล้วไป map เลย
        if(mAuth.getCurrentUser() != null){
            //profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(), MapActivity.class));
        }

        buttonRegister = (Button) findViewById(R.id.b_Rester);
        editTextfirst_name = (EditText) findViewById(R.id.editTextfirst_name);
        editTextlast_name = (EditText) findViewById(R.id.editTextlast_name);
        editTextemail = (EditText) findViewById(R.id.editTextemail);
        editTextpassword = (EditText) findViewById(R.id.editTextpassword);

        buttonRegister.setOnClickListener(this);
//        if(isServicesOK()){
//            init();
//        }
    }
    @Override
    public void onClick(View v) {
        if(v == buttonRegister && isServicesOK()){
            registerUser();
            init();
        }
    }
    private void init(){
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Register.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Register.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    private void registerUser(){
        String first_name =  editTextfirst_name.getText().toString().trim();
        String last_name =  editTextlast_name.getText().toString().trim();
        String emaill =  editTextemail.getText().toString().trim();
        String password =  editTextpassword.getText().toString().trim();

        if(TextUtils.isEmpty(first_name)){
            //first_name is empty
            Toast.makeText(this,"กรุณากรอกชื่อ",Toast.LENGTH_SHORT).show();
            //stopping the function executoin futher
            return;
        }
        if(TextUtils.isEmpty(last_name)){
            //last_name is empty
            Toast.makeText(this,"กรุณากรอกนามสกุล",Toast.LENGTH_SHORT).show();
            //stopping the function executoin futher
            return;
        }
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


        //create new user
        mAuth.createUserWithEmailAndPassword(emaill,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //user is successfully register and logged in
                            //we will start the profile activity here
                            //right now lets display a toast only
                            Toast.makeText(Register.this,"ลงทะเบียนสำเร็จ",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MapActivity.class));
                        }else {
                            Toast.makeText(Register.this,"ลงทะเบียนไม่สำเร็จ",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
