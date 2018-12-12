package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.MEstacao;
import model.MLeitura;
import model.MSensor;
import utils.EndPoints;
import utils.HttpPost;

/**
 *
 * Classe: DLeitura
 * Objetivo: Classe data de Sensor Instalado
 *
 * @author Isadora Giacomini de Moraes
 * @since 02/12/2018
 *
 */
public class DLeitura {

    public List<MLeitura> listarSensores(MLeitura sensorInstalado, String json){

        List<MLeitura> sensores = new ArrayList<>();

        //
        // Código para buscar sensores (leitura) da estação no banco e carregar Models associados
        //
        try {
            JSONArray jsonarray = new JSONArray(json);
            for(int i=0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                MLeitura itemLeitura = new MLeitura();

                itemLeitura.setId( jsonobject.getInt("id") );
                itemLeitura.setCodigo( jsonobject.getString("codigo") );
                itemLeitura.setNome( jsonobject.getString("nome") );
                itemLeitura.setUnidadeMedida( jsonobject.getString("unidadeMedida") );
                itemLeitura.setAtivo( (jsonobject.getInt("ativo") == 1)  );

                JSONObject sensorJson = jsonobject.getJSONObject("sensor");
                MSensor sensor = new MSensor();
                sensor.setId( sensorJson.getInt("id") );
                sensor.setNome( sensorJson.getString("nome") );
                itemLeitura.setSensor(sensor);

                JSONObject estacaoJson = jsonobject.getJSONObject("estacao");
                MEstacao estacao = new MEstacao();
                estacao.setId( estacaoJson.getInt("id") );
                estacao.setNome( estacaoJson.getString("nome") );
                estacao.setTempoLeitura( estacaoJson.getInt("tempoLeitura") );
                estacao.setAtivo( (estacaoJson.getInt("ativo") == 1) );
                itemLeitura.setEstacao(estacao);

                if(sensorInstalado.isAtivo() && itemLeitura.isAtivo()) { //listar estações ativas
                    sensores.add(itemLeitura);
                }
                else if(!sensorInstalado.isAtivo() && !itemLeitura.isAtivo()){ //listar estações inativas
                    sensores.add(itemLeitura);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        //pesquisar a estação com o id
        MEstacao estacao = new MEstacao();

        //pesquisar por leituras da estação
        if(sensorInstalado.isAtivo()){ //listar leituras ativas

            MSensor sensor = new MSensor();
            sensor.setId(1);
            sensor.setNome("DHT22");

            MLeitura leitura1 = new MLeitura();
            leitura1.setEstacao(estacao);
            leitura1.setSensor(sensor);
            leitura1.setId(1);
            leitura1.setCodigo("FIX-01");
            leitura1.setNome("TEMPERATURA");
            leitura1.setUnidadeMedida("°C");
            leitura1.setAtivo(true);

            MLeitura leitura2 = new MLeitura();
            leitura2.setEstacao(estacao);
            leitura2.setSensor(sensor);
            leitura2.setId(2);
            leitura2.setCodigo("FIX-02");
            leitura2.setNome("UMIDADE");
            leitura2.setUnidadeMedida("%");
            leitura2.setAtivo(true);

            sensores.add(leitura1);
            sensores.add(leitura2);
        }
        else{ //listar leituras inativas

        }
        */
        return sensores;

    }

    public MLeitura pesquisar(MLeitura pLeitura){

        MLeitura leitura = null;

        //
        // Código para pesquisar no banco
        //
        if(pLeitura.getId() > 0) { //pesquisar pelo id da leitura
            //pesquisar a estação com o id
            MEstacao estacao = new MEstacao();

            MSensor sensor = new MSensor();
            sensor.setId(1);
            sensor.setNome("DHT22");

            leitura = new MLeitura();
            leitura.setEstacao(estacao);
            leitura.setSensor(sensor);
            leitura.setId(1);
            leitura.setCodigo("FIX-01");
            leitura.setNome("TEMPERATURA");
            leitura.setUnidadeMedida("°C");
            leitura.setAtivo(true);
        }

        return leitura;

    }

    public boolean salvar(MLeitura leitura){

        boolean bSalvo = false;

        //
        // Código para salvar
        //
        try {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("id_leitura", leitura.getId());
            jsonParam.put("ativo", (leitura.isAtivo())? 1 : 0);

            new HttpPost(EndPoints.SET_LEITURA).execute(jsonParam.toString());
            bSalvo = true;

        } catch (Exception e) {
            bSalvo = false;
            e.printStackTrace();
        }

        return bSalvo;
    }
}
