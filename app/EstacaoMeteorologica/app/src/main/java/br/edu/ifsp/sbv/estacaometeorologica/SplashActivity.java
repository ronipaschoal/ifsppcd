package br.edu.ifsp.sbv.estacaometeorologica;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 *
 * Classe: SplashActivity
 * Objetivo: Apresentação do aplicativo
 *
 * @author Isadora Giacomini de Moraes
 * @since 02/12/2018
 *
 */
public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    ImageView imgLogo;

    /**
     * Método onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /*
        imgLogo = (ImageView) findViewById(R.id.imgLogo);

        //
        // Adicionar o evento de clique longo para a imagem de logo
        //
        imgLogo.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                //
                // Abre a tela de Login
                //
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();

                startActivity(intent);

                return false;
            }

        });*/

        //depois de alguns segundos abre MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();

                startActivity(intent);
            }
        }, SPLASH_TIME_OUT);
    }
}
