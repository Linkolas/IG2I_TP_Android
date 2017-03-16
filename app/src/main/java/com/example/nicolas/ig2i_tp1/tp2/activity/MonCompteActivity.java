package com.example.nicolas.ig2i_tp1.tp2.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.nicolas.ig2i_tp1.GlobalState;
import com.example.nicolas.ig2i_tp1.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MonCompteActivity extends RestActivity {

    @Bind(R.id.compte_btnOK)
    Button btnOK;

    @Bind(R.id.compte_edtPasse)
    EditText edtPasse;

    @Bind(R.id.compte_edtLogin)
    EditText edtLogin;

    @Bind(R.id.compte_listeAvatars)
    Spinner spinColor;

    @Override
    public void traiteReponse(JSONObject o, String action) {
        JsonParser parser = new JsonParser();
        JsonObject o2 = parser.parse(o.toString()).getAsJsonObject();

        traiteReponse(o2, action);
    }


    public void traiteReponse(JsonObject o, String action) {

        // Nothing here to do here

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte);
        ButterKnife.bind(this);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GlobalState) getApplication()).showToast("Not yet implemented");
                /*
                String login = edtLogin.getText().toString();
                String passe = edtPasse.getText().toString();
                String color = spinColor.getSelectedItem().toString();

                envoiRequete("action=setPseudo&pseudo=" + login, "setPseudo");
                envoiRequete("action=setPasse&passe=" + passe, "setPasse");
                envoiRequete("action=setCouleur&couleur=" + color, "setCouleur");
                */
            }
        });
    }


}
