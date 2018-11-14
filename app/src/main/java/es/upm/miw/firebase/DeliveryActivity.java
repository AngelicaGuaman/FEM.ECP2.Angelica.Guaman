package es.upm.miw.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import es.upm.miw.firebase.models.Delivery;
import es.upm.miw.firebase.models.Photo;
import es.upm.miw.firebase.models.User;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeliveryActivity extends AppCompatActivity {

    static final String LOG_TAG = "MiW";
    private static final String API_BASE_URL = "https://jsonplaceholder.typicode.com";

    private FirebaseLogin mFirebaseLogin;
    private FirebaseUser mFirebaseUser;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDeliveryDatabaseReference;

    private UserRESTAPIService apiUser;
    private PhotoRESTAPIService apiPhoto;

    static User userSelected;

    List<Photo> photoList = new ArrayList<>();
    ArrayList<String> photoArrayString = new ArrayList<>();
    Photo photoSelected;

    private SpinnerDialog mPhotoSpinnerDialog;

    private Button mBtnBuscarFoto;
    private Button mBtnPedirFoto;
    private Button mBtnVerUsuario;
    private Button mBtnRepartos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        mFirebaseLogin = FirebaseLogin.getInstance();
        mFirebaseUser = mFirebaseLogin.currentUser();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiUser = retrofit.create(UserRESTAPIService.class);
        apiPhoto = retrofit.create(PhotoRESTAPIService.class);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDeliveryDatabaseReference = mFirebaseDatabase.getReference().child("deliveries");

        ///// LISTA DE USUARIOS
        Call<User> call_async = apiUser.getUserById("1");

        // Asíncrona
        call_async.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                userSelected = response.body();
                if (null != userSelected) {
                    mostrarUsuario(userSelected);
                    Log.i(LOG_TAG, "obtenerUsuario => respuesta=" + userSelected);
                } else {
                    //tvRespuesta.setText(getString(R.string.strError));
                    Log.i(LOG_TAG, getString(R.string.strErrorUser));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(
                        getApplicationContext(),
                        "ERROR: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
                Log.e(LOG_TAG, t.getMessage());
            }
        });

        ///// LISTA DE FOTOS
        Call<List<Photo>> call_async_Photo = apiPhoto.getPhotos();

        // Asíncrona
        call_async_Photo.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                photoList = response.body();
                if (null != photoList) {
                    photoSelected = photoList.get(0);
                    mostrarFoto(photoSelected);
                    for (Photo photo : photoList) {
                        Log.i(LOG_TAG, "obtenerFoto => respuesta=" + photo.getTitle());
                        photoArrayString.add(photo.getTitle());
                    }
                    Log.i(LOG_TAG, "obtenerPhotos => respuesta=" + photoList);
                } else {
                    //tvRespuesta.setText(getString(R.string.strError));
                    Log.i(LOG_TAG, getString(R.string.strErrorPhoto));
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Toast.makeText(
                        getApplicationContext(),
                        "ERROR: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
                Log.e(LOG_TAG, t.getMessage());
            }
        });

        mPhotoSpinnerDialog = new SpinnerDialog(DeliveryActivity.this, photoArrayString, "Select or Search Photo", R.style.DialogAnimations_SmileWindow, "Close Button Text");

        mPhotoSpinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                Toast.makeText(DeliveryActivity.this, item + "  " + position + "", Toast.LENGTH_SHORT).show();
                photoSelected = photoList.get(position);
                mostrarFoto(photoSelected);
            }
        });

        mBtnBuscarFoto = (Button) findViewById(R.id.btnBuscarFoto);

        mBtnBuscarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoSpinnerDialog.showSpinerDialog();
            }
        });

        mBtnPedirFoto = (Button) findViewById(R.id.btnPedirFoto);

        mBtnPedirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delivery delivery = new Delivery(userSelected, photoSelected, "", "");
                mDeliveryDatabaseReference.push().setValue(delivery);
                Toast.makeText(DeliveryActivity.this, "Se ha añadido correctamente el pedido", Toast.LENGTH_SHORT).show();
            }
        });


        mBtnVerUsuario = (Button) findViewById(R.id.btnVerUsuario);

        mBtnVerUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "Ver detalles de usuario");
                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(intent);
            }
        });


        mBtnRepartos = (Button) findViewById(R.id.btnRepartos);

        mBtnRepartos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "Ver Repartos");
                Intent intent = new Intent(getApplicationContext(), DeliveryList.class);
                startActivity(intent);
            }
        });
    }

    private void mostrarFoto(Photo photo) {
        ((TextView) findViewById(R.id.titleFoto)).setText(photo.getTitle());
        ((TextView) findViewById(R.id.albumFoto)).setText(String.valueOf(photo.getAlbumId()));

        ImageView mImageView = (ImageView) findViewById(R.id.photoImage);

        if (photo.getThumbnailUrl() != null || !photo.getThumbnailUrl().isEmpty()) {
            Picasso.get()
                    .load(photo.getThumbnailUrl())
                    .placeholder(R.drawable.firebase_lockup_400)
                    .error(R.drawable.common_google_signin_btn_icon_dark)
                    .into(mImageView);
        }
    }

    private void mostrarUsuario(User usuario) {
        ((TextView) findViewById(R.id.userName)).setText(usuario.getName());
        ((TextView) findViewById(R.id.userEmail)).setText(usuario.getEmail());

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mFirebaseUser == null) {
            this.finish();
        }
    }
}