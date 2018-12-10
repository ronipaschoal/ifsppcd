package br.edu.ifsp.sbv.estacaometeorologica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import data.DEstacao;
import model.MEstacao;

public class ConfiguracoesActivity extends AppCompatActivity {

    private int estacaoId;

    private DEstacao dbEstacao;

    private CheckBox chkAtivo;
    private TextView txtEstacaoMeteorologica, txtTempoLeitura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        this.setTitle(R.string.menu_item_configuracoes);

        try {

            estacaoId = getIntent().getIntExtra("estacao", 0);

            dbEstacao = new DEstacao();

            carregarControles();
            carregarSensor();

        }
        catch (Exception ex) {
            ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_carregar) + " " + ex.getMessage());
        }
    }

    /**
     * Carregar estação meteorológica
     */
    private void carregarSensor(){

        if (estacaoId <= 0){

            //
            // Quando estação não encontrada, então abrir listagem
            //
            Intent intent = new Intent(this, MainActivity.class);
            this.finish();
            startActivity(intent);

        }
        else{

            MEstacao estacao = new MEstacao();

            estacao.setId(estacaoId);

            estacao = dbEstacao.pesquisar(estacao);

            if (estacao != null && estacao.getId() > 0){

                txtEstacaoMeteorologica.setText(estacao.getNome());
                txtTempoLeitura.setText( String.valueOf(estacao.getTempoLeitura()/1000) );
                chkAtivo.setChecked(estacao.isAtivo());
            }
            else{

                //
                // Quando estação não encontrada, então abrir listagem
                //
                Intent intent = new Intent(this, MainActivity.class);
                this.finish();
                startActivity(intent);

            }
        }
    }

    /**
     * Carregar componentes do layout
     */
    private void carregarControles(){

        txtEstacaoMeteorologica = (TextView)findViewById(R.id.txtEstacaoMeteorologica);
        txtTempoLeitura = (TextView)findViewById(R.id.txtTempoLeitura);

        chkAtivo = (CheckBox)findViewById(R.id.chkAtivo);

    }

    /**
     * Método onClick do botão btnSalvar
     */
    public void btnSalvar_Click(View view){

        try {

            //
            // Salvar status e tempo de leitura da estação meteorológica
            //
            MEstacao estacao = new MEstacao();

            estacao.setId(estacaoId);
            estacao.setTempoLeitura(Integer.valueOf(txtTempoLeitura.getText().toString()));
            estacao.setAtivo(chkAtivo.isChecked());

            boolean bSalvo = dbEstacao.salvar(estacao);

            if (bSalvo) {
                ActivityBase.showMessage(getApplicationContext(), getString(R.string.mensagem_salvar));
            }
            else {
                ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_salvar));
            }


        }
        catch (Exception ex){
            ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_salvar) + " " + ex.getMessage());
        }

    }

    /**
     * Método onClick do botão btnCancelar
     */
    public void btnCancelar_Click(View view){

        try {

            //
            // Volta para a tela de listagem dos sensores
            //
            Intent intent = new Intent(getApplicationContext(), SensoresActivity.class);
            intent.putExtra("estacao", estacaoId);
            finish();

            startActivity(intent);

        }
        catch (Exception ex){
            ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_carregar) + " " + ex.getMessage());
        }

    }
}
