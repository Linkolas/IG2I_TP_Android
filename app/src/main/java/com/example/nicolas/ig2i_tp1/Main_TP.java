package com.example.nicolas.ig2i_tp1;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Main_TP extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.btnOK) Button btnOK;
    @Bind(R.id.btnToast) Button btnToast;
    @Bind(R.id.btnAlert) Button btnAlert;
    @Bind(R.id.btnNotif) Button btnNotif;

    @Bind(R.id.inputNom) EditText inputNom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_linear);
        ButterKnife.bind(this);

        Log.i("Main_TP", "OnCreate");

        btnOK.setOnClickListener(this);
        btnToast.setOnClickListener(this);
        btnAlert.setOnClickListener(this);
        btnNotif.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String pseudo = prefs.getString("login", "MissingNo.");

        inputNom.setText(pseudo);
    }

    void showToast(String message) {
        //Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        ((GlobalState) getApplication()).showToast(message);
    }

    void showAlert(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Alerte !")
                .setMessage(message)
                .show();
    }

    void showNotif(String message) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo48)
                        .setContentTitle("Notification")
                        .setContentText(message);

        // Sets an ID for the notification
        int mNotificationId = 001;

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    String getInputNom() {
        return inputNom.getText().toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnToast:
                showToast(getInputNom());
                break;
            case R.id.btnAlert:
                showAlert(getInputNom());
                break;
            case R.id.btnNotif:
                showNotif(getInputNom());
                break;

            case R.id.btnOK:
                Bundle bundle = new Bundle();
                bundle.putString("pseudo", getInputNom());

                Intent intent = new Intent(this, SecondActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.preferences:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }

        return false;
    }
}
