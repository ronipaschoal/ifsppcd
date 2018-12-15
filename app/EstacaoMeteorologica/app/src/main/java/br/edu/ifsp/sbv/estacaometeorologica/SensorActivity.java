package br.edu.ifsp.sbv.estacaometeorologica;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import data.DEstacao;
import data.DHistorico;
import data.DLeitura;
import data.DTempoReal;
import model.MEstacao;
import model.MHistorico;
import model.MLeitura;
import model.MTempoReal;
import utils.EndPoints;

public class SensorActivity extends AppCompatActivity {

    private int estacaoId;
    private int leituraId;

    private MEstacao estacaoSensor;
    private MLeitura leituraSensor;

    MTempoReal  tempoReal;

    private DLeitura dbLeitura;
    private List<MHistorico> listHistorico;
    private ArrayAdapter<String> adapter;

    private ListView listViewHistorico;
    private ProgressDialog pd;

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

            estacaoId = getIntent().getIntExtra("estacaoId", 0);
            estacaoSensor = (MEstacao) getIntent().getSerializableExtra("estacao");

            leituraId = getIntent().getIntExtra("leituraId", 0);
            leituraSensor = (MLeitura) getIntent().getSerializableExtra("leitura");

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

        System.out.println("--------- carregarSensor() ---------");

        if(pd != null && pd.isShowing()) {
            pd.dismiss();
        }

        if (leituraSensor == null){

            //
            // Quando sensor não encontrado, então abrir listagem
            //
            Intent intent = new Intent(this, SensoresActivity.class);
            intent.putExtra("estacao", estacaoId);
            this.finish();
            startActivity(intent);

        }
        else{

            if (leituraSensor != null && leituraSensor.getId() > 0){
                this.setTitle(leituraSensor.getSensor().getNome());

                txtSensor.setText(leituraSensor.getNome() + " - " + leituraSensor.getSensor().getNome());
                txtUnidadeMedida.setText(leituraSensor.getUnidadeMedida());
                chkAtivo.setChecked(leituraSensor.isAtivo());

                //pesquisar medicao em tempo real
                MTempoReal pTempoReal = new MTempoReal();
                pTempoReal.setLeitura(leituraSensor);

                if(tempoReal == null) {
                    new PesquisarTempoReal(pTempoReal).execute(EndPoints.GET_TEMPO_REAL);
                }
                else{
                    txtDataMedicao.setText(simpleDate.format(tempoReal.getData()));
                    txtValor.setText(tempoReal.getValor());
                }
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

    private class PesquisarTempoReal extends AsyncTask<String, Void, String> {

        MTempoReal pTempoReal;

        public PesquisarTempoReal(MTempoReal tempoReal){
            this.pTempoReal = tempoReal;
        }

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(SensorActivity.this);
            pd.setMessage(getString(R.string.carregando));
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            String response = "";
            BufferedReader reader = null;

            try{
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject json = new JSONObject();
                json.put("id_leitura", pTempoReal.getLeitura().getId());

                Log.i("JSON", json.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(json.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage());

                InputStream stream = conn.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);

                }

                Log.i("RESPONSE", buffer.toString());

                conn.disconnect();

                return buffer.toString();
            }
            catch (Exception ex){
                Log.e("ERROR", ex.getMessage());
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            DTempoReal dTempoReal = new DTempoReal();
            tempoReal = dTempoReal.pesquisar(pTempoReal, result);

            carregarSensor();
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

        if(pd != null && pd.isShowing()) {
            pd.dismiss();
        }

        try {

            List<String> listStrHistorico = new ArrayList<>();

            MHistorico historico = new MHistorico();
            historico.setLeitura(leituraSensor); // Buscar histórico do sensor (leitura)

            if(listHistorico == null){
                new PesquisarHistorico(historico).execute(EndPoints.GET_HISTORICO);
            }
            else {
                for (int i = 0; i < listHistorico.size(); i++) {
                    listStrHistorico.add("[" + simpleDate.format(listHistorico.get(i).getData()) + "] - Valor: " +
                            listHistorico.get(i).getValor());
                }

                if (listHistorico != null) {

                    if (listHistorico.size() > 0) {
                        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                                listStrHistorico);
                        listViewHistorico.setAdapter(adapter);
                    } else {
                        ActivityBase.showMessage(getApplicationContext(), getString(R.string.mensagem_sem_historico));
                        listViewHistorico.setAdapter(null);
                    }

                } else {
                    ActivityBase.showMessage(getApplicationContext(), getString(R.string.mensagem_sem_historico));
                    listViewHistorico.setAdapter(null);
                }
            }

        } catch (Exception ex) {
            ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_carregar) + " " + ex.getMessage());
        }
    }

    private class PesquisarHistorico extends AsyncTask<String, Void, String> {

        MHistorico pHistorico;

        public PesquisarHistorico(MHistorico historico){
            this.pHistorico = historico;
        }

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(SensorActivity.this);
            pd.setMessage(getString(R.string.carregando));
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            String response = "";
            BufferedReader reader = null;

            try{
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject json = new JSONObject();
                json.put("id_leitura", pHistorico.getLeitura().getId());

                Log.i("JSON", json.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(json.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage());

                InputStream stream = conn.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);

                }

                Log.i("RESPONSE", buffer.toString());

                conn.disconnect();

                return buffer.toString();
            }
            catch (Exception ex){
                Log.e("ERROR", ex.getMessage());
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            DHistorico dbHistorico = new DHistorico();
            listHistorico = dbHistorico.listar(pHistorico, result);

            carregarHistorico();
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

            leitura.setId(leituraSensor.getId());
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
