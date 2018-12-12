package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.MEstacao;
import model.MLeitura;
import model.MTempoReal;

public class DTempoReal {

    public MTempoReal pesquisar(MTempoReal pTempoReal, String json){

        MTempoReal tempoReal = null;

        //
        // Código para buscar histórico do sensor (leitura)
        //
        if(pTempoReal.getLeitura().getId() > 0) {

            try {
                System.out.println("json recebido para parser");
                System.out.println(json);
                JSONArray jsonarray = new JSONArray(json);
                JSONObject jsonobject = jsonarray.getJSONObject(0);

                tempoReal = new MTempoReal();
                tempoReal.setId( jsonobject.getInt("id") );
                tempoReal.setLeitura(pTempoReal.getLeitura());
                tempoReal.setValor( jsonobject.getString("valor") );

                //converte string para Date
                String dataStr = jsonobject.getString("data");
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date data = formato.parse(dataStr);

                tempoReal.setData(data);

                return tempoReal;

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            /*tempoReal = new MTempoReal();
            tempoReal.setId(1);
            tempoReal.setLeitura(pTempoReal.getLeitura());
            tempoReal.setValor("26");
            tempoReal.setData(new Date());*/
        }

        return tempoReal;
    }
}
