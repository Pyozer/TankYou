package com.pyozer.tankyou.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.pyozer.tankyou.R;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.jouer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });
        findViewById(R.id.scores).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScoresActivity.class));
            }
        });
        findViewById(R.id.rules).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, RulesActivity.class));
                showRulesDialog();
            }
        });
        findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialog();
            }
        });
    }

    private void showRulesDialog() {
        final Dialog dialog = new Dialog(this, R.style.AppTheme_NoActionBar);
        View view = LayoutInflater.from(this).inflate(R.layout.rules_dialog, null);

        String rules_text = "<style>p {color: #ffffff; font-size: 20px; text-align: justify;}</style>" +
                "<p>Le jeu se joue grâce à l'<strong>accéléromètre</strong> et à la <strong>boussole</strong> pour pouvoir se déplacer.<br />" +
                "Le but est de contrôler un <strong>tank</strong> et d'éviter les <strong>obstacles</strong> qui se déplacent sur l'écran.<br /><br />" +
                "Vous avez la possibilité avec votre tank de <strong>tirer des missiles</strong> pour détruire des obstacles.<br />" +
                "Entre chaque tire, il y un temps d'attente de <strong>1.5sec</strong> durant laquel le tank recharge un missile.<br /><br />" +
                "A chaque obstacle détruit vous gagnez <strong>un point</strong>. Si vous touchez un obstacle, vous perdez la partie.</p>";

        WebView mWebView = view.findViewById(R.id.rules_text);
        mWebView.loadData(rules_text, "text/html; charset=UTF-8;", null);
        mWebView.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        view.findViewById(R.id.rules_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            dialog.dismiss();
            }
        });

        dialog.setContentView(view);

        dialog.show();
    }

    private void showAboutDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.about_dialog);
        dialog.show();
    }

}
