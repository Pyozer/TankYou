package com.pyozer.tankyou;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class RulesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        String rules_text = "<style>p {color: #ffffff; font-size: 20px; }</style>" +
                "<p>Le jeu se joue grâce à l'<strong>accéléromètre</strong> et à la <strong>boussole</strong> pour pouvoir se déplacer.<br />" +
                "Le but est de contrôler un <strong>tank</strong> et d'éviter les <strong>obstacles</strong> qui se déplacent sur l'écran.<br /><br />" +
                "Vous avez la possibilité avec votre tank de <strong>tirer des missiles</strong> pour détruire des obstacles.<br />" +
                "Entre chaque tire, il y un temps d'attente de <strong>1.5sec</strong> durant laquel le tank recharge un missile.<br /><br />" +
                "A chaque obstacle détruit vous gagnez <strong>un point</strong>. Si vous touchez un obstacle, vous perdez la partie.</p>";

        WebView mWebView = findViewById(R.id.rules_text);
        mWebView.loadData(rules_text, "text/html; charset=UTF-8;", null);
        mWebView.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        findViewById(R.id.rules_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

    }
}
