package br.edu.ifsp.sbv.estacaometeorologica;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import data.DEstacao;
import model.MEstacao;
import utils.EndPoints;

/**
 *
 * Classe: MainActivity
 * Objetivo: Login no aplicativo
 *
 * @author Isadora Giacomini de Moraes
 * @since 02/12/2018
 *
 */
public class MainActivity extends AppCompatActivity {

    private DEstacao dbEstacao;
    private List<MEstacao> listEstacoes;
    private ArrayAdapter<String> adapter;

    private String[] status;

    private Spinner cboStatus;
    private ListView listViewEstacoes;
    ProgressDialog pd;

    /**
     * Método onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle(R.string.title_estacoes);

        try {

            dbEstacao = new DEstacao();

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

        cboStatus = (Spinner)findViewById(R.id.cboStatus);
        listViewEstacoes = (ListView) findViewById(R.id.listViewEstacoes);
        listViewEstacoes.setOnItemClickListener(listarSensores);

        //listViewEstacoes = null;

        carregarSpinner();
        carregarEstacoes();

        cboStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listEstacoes = null;
                carregarEstacoes();
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
     * Carregar estações meteorológicas
     */
    private void carregarEstacoes(){
        System.out.println("--------- carregarEstacoes() ---------");

        if(pd != null && pd.isShowing()) {
            pd.dismiss();
        }

        try {

            List<String> estacoes = new ArrayList<>();

            boolean bAtivo = status[0].equals(cboStatus.getSelectedItem().toString());

            MEstacao estacao = new MEstacao();
            estacao.setAtivo(bAtivo);

            if(listEstacoes == null){
                new BaixarEstacoes(estacao).execute(EndPoints.GET_ESTACOES);
            }
            else{

                for (int i = 0; i < listEstacoes.size(); i++) {
                    estacoes.add(listEstacoes.get(i).getNome());
                }

                if (listEstacoes != null) {

                    if (listEstacoes.size() > 0) {
                        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                                estacoes);
                        listViewEstacoes.setAdapter(adapter);
                    } else {
                        ActivityBase.showMessage(getApplicationContext(), getString(R.string.mensagem_nenhum_registro));
                        listViewEstacoes.setAdapter(null);
                    }

                } else {
                    ActivityBase.showMessage(getApplicationContext(), getString(R.string.mensagem_nenhum_registro));
                    listViewEstacoes.setAdapter(null);
                }
            }

        } catch (Exception ex) {
            ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_carregar) + " " + ex.getMessage());
        }
    }

    private class BaixarEstacoes extends AsyncTask<String, Void, String> {

        MEstacao estacao;

        public BaixarEstacoes(MEstacao estacao){
            this.estacao = estacao;
        }

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
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

            listEstacoes = dbEstacao.listarEstacoes(estacao, result);

            carregarEstacoes();
        }
    }

    /**
     * Método onItemClickListener, para visualizar sensores da estação
     */
    private AdapterView.OnItemClickListener listarSensores = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {

            try {

                Intent intent = new Intent(getApplicationContext(), SensoresActivity.class);
                intent.putExtra("estacao", listEstacoes.get(pos).getId());
                finish();

                startActivity(intent);

            }
            catch (Exception ex) {
                ActivityBase.showMessage(getApplicationContext(), getString(R.string.erro_carregar) + " " + ex.getMessage());
            }

        }

    };
}
