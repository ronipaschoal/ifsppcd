package br.edu.ifsp.sbv.estacaometeorologica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import data.DEstacao;
import data.DLeitura;
import model.MEstacao;
import model.MLeitura;

public class SensoresActivity extends AppCompatActivity {

    private int estacaoId;

    private DLeitura dbSensorInstalado;
    private List<MLeitura> listSensores;
    private ArrayAdapter<String> adapter;

    private String[] status;

    private Spinner cboStatus;
    private ListView listViewSensores;

    /**
     * Método onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensores);

        try {

            estacaoId = getIntent().getIntExtra("estacao", 0);

            dbSensorInstalado = new DLeitura();

            carregarEstacao();
            carregarControles();

        }
        catch (Exception ex) {
            ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_carregar) + " " + ex.getMessage());
        }
    }

    /**
     * Criar menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_main, menu);
        return true;
    }

    /**
     * Selecionar opção do menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {

            int id = item.getItemId();

            Intent intent;

            switch (id) {

                case R.id.btnConfiguracoes:
                    intent = new Intent(this, ConfiguracoesActivity.class);
                    intent.putExtra("estacao", estacaoId);
                    this.finish();
                    startActivity(intent);

                    break;

                case R.id.btnSair:
                    intent = new Intent(this, MainActivity.class);
                    this.finish();
                    startActivity(intent);

                    break;

            }

        }
        catch (Exception ex){

            ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_carregar) + " " + ex.getMessage());

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Carregar estação
     */
    private void carregarEstacao(){

        if (estacaoId <= 0){

            //
            // Quando estação não informada, então abrir tela de listagem estações
            //
            Intent intent = new Intent(this, MainActivity.class);
            this.finish();
            startActivity(intent);

        }
        else{

            DEstacao dbEstacao = new DEstacao();
            MEstacao estacao = new MEstacao();

            estacao.setId(estacaoId);

            estacao = dbEstacao.pesquisar(estacao);

            if (estacao != null && estacao.getId() > 0){
                this.setTitle(estacao.getNome()); // Setar título da Activity
            }
            else{

                //
                // Quando estação não encontrada, então abrir tela de listagem estações
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

        cboStatus = (Spinner)findViewById(R.id.cboStatus);
        listViewSensores = (ListView)findViewById(R.id.listViewSensores);
        listViewSensores.setOnItemClickListener(visualizarSensor);

        //listViewSensores = null;

        carregarSpinner();
        carregarSensores();

        cboStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                carregarSensores();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * Carregar spinner Status
     */
    private void carregarSpinner(){
        status = this.getResources().getStringArray(R.array.array_status);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, status);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cboStatus.setAdapter(adapter);
        cboStatus.setSelection(0);
    }

    /**
     * Carregar sensores da estação meteorológica
     */
    private void carregarSensores(){

        try {

            List<String> sensores = new ArrayList<>();

            boolean bAtivo = status[0].equals(cboStatus.getSelectedItem().toString());

            MEstacao estacao = new MEstacao();
            estacao.setId(estacaoId);

            MLeitura leitura = new MLeitura();
            leitura.setEstacao(estacao); // Buscar sensores conforme estação
            leitura.setAtivo(bAtivo);

            listSensores = dbSensorInstalado.listarSensores(leitura);

            for (int i = 0; i < listSensores.size(); i++){
                sensores.add(listSensores.get(i).getNome() +" - "+ listSensores.get(i).getSensor().getNome());
            }

            if (listSensores != null) {

                if (listSensores.size() > 0) {
                    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                            sensores);
                    listViewSensores.setAdapter(adapter);
                }
                else{
                    ActivityBase.showMessage(getApplicationContext(), getString(R.string.mensagem_nenhum_registro));
                    listViewSensores.setAdapter(null);
                }

            }
            else{
                ActivityBase.showMessage(getApplicationContext(), getString(R.string.mensagem_nenhum_registro));
                listViewSensores.setAdapter(null);
            }

        } catch (Exception ex) {
            ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_carregar) + " " + ex.getMessage());
        }
    }

    /**
     * Método onItemClickListener, para visualizar sensor
     */
    private AdapterView.OnItemClickListener visualizarSensor = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {

            try {

                Intent intent = new Intent(getApplicationContext(), SensorActivity.class);
                intent.putExtra("estacao", estacaoId);
                intent.putExtra("leitura", listSensores.get(pos).getId());
                finish();

                startActivity(intent);

            }
            catch (Exception ex) {
                ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_carregar) + " " + ex.getMessage());
            }

        }

    };
}
