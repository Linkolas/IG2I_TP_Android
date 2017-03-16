package com.example.nicolas.ig2i_tp1.tp2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nicolas.ig2i_tp1.GlobalState;
import com.example.nicolas.ig2i_tp1.R;
import com.example.nicolas.ig2i_tp1.SettingsActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public abstract class RestActivity extends FinishAllReceiver {

    protected GlobalState gs;

    // Une classe capable de faire des requêtes simplement
    // Si elle doit faire plusieurs requetes,
    // comment faire pour controler quelle requete se termine ?
    // on passe une seconde chaine à l'appel asynchrone

    public void envoiRequete(String qs, String action) {
        // En instanciant à chaque fois, on peut faire autant de requetes que l'on veut...

        RestRequest req = new RestRequest(this);
        req.execute(qs, action);
    }

    public String urlPeriodique(String action) {
        // devrait être abstraite, mais dans ce cas doit être obligatoirement implémentée...
        // On pourrait utiliser une interface ?
        return "";
    }


    private List<Timer> timers = new ArrayList<>();

    // http://androidtrainningcenter.blogspot.fr/2013/12/handler-vs-timer-fixed-period-execution.html
    // Try AlarmManager running Service
    // http://rmdiscala.developpez.com/cours/LesChapitres.html/Java/Cours3/Chap3.1.htm
    // La requete elle-même sera récupérée grace à l'action demandée dans la méthode urlPeriodique
    public void requetePeriodique(int periode, final String action) {

        TimerTask doAsynchronousTask;
        final Handler handler = new Handler();
        Timer timer = new Timer();
        timers.add(timer);

        doAsynchronousTask = new TimerTask() {

            @Override
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        envoiRequete(urlPeriodique(action), action);
                    }
                });

            }

        };

        timer.schedule(doAsynchronousTask, 0, 1000 * periode);

    }

    public void requetePeriodiqueStop() {
        for (Timer timer : timers) {
            timer.cancel();
        }
    }


    public abstract void traiteReponse(JSONObject o, String action);
    // devra être implémenté dans la classe fille


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gs = (GlobalState) getApplication();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requetePeriodiqueStop();
    }

    //region MENU HAMBURGER

    // Utilisé pour les tests de mise à jour d'une notification
    int compteur = 1;

    /**
     * Nécessaire pour afficher le menu.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Gère les actions du menu.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case R.id.action_account:
                if (this.getClass().getSimpleName().equals("MonCompteActivity")) {
                    break;
                }

                intent = new Intent(this, MonCompteActivity.class);
                startActivity(intent);
                break;

            case R.id.preferences:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;

            case R.id.quitter:
                quitterApp();
                break;

            case R.id.tester_notifs:
                // On met à jour la notification (en montant le compteur affiché)
                gs.notifier(Main_TP2.class, "Notif", "Numéro " + compteur++, 1, "ticker_msg");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    private void quitterApp() {
        // On crée une notification (comme demandé dans le sujet)
        gs.notifier(Main_TP2.class, "Titre de notif", "Message de notif", 1, "ticker_message");

        // On ferme toutes les activités
        FinishAllReceiver.closeAllActivities(this);

        // On arrête le processus
        //      ATTENTION : La notification disparaîtra car processus éteint.
        //System.exit(1); // BAD PRACTICE, DO THIS:
        finishAndRemoveTask();
    }

    protected void retournerAccueil() {
        closeAllActivities(this);

        Intent intent = new Intent(this, Main_TP2.class);
        startActivity(intent);

        finish();
    }
}