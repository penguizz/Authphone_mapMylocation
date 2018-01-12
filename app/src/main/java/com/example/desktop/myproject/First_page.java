package com.example.desktop.myproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.TimeUnit;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


public class First_page extends AppCompatActivity {
    private static final String TAG = "PhoneLogin";
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    ImageView imageLogo;
    EditText editTextphone_number,editTextOTP,editTextpassword;
    Button buttonPhoneVerify,buttonOTPVerify,buttonback_to_login,buttonSavepassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        editTextphone_number = (EditText) findViewById(R.id.editTextphone_number);
        buttonback_to_login = (Button) findViewById(R.id.buttonback_to_login);
        imageLogo = (ImageView)findViewById(R.id.imageView2);
        editTextOTP = (EditText) findViewById(R.id.editTextOTP);
        buttonOTPVerify = (Button)findViewById(R.id.buttonOTPVerify);
        mAuth = FirebaseAuth.getInstance();
        buttonPhoneVerify = (Button) findViewById(R.id.buttonPhoneVerify);
        editTextpassword = (EditText) findViewById(R.id.editTextpassword);
        buttonSavepassword = (Button) findViewById(R.id.buttonSavepassword);


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;
                Toast.makeText(First_page.this,"Verification Complete",Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // Log.w(TAG, "onVerificationFailed", e);
                Toast.makeText(First_page.this,"Verification Failed",Toast.LENGTH_SHORT).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(First_page.this,"InValid Phone Number",Toast.LENGTH_SHORT).show();
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(First_page.this,"กำลังส่งรหัสยืนยันไปยังหมายเลขโทรศัพท์ที่ระบุทาง SMS",Toast.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                editTextphone_number.setVisibility(View.GONE);
                buttonPhoneVerify.setVisibility(View.GONE);
                buttonback_to_login.setVisibility(View.GONE);
                imageLogo.setVisibility(View.GONE);
                editTextOTP.setVisibility(View.VISIBLE);
                buttonOTPVerify.setVisibility(View.VISIBLE);
                editTextpassword.setVisibility(View.GONE);
                buttonSavepassword.setVisibility(View.GONE);
                // ...
            }
        };

        buttonPhoneVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(First_page.this);
                builder.setMessage("รับขนมจีบซาลาเปาเพิ่มมั้ยครับ?");
                builder.setPositiveButton("รับ", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                editTextphone_number.getText().toString(),
                                60,
                                java.util.concurrent.TimeUnit.SECONDS,

                                First_page.this,
                                mCallbacks);
                    }
                });
                builder.setNegativeButton("ไม่รับ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                    }
                });
                builder.show();
            }
        });


        buttonOTPVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, editTextOTP.getText().toString());
                // [END verify_with_code]
                signInWithPhoneAuthCredential(credential);
            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Log.d(TAG, "signInWithCredential:success");
                            startActivity(new Intent(First_page.this,Register.class));
                            Toast.makeText(First_page.this,"Verification Done",Toast.LENGTH_SHORT).show();
                            // ...
                        } else {
                            // Log.w (TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(First_page.this,"Invalid Verification",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}