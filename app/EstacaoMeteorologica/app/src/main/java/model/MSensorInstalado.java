package model;

/**
 *
 * Classe: MSensorInstalado
 * Objetivo: Classe model de Sensor Instalado
 *
 * @author Isadora Giacomini de Moraes
 * @since 02/12/2018
 *
 */
public class MSensorInstalado {

    private int id;
    private MSensor sensor;
    private MEstacao estacao;
    private MTipoMedicao tipoMedicao;
    private MMedicoes medicoes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MSensor getSensor() {
        return sensor;
    }

    public void setSensor(MSensor sensor) {
        this.sensor = sensor;
    }

    public MEstacao getEstacao() {
        return estacao;
    }

    public void setEstacao(MEstacao estacao) {
        this.estacao = estacao;
    }

    public MTipoMedicao getTipoMedicao() {
        return tipoMedicao;
    }

    public void setTipoMedicao(MTipoMedicao tipoMedicao) {
        this.tipoMedicao = tipoMedicao;
    }

    public MMedicoes getMedicoes() {
        return medicoes;
    }

    public void setMedicoes(MMedicoes medicoes) {
        this.medicoes = medicoes;
    }
}
