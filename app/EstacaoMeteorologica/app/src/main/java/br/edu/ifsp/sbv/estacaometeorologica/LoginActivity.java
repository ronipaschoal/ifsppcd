package br.edu.ifsp.sbv.estacaometeorologica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import data.DEstacao;
import model.MEstacao;

/**
 *
 * Classe: LoginActivity
 * Objetivo: Login no aplicativo
 *
 * @author Isadora Giacomini de Moraes
 * @since 02/12/2018
 *
 */
public class LoginActivity extends AppCompatActivity {

    private EditText txtChaveAcesso;

    /**
     * Método onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            this.carregarControles();
        }
        catch (Exception ex) {
            ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_carregar) + " " + ex.getMessage());
        }
    }

    /**
     * Carregar componentes do layout
     */
    private void carregarControles(){
        txtChaveAcesso = (EditText)findViewById(R.id.txtChaveAcesso);
    }

    /**
     * Método onClick do botão btnEntrar
     */
    public void btnEntrar_Click(View view){

        try {
            String strChaveAcesso = txtChaveAcesso.getText().toString().trim();

            if (strChaveAcesso.isEmpty())
            {
                ActivityBase.showMessage(getApplicationContext(), getString(R.string.mensagem_chave_invalida));
                return;
            }

            //
            // Busca estação meteorológica que possui a chave de acesso informada
            //
            DEstacao dbEstacao = new DEstacao();
            MEstacao estacao = new MEstacao();

            //estacao.setChaveAcesso(strChaveAcesso);

            estacao = dbEstacao.pesquisar(estacao);

            if (estacao != null && estacao.getId() > 0) {

                //
                // Se estação encontrada, abre a tela MainActivity
                //
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("estacao", estacao.getId());
                this.finish();
                startActivity(intent);

            } else {
                ActivityBase.showMessage(getApplicationContext(), getString(R.string.mensagem_chave_incorreta));
            }
        }
        catch (Exception ex){
            ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_entrar) + " " + ex.getMessage());
        }

    }

    /**
     * Método onClick do botão btnCancelar
     */
    public void btnCancelar_Click(View view){

        try {

            //
            // Volta para a tela de Splash
            //
            Intent intent = new Intent(this, SplashActivity.class);
            this.finish();
            startActivity(intent);

        }
        catch (Exception ex){
            ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_carregar) + " " + ex.getMessage());
        }

    }
}
