package com.example.freelancersapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginPage extends AppCompatActivity  {

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private String userName;
    private String userPassword;

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 0 ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    SignInButton signInButton;
    private GoogleSignInApi googleSignInApi;
    private Button signOutButton;
    private TextView nameTextView;
    private TextView emailTextView;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        TextView signupText = findViewById(R.id.signupText);
        Button loginButton = findViewById(R.id.loginButton);
        final EditText userName = findViewById(R.id.loginEmail);
        final EditText userPassword = findViewById(R.id.loginPasswd);
        Button SifremiUnuttum = findViewById(R.id.yeniSifreButton);
        signInButton = findViewById(R.id.sign_in_button);

        auth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userName.getText().toString();
                String parola = userPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter your email address", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(parola)) {
                    Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
                } else if (parola.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Parola en az 6 haneli olmalıdır", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(email, parola).addOnCompleteListener(LoginPage.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Intent i = new Intent(LoginPage.this, MainActivity.class);
                                        startActivity(i);
                                        finish();

                                    } else {
                                        // hata
                                        //Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(LoginPage.this, "LOGIN HATA.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                }
            }
        });

        //Geçerli bir yetkilendirme olup olmadığını kontrol ediyoruz.
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginPage.this, MainActivity.class));
        }

        //SignUp page yönlendirme
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(i);
                finish();
            }
        });

        //Şifremi Unuttum kısmı
        SifremiUnuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this, NewCreatePassword.class));
            }
        });
    }
}



