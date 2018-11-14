package es.upm.miw.firebase;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import es.upm.miw.firebase.models.User;

public class UserProfileActivity extends Activity {

    static final String LOG_TAG = "MiW";

    private FirebaseLogin mFirebaseLogin;
    private FirebaseUser mFirebaseUser;
    private User userSelected ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        mFirebaseLogin = FirebaseLogin.getInstance();
        mFirebaseUser = mFirebaseLogin.currentUser();
        userSelected = DeliveryActivity.userSelected;

        ((TextView) findViewById(R.id.userName)).setText(userSelected.getName());
        ((TextView) findViewById(R.id.userEmail)).setText(userSelected.getEmail());
        ((TextView) findViewById(R.id.userCompany)).setText(userSelected.getCompany().getName());
        ((TextView) findViewById(R.id.userAddress)).setText(userSelected.getAddress().getStreet());

    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
