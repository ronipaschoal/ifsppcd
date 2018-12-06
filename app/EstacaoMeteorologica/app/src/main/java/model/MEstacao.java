package model;

/**
 *
 * Classe: MEstacao
 * Objetivo: Classe model de Estação Meteorológica
 *
 * @author Isadora Giacomini de Moraes
 * @since 02/12/2018
 *
 */
public class MEstacao {

    private int id;
    private int tempoMedicao;
    private String nome;
    private String ip;
    private String porta;
    private String chaveAcesso;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTempoMedicao() {
        return tempoMedicao;
    }

    public void setTempoMedicao(int tempoMedicao) {
        this.tempoMedicao = tempoMedicao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPorta() {
        return porta;
    }

    public void setPorta(String porta) {
        this.porta = porta;
    }

    public String getChaveAcesso() {
        return chaveAcesso;
    }

    public void setChaveAcesso(String chaveAcesso) {
        this.chaveAcesso = chaveAcesso;
    }

}
