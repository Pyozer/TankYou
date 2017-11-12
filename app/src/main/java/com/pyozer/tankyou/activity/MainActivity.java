package com.pyozer.tankyou.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.pyozer.tankyou.R;
import com.pyozer.tankyou.util.PrefUserManager;

/**
 * Activity principale contenant le menu du jeu
 */
public class MainActivity extends BaseActivity {

    private PrefUserManager prefUserManager;
    private Dialog dialogRules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.jouer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
                finish();
            }
        });
        findViewById(R.id.scores).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScoresActivity.class));
            }
        });
        findViewById(R.id.stats).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StatsActivity.class));
            }
        });
        findViewById(R.id.rules).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRulesDialog();
            }
        });
        findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialog();
            }
        });

        // Si le pseudo n'a pas été renseigné on le demande
        prefUserManager = new PrefUserManager(this);
        if(!prefUserManager.isUserExists()) {
            askUsername();
        }

        loadRulesDialog();
    }

    /**
     * Permet de demande à l'utilisateur son pseudo
     * On stock le pseudo dans les SharedPreferences
     */
    private void askUsername() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Récupère notre vue pour le dialog
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.username_dialog, null, false);
        // Récupère l'EditText de la vue
        final EditText input = viewInflated.findViewById(R.id.input_username);
        // Spécifie on dialog notre vue
        builder.setView(viewInflated);

        builder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        // Evite que l'utilisateur quitte le dialog sans avoir mis de pseudo
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();
        // Override notre listener pour enregistrer pour pouvoir géré les erreurs (champs vide)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = input.getText().toString().trim();
                if(TextUtils.isEmpty(username)) {
                    input.setError(getString(R.string.input_required));
                } else {
                    prefUserManager.saveUsername(username);
                    dialog.dismiss();
                }
            }
        });
    }

    /**
     * Charge le Dialog pour afficher les règles
     * Permet de charger la vue du Dialog au lancement de l'activity
     */
    private void loadRulesDialog() {
        dialogRules = new Dialog(this, R.style.AppTheme_NoActionBar);
        // Récupération de la vue du dialog
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewRulesDialog = inflater.inflate(R.layout.rules_dialog, null);
        dialogRules.setContentView(viewRulesDialog);
        // Règles du jeu (HTML)
        String rules_text = "<html><head><style>p {color: #ffffff; font-size: 20px; text-align: justify;}</style></head><body>" +
                "<p>Le but est de contrôler un <strong>tank</strong> et d'éviter les <strong>obstacles</strong> qui se déplacent sur l'écran.<br /><br />" +
                "Le jeu se joue grâce à l'<strong>accéléromètre</strong> pour pouvoir se déplacer.<br />" +
                "Pour avancer il suffit de pencher son téléphone en avant ou en arrière. Pour tourner à gauche ou à droite il faut pencher le téléphone sur le côté<br /><br />" +
                "Vous avez la possibilité avec votre tank de <strong>tirer des missiles</strong> pour détruire des obstacles.<br />" +
                "Entre chaque tire, il y un temps d'attente de <strong>1.5sec</strong> durant laquel le tank recharge son missile.<br /><br />" +
                "A chaque obstacle détruit vous gagnez <strong>un point</strong>. Si vous touchez un obstacle, vous perdez la partie.</p></body></html>";

        // Créer notre webview
        WebView mWebView = viewRulesDialog.findViewById(R.id.rules_text);
        mWebView.loadData(rules_text, "text/html; charset=UTF-8;", null);
        mWebView.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        viewRulesDialog.findViewById(R.id.rules_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogRules.dismiss();
            }
        });

    }

    /**
     * Affiche le dialog pour les règles du jeu
     */
    private void showRulesDialog() {
        if(!dialogRules.isShowing())
            dialogRules.show();
    }

    /**
     * Affiche le dialog pour A propos
     */
    private void showAboutDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.about_dialog);
        dialog.show();
    }

}
