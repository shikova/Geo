package com.example.shikova.geo;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 123;
    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //Username
    private String mUserName;
    //GoogleApiClient
    //GoogleMap
    private GoogleMap myMap;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment=(MapFragment)getFragmentManager().findFragmentById(R.id.main_map);
        mapFragment.getMapAsync(this);
        //Fire Base Auth Instance
        mAuth=FirebaseAuth.getInstance();

        //FireBase Listener
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user=mAuth.getCurrentUser();

                if (user!=null)
                {   // TODO: 22/01/17
                    // User is signed in
                    Toast.makeText(MainActivity.this,"Welcome Back",Toast.LENGTH_LONG).show();
                    onSignedInInitiliaze(user.getDisplayName());
                }else {
                    onSignedOutCleanUp();
                    // User is signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder().setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }


            }
        };
    }

    private void onSignedOutCleanUp() {
        //Detache DataBase
        // TODO: 22/01/17
    }

    private void onSignedInInitiliaze(String displayName) {
        mUserName=displayName;
        //AtachFirebase
        }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);



    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.itemOut:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
    myMap=googleMap;

        try {
         boolean sucess =myMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MainActivity.this,R.raw.ac_map_style));
            if (!sucess)
            {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(-34, 151)));


    }
    }

