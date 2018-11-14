package es.upm.miw.firebase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import es.upm.miw.firebase.models.Delivery;

public class DeliveryList extends AppCompatActivity implements View.OnClickListener {

    static final String LOG_TAG = "MiW";
    static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseLogin mFirebaseLogin;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDeliveryDatabaseReference;
    private ChildEventListener mChildEventListener;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mDeliveryStorageReference;

    private ListView lvListado;
    private Button btnAnhadirQueja;

    private ImageButton mPhotoQueja;

    DeliveryAdapter mDeliveryAdapter;
    List<Delivery> deliveryList;

    Delivery itemSelected;

    private Uri mImageUri;

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos);

        mFirebaseLogin = FirebaseLogin.getInstance();

        lvListado = (ListView) findViewById(R.id.lvListadoElementos);

        mDeliveryDatabaseReference = FirebaseDatabase.getInstance().getReference().child("deliveries");

        mDeliveryStorageReference = FirebaseStorage.getInstance().getReference("fotosQuejas");


        deliveryList = new ArrayList<>();

        mDeliveryAdapter = new DeliveryAdapter(this, R.layout.item_lista, deliveryList);
        lvListado.setAdapter(mDeliveryAdapter);
        lvListado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.w(LOG_TAG, "seleccionando items");
                itemSelected = (Delivery) lvListado.getItemAtPosition(position);
                Toast.makeText(DeliveryList.this, "Item seleccionado: " + itemSelected.getId(),
                        Toast.LENGTH_SHORT).show();
                mostrarPedido(itemSelected);

            }
        });

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Delivery delivery = dataSnapshot.getValue(Delivery.class);
                mDeliveryAdapter.add(delivery);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        mDeliveryDatabaseReference.addChildEventListener(mChildEventListener);

        btnAnhadirQueja = (Button) findViewById(R.id.btnAnhadirQueja);
        btnAnhadirQueja.setOnClickListener(this);

        mPhotoQueja = (ImageButton) findViewById(R.id.photoQueja);
        mPhotoQueja.setOnClickListener(this);

        mImageView = (ImageView) findViewById(R.id.showPhoto);


    }

    @Override
    public void onStart() {
        super.onStart();
        if (mFirebaseLogin.currentUser() == null) {
            this.finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnAnhadirQueja) {
            Log.i(LOG_TAG, "SUBIENDO FOTO A FIREBASE STORAGE");
            StorageReference fileReference = mDeliveryStorageReference.child(System.currentTimeMillis() + ".jpeg");
            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    itemSelected.setFotoQueja(urlTask.getResult().toString());
                    EditText incidenciaTxt = findViewById(R.id.incidencia);
                    String incidencia = incidenciaTxt.getText().toString();
                    itemSelected.setIncidencia(incidencia);
                    mDeliveryDatabaseReference.setValue(itemSelected);
                    Toast.makeText(DeliveryList.this, "Se ha subido correctamente la foto",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else if (id == R.id.photoQueja) {
            Log.i(LOG_TAG, "SE QUIERE SUBIR UNA FOTO A FIREBASE STORAGE");
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Seleccione una imagen"), PICK_IMAGE_REQUEST);
        }
    }

    private void mostrarPedido(Delivery delivery) {
        ((TextView) findViewById(R.id.refPedido)).setText(delivery.getId());
        ((TextView) findViewById(R.id.fecha)).setText(delivery.getFechaRegistro());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(mImageView);
        }
    }
}
