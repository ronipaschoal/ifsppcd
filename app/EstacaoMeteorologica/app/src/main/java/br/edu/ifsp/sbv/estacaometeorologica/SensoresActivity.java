package br.edu.ifsp.sbv.estacaometeorologica;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import data.DEstacao;
import data.DLeitura;
import model.MEstacao;
import model.MLeitura;
import utils.EndPoints;

public class SensoresActivity extends AppCompatActivity {

    private int estacaoId;

    private DLeitura dbSensorInstalado;
    private List<MLeitura> listSensores;
    private ArrayAdapter<String> adapter;
    private ProgressDialog pd;
    private MEstacao estacaoSensores;

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
                    intent.putExtra("estacao", estacaoSensores);
                    intent.putExtra("estacaoId", estacaoId);
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

        System.out.println("--------- carregarEstacoes() ---------");

        if(pd != null && pd.isShowing()) {
            pd.dismiss();
        }

        if (estacaoId <= 0){

            //
            // Quando estação não informada, então abrir tela de listagem estações
            //
            Intent intent = new Intent(this, MainActivity.class);
            this.finish();
            startActivity(intent);

        }
        else{
            MEstacao pEstacao = new MEstacao();
            pEstacao.setId(estacaoId);

            //estacao = dbEstacao.pesquisar(estacao);
            if(estacaoSensores == null){
                new PesquisarEstacao(pEstacao).execute(EndPoints.GET_ESTACOES);
            }
            else {
                if (estacaoSensores != null && estacaoSensores.getId() > 0) {
                    this.setTitle(estacaoSensores.getNome()); // Setar título da Activity
                } else {

                    //
                    // Quando estação não encontrada, então abrir tela de listagem estações
                    //
                    Intent intent = new Intent(this, MainActivity.class);
                    this.finish();
                    startActivity(intent);

                }
            }
        }

    }

    private class PesquisarEstacao extends AsyncTask<String, Void, String> {

        MEstacao estacao;

        public PesquisarEstacao(MEstacao estacao){
            this.estacao = estacao;
        }

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(SensoresActivity.this);
            pd.setMessage(getString(R.string.carregando));
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);

                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            DEstacao dbEstacao = new DEstacao();
            estacaoSensores = dbEstacao.pesquisar(estacao, result);

            carregarEstacao();
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
                listSensores = null;
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

        System.out.println("--------- carregarSensores() ---------");

        if(pd != null && pd.isShowing()) {
            pd.dismiss();
        }

        try {

            List<String> sensores = new ArrayList<>();

            boolean bAtivo = status[0].equals(cboStatus.getSelectedItem().toString());

            MEstacao estacao = new MEstacao();
            estacao.setId(estacaoId);

            MLeitura leitura = new MLeitura();
            leitura.setEstacao(estacao); // Buscar sensores conforme estação
            leitura.setAtivo(bAtivo);

            //listSensores = dbSensorInstalado.listarSensores(leitura);
            if(listSensores == null){
                new PesquisarSensores(leitura).execute(EndPoints.GET_LEITURAS);
            }
            else {
                for (int i = 0; i < listSensores.size(); i++) {
                    sensores.add(listSensores.get(i).getNome() + " - " + listSensores.get(i).getSensor().getNome());
                }

                if (listSensores != null) {

                    if (listSensores.size() > 0) {
                        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                                sensores);
                        listViewSensores.setAdapter(adapter);
                    } else {
                        ActivityBase.showMessage(getApplicationContext(), getString(R.string.mensagem_nenhum_registro));
                        listViewSensores.setAdapter(null);
                    }

                } else {
                    ActivityBase.showMessage(getApplicationContext(), getString(R.string.mensagem_nenhum_registro));
                    listViewSensores.setAdapter(null);
                }
            }

        } catch (Exception ex) {
            ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_carregar) + " " + ex.getMessage());
        }
    }

    private class PesquisarSensores extends AsyncTask<String, Void, String> {

        MLeitura leitura;

        public PesquisarSensores(MLeitura leitura){
            this.leitura = leitura;
        }

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(SensoresActivity.this);
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
                json.put("id_estacao", leitura.getEstacao().getId());

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

            listSensores = dbSensorInstalado.listarSensores(leitura, result);

            carregarSensores();
        }
    }

    /**
     * Método onItemClickListener, para visualizar sensor
     */
    private AdapterView.OnItemClickListener visualizarSensor = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {

            try {

                Intent intent = new Intent(getApplicationContext(), SensorActivity.class);
                intent.putExtra("estacaoId", estacaoId);
                intent.putExtra("estacao", estacaoSensores);

                intent.putExtra("leituraId", listSensores.get(pos).getId());
                intent.putExtra("leitura", listSensores.get(pos));
                finish();

                startActivity(intent);

            }
            catch (Exception ex) {
                ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_carregar) + " " + ex.getMessage());
            }

        }

    };
}
