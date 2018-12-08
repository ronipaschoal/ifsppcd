package br.edu.ifsp.sbv.estacaometeorologica;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import data.DSensorInstalado;
import model.MHistorico;
import model.MLeitura;

public class SensorActivity extends AppCompatActivity {

    private int estacaoId;
    private int sensorId;

    private DSensorInstalado dbSensorInstalado;
    private List<MHistorico> listHistorico;
    private ArrayAdapter<String> adapter;

    private ListView listViewHistorico;

    private TextView txtSensor, txtUnidadeMedida, txtDataMedicao, txtValor;
    private CheckBox chkAtivo;

    /**
     * Método onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        try {

            estacaoId = getIntent().getIntExtra("estacao", 0);
            sensorId = getIntent().getIntExtra("sensor", 0);

            dbSensorInstalado = new DSensorInstalado();

            carregarControles();
            carregarSensor();

        }
        catch (Exception ex) {
            ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_carregar) + " " + ex.getMessage());
        }
    }

    /**
     * Carregar sensor
     */
    private void carregarSensor(){

        if (sensorId <= 0){

            //
            // Quando sensor não encontrado, então abrir listagem
            //
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("estacao", estacaoId);
            this.finish();
            startActivity(intent);

        }
        else{

            MLeitura leitura = new MLeitura();

            leitura.setId(sensorId);

            //leitura = dbSensorInstalado.pesquisar(leitura);

            if (leitura != null && leitura.getId() > 0){
                this.setTitle(leitura.getSensor().getNome());

                txtSensor.setText(leitura.getSensor().getNome());
                txtUnidadeMedida.setText(leitura.getUnidadeMedida());

                //txtDataMedicao.setText(leitura.getMedicoes().getDate().toString()); //TODO: pegar data
                //txtValor.setText(leitura.getMedicoes().getValor().toString()); //TODO: pegar valor

                //chkAtivo.setChecked(leitura.isAtivo()); Não tem campo Status
            }
            else{

                //
                // Quando sensor não encontrado, então abrir listagem
                //
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("estacao", estacaoId);
                this.finish();
                startActivity(intent);

            }
        }
    }

    /**
     * Carregar componentes do layout
     */
    private void carregarControles(){

        txtSensor = (TextView)findViewById(R.id.txtSensor);
        txtUnidadeMedida = (TextView)findViewById(R.id.txtUnidadeMedida);
        txtDataMedicao = (TextView)findViewById(R.id.txtDataMedicao);
        txtValor = (TextView)findViewById(R.id.txtValor);

        chkAtivo = (CheckBox)findViewById(R.id.chkAtivo);

        listViewHistorico = (ListView)findViewById(R.id.listViewSensores);
        listViewHistorico = null;

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        //Tab Sensor
        TabHost.TabSpec spec = tabHost.newTabSpec(getString(R.string.tab_sensor));
        spec.setContent(R.id.tabSensor);
        spec.setIndicator(getString(R.string.tab_sensor));
        tabHost.addTab(spec);

        //Tab Histórico
        spec = tabHost.newTabSpec(getString(R.string.tab_historico));
        spec.setContent(R.id.tabHistorico);
        spec.setIndicator(getString(R.string.tab_historico));
        tabHost.addTab(spec);

        //Background color das Tabs
        tabHost.getTabWidget().setBackgroundColor(Color.parseColor("#CC0000"));

        //Cor do detalhe para indicar Tab ativa
        tabHost.getTabWidget().getChildAt(0).
                setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        tabHost.getTabWidget().getChildAt(1).
                setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));

        //Formatação do texto das Tabs
        for(int i = 0; i < tabHost.getTabWidget().getChildCount(); i++)
        {
            TextView textView = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            textView.setAllCaps(false);
            textView.setTextSize(18);
        }

        //Tab a ser exibida
        tabHost.setCurrentTab(0);

        carregarHistorico();

    }

    /**
     * Carregar histórico de medições do sensor
     */
    private void carregarHistorico(){

        try {

            List<String> historico = new ArrayList<>();

//            MLeitura leitura = new MLeitura();
//            leitura.getEstacao().setId(estacaoId); // Buscar sensores conforme estação
//            // (?) Filtrar por sensor ativo / inativo, porém não tem campo na tabela
//
//            listSensores = dbSensorInstalado.listarSensores(leitura);
//
//            for (int i = 0; i < listSensores.size(); i++){
//                historico.add(listSensores.get(i).getSensor().getNome());
//            }
//
//            if (listSensores != null) {
//
//                if (listSensores.size() > 0) {
//                    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
//                            historico);
//                    listViewSensores.setAdapter(adapter);
//                }
//                else{
//                    ActivityBase.showMessage(getApplicationContext(), getString(R.string.mensagem_nenhum_registro));
//                    listViewSensores.setAdapter(null);
//                }
//
//            }
//            else{
//                ActivityBase.showMessage(getApplicationContext(), getString(R.string.mensagem_nenhum_registro));
//                listViewSensores.setAdapter(null);
//            }

        } catch (Exception ex) {
            ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_carregar) + " " + ex.getMessage());
        }
    }
}
