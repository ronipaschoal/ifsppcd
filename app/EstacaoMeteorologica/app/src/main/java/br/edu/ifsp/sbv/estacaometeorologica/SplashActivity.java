package br.edu.ifsp.sbv.estacaometeorologica;

import android.content.Intent;
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

    ImageView imgLogo;

    /**
     * Método onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                finish();

                startActivity(intent);

                return false;
            }

        });
    }
}
