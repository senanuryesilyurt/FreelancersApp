package com.example.freelancersapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilPage extends AppCompatActivity {

        private TextView userNameTxt;
        private Button changeEmailBttn;
        private Button changePasswordBttn;
        private Button signOutBttn;
        private FirebaseAuth auth;
        private FirebaseUser firebaseUser;
        private FirebaseAuth.AuthStateListener authListener;
        private String str;

        @Override
        public void onBackPressed() {
        }

        @Override
        public void onStart() {
            super.onStart();
            auth.addAuthStateListener(authListener);
        }

        @Override
        public void onStop() {
            super.onStop();
            if (authListener != null) {
                auth.removeAuthStateListener(authListener);
            }
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            auth = FirebaseAuth.getInstance();
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // get current user

            authListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user == null) { // when auth state change
                        finish();
                    }
                }
            };

            userNameTxt = (TextView)findViewById(R.id.userNameTxt);
            changeEmailBttn = (Button) findViewById(R.id.changeEmailBttn);
            changePasswordBttn = (Button)findViewById(R.id.changePasswordBttn);
            signOutBttn = (Button)findViewById(R.id.signOutBttn);

            userNameTxt.setText("Kullanıcı Adı:" + " " + auth.getCurrentUser().getEmail());

            signOutBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signOutFunc(); // sign out
                }
            });

            changeEmailBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str = "Lütfen yeni e-posta adresini giriniz.";
                    changeEmailOrPasswordFunc(str,true);
                }
            });

            changePasswordBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str = "Lütfen yeni şifreyi giriniz.";
                    changeEmailOrPasswordFunc(str,false);
                }
            });

        }

        private void signOutFunc() {

            auth.signOut();
        }

        private void changeEmailOrPasswordFunc(String title, final boolean option) {

            AlertDialog.Builder builder = new AlertDialog.Builder(ProfilPage.this);
            final EditText edit = new EditText(ProfilPage.this);
            builder.setPositiveButton(getString(R.string.change_txt), null);
            builder.setNegativeButton(getString(R.string.close_txt), null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            edit.setLayoutParams(lp);
            if(!option){  // password type
                edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            builder.setTitle(title);
            builder.setView(edit);

            final AlertDialog mAlertDialog = builder.create();
            mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialog) {

                    Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            if(edit.getText().toString().isEmpty()){

                                edit.setError("Lütfen ilgili alanı doldurunuz!");

                            }else{

                                if(option){ // email change

                                    changeEmail();

                                }else{  // password change

                                    changePassword();

                                }
                            }

                        }
                    });
                }

                private void changePassword() {

                    firebaseUser.updatePassword(edit.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ProfilPage.this, "Şifre değiştirildi.", Toast.LENGTH_LONG).show();
                                        signOutFunc();
                                    } else {
                                        edit.setText("");
                                        Toast.makeText(ProfilPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

                private void changeEmail() {

                    firebaseUser.updateEmail(edit.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ProfilPage.this, "E-posta değiştirildi.", Toast.LENGTH_LONG).show();
                                        signOutFunc();

                                    } else {
                                        edit.setText("");
                                        Toast.makeText(ProfilPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            });
            mAlertDialog.show();

        }
    }

