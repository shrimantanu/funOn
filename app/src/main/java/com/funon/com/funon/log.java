package com.funon.com.funon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class log extends AppCompatActivity {
    private Button b1;
    private EditText t1,t2;
    private ImageView i1,i2;
    SessionManager sessionManager=new SessionManager();
    TextInputLayout t11,t12;

    final String TAG="signup";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        mAuth = FirebaseAuth.getInstance();
        b1=findViewById(R.id.button2);
        t1=findViewById(R.id.editText);
        t2=findViewById(R.id.editText2);
        t11=findViewById(R.id.email_input);
        t12=findViewById(R.id.password);
        i1=findViewById(R.id.i1);
        i2=findViewById(R.id.i2);
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try{
                    t1.setVisibility(View.VISIBLE);
                    t2.setVisibility(View.VISIBLE);

                    t11.setVisibility(View.VISIBLE);
                    t12.setVisibility(View.VISIBLE);
                    b1.setVisibility(View.VISIBLE);
                    i1.setVisibility(View.GONE);
                    i2.setVisibility(View.GONE);
                }
                catch (Exception e)
                {

                }
            }
        });
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try{
                    t1.setVisibility(View.VISIBLE);
                    t2.setVisibility(View.VISIBLE);

                    t11.setVisibility(View.VISIBLE);
                    t12.setVisibility(View.VISIBLE);
                    b1.setVisibility(View.VISIBLE);
                    b1.setText("Sign up");
                    i1.setVisibility(View.GONE);
                    i2.setVisibility(View.GONE);
                    Toast.makeText(log.this, b1.getText(),
                            Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {

                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email=t1.getText().toString();
                final String password=t2.getText().toString();
                try{
                    if(b1.getText().toString().equals("Sign up"))
                        createAccount(email,password);
                    else
                        signIn(email,password);
                }
                catch (Exception e)
                {

                }
            }
        });



    }


    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();

        updateUI(currentUser);
    }
    private void createAccount(String email,String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(log.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });


    }
    private void signIn(String email,String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            sessionManager.setPrefs(log.this,"isloggedin","true");
                            Intent intent = new Intent(log.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(log.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
if(user==null){
    sessionManager.setPrefs(log.this,"isloggedin","false");
}


    }

}
