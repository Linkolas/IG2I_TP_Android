package com.example.nicolas.ig2i_tp1.tp2.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nicolas.ig2i_tp1.R;
import com.example.nicolas.ig2i_tp1.tp2.API.APICreator;
import com.example.nicolas.ig2i_tp1.tp2.API.ClientRequest;
import com.example.nicolas.ig2i_tp1.tp2.activity.UI.MessageRecyclerView;
import com.example.nicolas.ig2i_tp1.tp2.model.Conversation;
import com.example.nicolas.ig2i_tp1.tp2.model.Message;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationActivity extends RestActivity {

    int id_conversation = -1;
    int id_last_message = 0;
    @Bind(R.id.RVMessages)
    RecyclerView rv;
    @Bind(R.id.conversation_btnOK)
    Button btnOK;
    @Bind(R.id.conversation_edtMessage)
    EditText edtMessage;

    MessageRecyclerView messageRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_conversation);
        ButterKnife.bind(this);

        // On récupère l'ID de la conversation à afficher
        id_conversation = getIntent().getExtras().getInt("id", -1);
        if(id_conversation == -1) {
            // On annule tout si l'ID n'est pas valide !!
            gs.alerter("Erreur sur la conversation.");

            retournerAccueil();
            return;
        }

        // On initialise le RecyclerView (remplace ListView)
        messageRecyclerView = new MessageRecyclerView();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(messageRecyclerView);

        // Action du clic sur le bouton
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        // On récupère les messages
        requestMessages();
    }

    @Override
    public String urlPeriodique(String action) {

        String url = "";

        switch(action) {
            case "getMessages":

                url = "action=getMessages&idConv=" + id_conversation + "&idLastMessage=" + id_last_message;
                break;
        }

        return url;
    }

    private void sendMessage() {

        if (!Main_TP2.USE_RETROFIT) {

            envoiRequete("action=setMessage&idConv=" + id_conversation
                    + "&idLastMessage=" + id_last_message
                    + "&contenu=" + URLEncoder.encode(edtMessage.getText().toString())
                    , "setMessage");

        } else {

        }
    }

    private void requestMessages() {
        if (!Main_TP2.USE_RETROFIT) {

            //envoiRequete("action=getMessages&idConv=" + id_conversation + "&idLastMessage=" + id_last_message, "getMessages");

            requetePeriodique(5, "getMessages");

        } else {

            //ClientRequest request = ClientRequest.retrofit.create(ClientRequest.class);
            ClientRequest request = APICreator.getRetrofit(this);
            Call<JsonObject> connexion = request.getConversation(id_conversation, id_last_message);
            connexion.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    traiteReponse(response.body(), "getMessages");
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

        switch(action) {
            case "setMessage":
            case "getMessages":

                List<Message> messages = parseMessages(o.get("messages").getAsJsonArray());

                messageRecyclerView.messages.addAll(messages);
                messageRecyclerView.notifyDataSetChanged();

                if(o.get("idLastMessage") != null) {
                    id_last_message = o.get("idLastMessage").getAsInt();
                }

                break;
        }
    }

    private List<Message> parseMessages(JsonArray messagesJArray) {
        List<Message> messages = new ArrayList<>();

        for (JsonElement messageJElem :  messagesJArray) {
            JsonObject jobj =  messageJElem.getAsJsonObject();

            int id = jobj.get("id").getAsInt();
            String message = jobj.get("contenu").getAsString();
            String author = jobj.get("auteur").getAsString();
            int color = Color.parseColor(jobj.get("couleur").getAsString());

            Message msg = new Message(id, message, author, color);
            messages.add(msg);
        }

        return messages;
    }
}
