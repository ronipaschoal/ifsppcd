package data;

import java.util.Date;

import model.MTempoReal;

public class DTempoReal {

    public MTempoReal pesquisar(MTempoReal pTempoReal){

        MTempoReal tempoReal = null;

        //
        // Código para buscar histórico do sensor (leitura)
        //
        if(pTempoReal.getLeitura().getId() > 0) {
            tempoReal = new MTempoReal();
            tempoReal.setId(1);
            tempoReal.setLeitura(pTempoReal.getLeitura());
            tempoReal.setValor("26");
            tempoReal.setData(new Date());
        }

        return tempoReal;
    }
}
