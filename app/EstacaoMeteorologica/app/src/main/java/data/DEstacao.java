package data;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.MEstacao;
import utils.EndPoints;
import utils.HttpPost;

/**
 *
 * Classe: DEstacao
 * Objetivo: Classe data de Estação Meteorológica
 *
 * @author Isadora Giacomini de Moraes
 * @since 02/12/2018
 *
 */
public class DEstacao {

    public List<MEstacao> listarEstacoes(MEstacao estacao){

        List<MEstacao> estacoesMeteorologicas = new ArrayList<>();

        //
        // Código para buscar estações meteorológicas no banco e carregar Models associados
        //
        if(estacao.isAtivo()) { //listar estações ativas
            MEstacao estacao1 = new MEstacao();
            estacao1.setId(1);
            estacao1.setNome("ESTACAO FIXA");
            estacao1.setTempoLeitura(5000);
            estacao1.setAtivo(true);

            estacoesMeteorologicas.add(estacao1);
        }
        else{ //listar estações inativas
            MEstacao estacao2 = new MEstacao();
            estacao2.setId(2);
            estacao2.setNome("ESTACAO MOVEL");
            estacao2.setTempoLeitura(5000);
            estacao2.setAtivo(false);

            estacoesMeteorologicas.add(estacao2);
        }

        return estacoesMeteorologicas;

    }

    public MEstacao pesquisar(MEstacao pEstacao){

        MEstacao estacao = null;

        //
        // Código para pesquisar no banco
        //
        if(pEstacao.getId() > 0){ //pesquisar pelo id da estacao
            estacao = new MEstacao();
            estacao.setId(1);
            estacao.setNome("ESTACAO FIXA");
            estacao.setTempoLeitura(5000);
            estacao.setAtivo(true);
        }

        return estacao;

    }

    public boolean salvar(MEstacao estacao){

        boolean bSalvo = false;

        //
        // Código para salvar
        //
        try {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("id_estacao", estacao.getId());
            jsonParam.put("tempo_medicao", estacao.getTempoLeitura()*1000);
            jsonParam.put("ativo", (estacao.isAtivo())? 1 : 0);

            new HttpPost(EndPoints.CONFIG_ESTACAO).execute(jsonParam.toString());
            bSalvo = true;

        } catch (Exception e) {
            bSalvo = false;
            e.printStackTrace();
        }

        return bSalvo;
    }
}
