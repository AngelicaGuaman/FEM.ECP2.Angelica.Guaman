package es.upm.miw.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseLogin {

    private static FirebaseLogin instance = null;

    private FirebaseAuth mAuth;

    private FirebaseLogin() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseLogin getInstance() {
        if (instance == null) {
            instance = new FirebaseLogin();
        }
        return instance;
    }

    public FirebaseUser currentUser() {
        return mAuth.getCurrentUser();
    }

    public Task<AuthResult> signInWithCredential(String email, String password) {
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        return mAuth.signInWithCredential(credential);
    }

    public void signOut() {
        mAuth.signOut();
    }
}
