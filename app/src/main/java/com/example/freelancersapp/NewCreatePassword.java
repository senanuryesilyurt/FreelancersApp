package com.example.freelancersapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class NewCreatePassword extends AppCompatActivity {

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newcreatepasswd);

        Button yeniParola=findViewById(R.id.yeniParolaGonder);
        final EditText email=findViewById(R.id.uyeEmail);
        //FirebaseAuth sınıfının referans olduğu nesneleri kullanabilmek için getInstance methodunu kullanıyoruz.
        auth = FirebaseAuth.getInstance();

        yeniParola.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String mail = email.getText().toString().trim();

            if (TextUtils.isEmpty(mail)) {
                Toast.makeText(getApplication(), "Lütfen email adresinizi giriniz", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.sendPasswordResetEmail(mail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(NewCreatePassword.this, "Yeni parola için gerekli bağlantı adresinize gönderildi!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(NewCreatePassword.this, "Mail gönderme hatası!", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        }
    });
}
}
