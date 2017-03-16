package com.example.nicolas.ig2i_tp1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SecondActivity extends AppCompatActivity {

    String pseudo = "MissingNo.";

    @Bind(R.id.pseudo) TextView txtvPseudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);

        pseudo = getIntent().getExtras().getString("pseudo", "MissingNo.");
        txtvPseudo.setText(pseudo);
    }
}
