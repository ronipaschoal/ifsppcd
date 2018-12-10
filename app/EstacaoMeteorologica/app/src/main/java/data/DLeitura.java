package data;

import java.util.ArrayList;
import java.util.List;

import model.MEstacao;
import model.MLeitura;
import model.MSensor;

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

    public List<MLeitura> listarSensores(MLeitura sensorInstalado){

        List<MLeitura> sensores = new ArrayList<>();

        //
        // Código para buscar sensores (leitura) da estação no banco e carregar Models associados
        //

        //pesquisar a estação com o id
        DEstacao dEstacao = new DEstacao();
        MEstacao estacao = dEstacao.pesquisar(sensorInstalado.getEstacao());

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

        return sensores;

    }

    public MLeitura pesquisar(MLeitura pLeitura){

        MLeitura leitura = null;

        //
        // Código para pesquisar no banco
        //
        if(pLeitura.getId() > 0) { //pesquisar pelo id da leitura
            //pesquisar a estação com o id
            DEstacao dEstacao = new DEstacao();
            MEstacao estacao = dEstacao.pesquisar(pLeitura.getEstacao());

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

        return bSalvo;
    }
}
