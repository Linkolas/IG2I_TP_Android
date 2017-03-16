package com.example.nicolas.ig2i_tp1.tp2.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.nicolas.ig2i_tp1.R;
import com.example.nicolas.ig2i_tp1.tp2.API.APICreator;
import com.example.nicolas.ig2i_tp1.tp2.API.ClientRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main_TP2 extends RestActivity /*AppCompatActivity*/ {

    static public final boolean USE_RETROFIT = false;


    @Bind(R.id.login_edtLogin)
    EditText edtLogin;
    @Bind(R.id.login_edtPasse)
    EditText edtPasse;
    @Bind(R.id.login_cbRemember)
    CheckBox cbxRemember;
    @Bind(R.id.login_btnOK)
    Button btnOK;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        InitInputs();

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = edtLogin.getText().toString();
                String passe = edtPasse.getText().toString();

                if (cbxRemember.isChecked()) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("login", login);
                    editor.putString("passe", passe);
                    editor.commit();
                }

                if (!Main_TP2.USE_RETROFIT) {
                    envoiRequete("action=connexion&login=" + login + "&passe=" + passe, "connexion");
                } else {
                    //ClientRequest request = ClientRequest.retrofit.create(ClientRequest.class);
                    ClientRequest request = APICreator.getRetrofit(getApplicationContext());
                    Call<JsonObject> connexion = request.connexion(login, passe);
                    connexion.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            traiteReponse(response.body(), "connexion");
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.i("WOLOLO", "failure");
                        }
                    });
                }
            }
        });
    }

    private void InitInputs() {
        edtLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                gererBouton();
            }
        });

        edtPasse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                gererBouton();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (prefs.getBoolean("remember", true)) {
            edtLogin.setText(prefs.getString("login", ""));
            edtPasse.setText(prefs.getString("passe", ""));
            cbxRemember.setChecked(true);
        }

        gererBouton();
    }

    private void gererBouton() {
        if (isInputOK() && gs.verifReseau()) {
            btnOK.setEnabled(true);
        } else {
            btnOK.setEnabled(false);
        }
    }

    private boolean isInputOK() {
        if (edtLogin.getText().toString().length() < 1) {
            return false;
        }

        if (edtPasse.getText().toString().length() < 1) {
            return false;
        }

        return true;
    }

    @Override
    public void traiteReponse(JSONObject o, String action) {
        JsonParser parser = new JsonParser();
        JsonObject o2 = parser.parse(o.toString()).getAsJsonObject();

        traiteReponse(o2, action);
    }

    public void traiteReponse(JsonObject o, String action) {

        switch (action) {
            case "connexion":
                boolean connecte = o.get("connecte").getAsBoolean();
                if (!connecte) {
                    gs.alerter("Identifiants incorrects.");
                    return;
                }

                Intent intent = new Intent(this, ConversationsActivity.class);
                startActivity(intent);

                break;
        }

    }
}
