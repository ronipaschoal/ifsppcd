package br.edu.ifsp.sbv.estacaometeorologica;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import data.DHistorico;
import data.DLeitura;
import data.DTempoReal;
import model.MEstacao;
import model.MHistorico;
import model.MLeitura;
import model.MTempoReal;

public class SensorActivity extends AppCompatActivity {

    private int estacaoId;
    private int leituraId;

    private DLeitura dbLeitura;
    private List<MHistorico> listHistorico;
    private ArrayAdapter<String> adapter;

    private ListView listViewHistorico;

    private TextView txtSensor, txtUnidadeMedida, txtDataMedicao, txtValor;
    private CheckBox chkAtivo;

    private SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    /**
     * Método onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        try {

            estacaoId = getIntent().getIntExtra("estacao", 0);
            leituraId = getIntent().getIntExtra("leitura", 0);

            dbLeitura = new DLeitura();

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

        if (leituraId <= 0){

            //
            // Quando sensor não encontrado, então abrir listagem
            //
            Intent intent = new Intent(this, SensoresActivity.class);
            intent.putExtra("estacao", estacaoId);
            this.finish();
            startActivity(intent);

        }
        else{

            MEstacao estacao = new MEstacao();
            estacao.setId(estacaoId);

            MLeitura leitura = new MLeitura();
            leitura.setId(leituraId);
            leitura.setEstacao(estacao);


            leitura = dbLeitura.pesquisar(leitura);

            if (leitura != null && leitura.getId() > 0){
                this.setTitle(leitura.getSensor().getNome());

                txtSensor.setText(leitura.getSensor().getNome());
                txtUnidadeMedida.setText(leitura.getUnidadeMedida());
                chkAtivo.setChecked(leitura.isAtivo());

                //pesquisar medicao em tempo real
                DTempoReal dTempoReal = new DTempoReal();
                MTempoReal  tempoReal = new MTempoReal();
                tempoReal.setLeitura(leitura);

                tempoReal = dTempoReal.pesquisar(tempoReal);

                txtDataMedicao.setText(simpleDate.format(tempoReal.getData()));
                txtValor.setText(tempoReal.getValor());
            }
            else{

                //
                // Quando sensor não encontrado, então abrir listagem
                //
                Intent intent = new Intent(this, SensoresActivity.class);
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

        listViewHistorico = (ListView)findViewById(R.id.listViewHistorico);
        //listViewHistorico = null;

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

            List<String> listStrHistorico = new ArrayList<>();

            DHistorico dbHistorico = new DHistorico();
            MHistorico historico = new MHistorico();

            MEstacao estacao = new MEstacao();
            estacao.setId(estacaoId);

            MLeitura leitura = new MLeitura();
            leitura.setId(leituraId);
            leitura.setEstacao(estacao);

            historico.setLeitura(leitura); // Buscar histórico do sensor (leitura)

            listHistorico = dbHistorico.listar(historico);

            for (int i = 0; i < listHistorico.size(); i++){
                listStrHistorico.add("[" + simpleDate.format(listHistorico.get(i).getData()) + "] - Valor: " +
                                     listHistorico.get(i).getValor());
            }

            System.out.println("tamanho historico");
            System.out.println(listHistorico.size());

            if (listHistorico != null) {

                if (listHistorico.size() > 0) {
                    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                            listStrHistorico);
                    listViewHistorico.setAdapter(adapter);
                }
                else{
                    ActivityBase.showMessage(getApplicationContext(), getString(R.string.mensagem_nenhum_registro));
                    listViewHistorico.setAdapter(null);
                }

            }
            else{
                ActivityBase.showMessage(getApplicationContext(), getString(R.string.mensagem_nenhum_registro));
                listViewHistorico.setAdapter(null);
            }

        } catch (Exception ex) {
            ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_carregar) + " " + ex.getMessage());
        }
    }

    /**
     * Método onClick do botão btnSalvar
     */
    public void btnSalvar_Click(View view){

        try {

            //
            // Salvar status do sensor
            //
            DLeitura dbLeitura = new DLeitura();
            MLeitura leitura = new MLeitura();

            leitura.setAtivo(chkAtivo.isChecked());

            boolean bSalvo = dbLeitura.salvar(leitura);

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
            // Volta para a tela de listagem de sensores
            //
            Intent intent = new Intent(this, SensoresActivity.class);
            intent.putExtra("estacao", estacaoId);
            this.finish();
            startActivity(intent);

        }
        catch (Exception ex){
            ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_carregar) + " " + ex.getMessage());
        }

    }
}
