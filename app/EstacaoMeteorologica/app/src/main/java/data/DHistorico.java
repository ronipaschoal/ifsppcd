package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.MEstacao;
import model.MHistorico;

/**
 *
 * Classe: DHistorico
 * Objetivo: Classe data de histórico
 *
 * @author Isadora Giacomini de Moraes
 * @since 09/12/2018
 *
 */
public class DHistorico {

    public List<MHistorico> listar(MHistorico pHistorico, String json){

        List<MHistorico> historico = new ArrayList<>();

        //
        // Código para buscar histórico do sensor (leitura)
        //
        if(pHistorico.getLeitura().getId() > 0){ //buscar historico de leitura

            try {
                JSONArray jsonarray = new JSONArray(json);
                for(int i=0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    MHistorico itemHistorico = new MHistorico();
                    itemHistorico.setId( jsonobject.getInt("id") );
                    itemHistorico.setLeitura(pHistorico.getLeitura());
                    itemHistorico.setValor( jsonobject.getString("valor") );

                    //converte string para Date
                    String dataStr = jsonobject.getString("data");
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date data = formato.parse(dataStr);

                    itemHistorico.setData( data );

                    historico.add(itemHistorico);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            /*MHistorico historico1 = new MHistorico();
            historico1.setId(1);
            historico1.setLeitura(pHistorico.getLeitura());
            historico1.setValor("25");
            historico1.setData(new Date());

            MHistorico historico2 = new MHistorico();
            historico2.setId(2);
            historico2.setLeitura(pHistorico.getLeitura());
            historico2.setValor("22");
            historico2.setData(new Date());

            historico.add(historico1);
            historico.add(historico2);*/
        }

        return historico;

    }
}
