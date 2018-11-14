package es.upm.miw.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final String LOG_TAG = "MiW";

    private FirebaseLogin mFirebaseLogin;

    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fields
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);

        // Click listeners
        findViewById(R.id.buttonSignIn).setOnClickListener(this);
        findViewById(R.id.buttonExit).setOnClickListener(this);

        mFirebaseLogin = FirebaseLogin.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    // https://github.com/firebase/quickstart-android/blob/master/auth/app/src/main/java/com/google/firebase/quickstart/auth/java/AnonymousAuthActivity.java
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.buttonSignIn) {
            signInWithCredentials();
        } else if (i == R.id.buttonExit) {
            signOut();
            this.finish();
        }
    }

    private boolean validateLinkForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError(getString(R.string.field_required));
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError(getString(R.string.field_required));
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void signInWithCredentials() {
        if (!validateLinkForm()) {
            return;
        }

        // Get email and password from form
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        // Create EmailAuthCredential with email and password
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        mFirebaseLogin.signInWithCredential(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i(LOG_TAG, "signInWithCredentials:success");
                            FirebaseLogin.getInstance().currentUser();
                            Intent intent = new Intent(getApplicationContext(), DeliveryActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOG_TAG, "signInWithCredentials:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END signin_with_email_and_password]
    }

    private void signOut() {
        mFirebaseLogin.signOut();
    }
}