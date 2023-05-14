package com.myproject.myvehicleapp.LoginActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.myproject.myvehicleapp.DriveMate;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utilities.Utility;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccountBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        createAccountBtnTextView = findViewById(R.id.create_account_text_view_btn);

        // Set click listeners for the loginBtn and createAccountBtnTextView
        loginBtn.setOnClickListener((v) -> loginUser());
        createAccountBtnTextView.setOnClickListener((v) -> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));
    }

    void loginUser() {
        // Retrieve the email and password from the input fields
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Validate the data entered by the user
        boolean isValidated = validateData(email, password);
        if (!isValidated) {
            return;
        }

        // Log in to the account in Firebase
        loginAccountInFirebase(email, password);
    }

    void loginAccountInFirebase(String email, String password) {
        // Get the Firebase Authentication instance and sign in with the email and password
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // Hide progress bar and enable loginBtn after the login process is complete
                changeInProgress(false);

                if (task.isSuccessful()) {
                    // Login is successful
                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                        // Go to the main activity
                        startActivity(new Intent(LoginActivity.this, DriveMate.class));
                        finish();
                    } else {
                        Utility.showToast(LoginActivity.this, "Email not verified. Please verify your email.");
                    }
                } else {
                    // Login failed
                    Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });
    }

    void changeInProgress(boolean inProgress) {
        // Show or hide the progress bar and loginBtn based on the inProgress parameter
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password) {
        // Validate the data entered by the user

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is invalid");
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password length is invalid");
            return false;
        }
        return true;
    }
}