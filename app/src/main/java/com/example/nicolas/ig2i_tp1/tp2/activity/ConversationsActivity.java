package com.example.nicolas.ig2i_tp1.tp2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.nicolas.ig2i_tp1.R;
import com.example.nicolas.ig2i_tp1.SecondActivity;
import com.example.nicolas.ig2i_tp1.tp2.API.APICreator;
import com.example.nicolas.ig2i_tp1.tp2.API.ClientRequest;
import com.example.nicolas.ig2i_tp1.tp2.activity.UI.ConversationAdapter;
import com.example.nicolas.ig2i_tp1.tp2.model.Conversation;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationsActivity extends RestActivity {

    @Bind(R.id.choixConversation_choixConv)
    Spinner spinner;
    @Bind(R.id.choixConversation_btnOK)
    Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_conversation);
        ButterKnife.bind(this);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putInt("id", ((Conversation) spinner.getSelectedItem()).getId());

                Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        if (!Main_TP2.USE_RETROFIT) {

            envoiRequete("action=getConversations", "getConversations");

        } else {

            ClientRequest request = APICreator.getRetrofit(this);
            Call<JsonObject> connexion = request.getConversations();
            connexion.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    traiteReponse(response.body(), "getConversations");
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.i("WOLOLO", "failure");
                }
            });
        }
    }


    @Override
    public void traiteReponse(JSONObject o, String action) {
        JsonParser parser = new JsonParser();
        JsonObject o2 = parser.parse(o.toString()).getAsJsonObject();

        traiteReponse(o2, action);
    }

    public void traiteReponse(JsonObject o, String action) {

        switch (action) {
            case "getConversations":

                boolean connecte = o.get("connecte").getAsBoolean();
                if (!connecte) {
                    gs.alerter("Session dépassée");

                    retournerAccueil();
                    return;
                }

                List<Conversation> conversations = parseConversations(o.get("conversations").getAsJsonArray());

                //ArrayAdapter<Conversation> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, conversations);
                ArrayAdapter<Conversation> adapter = new ConversationAdapter(this, android.R.layout.simple_spinner_dropdown_item, conversations);

                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinner.setAdapter(adapter);

                break;
        }
    }

    private List<Conversation> parseConversations(JsonArray conversationsJArray) {
        List<Conversation> conversations = new ArrayList<>();

        for (JsonElement conversationJElem : conversationsJArray) {
            JsonObject jobj = conversationJElem.getAsJsonObject();

            int id = jobj.get("id").getAsInt();
            int active = jobj.get("active").getAsInt();
            String theme = jobj.get("theme").getAsString();

            Conversation conv = new Conversation(id, theme, active);
            conversations.add(conv);
        }

        return conversations;
    }
}
