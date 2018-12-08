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
    private String nome;
    private int tempoLeitura;
    private boolean ativo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getTempoLeitura() {
        return tempoLeitura;
    }

    public void setTempoLeitura(int tempoLeitura) {
        this.tempoLeitura = tempoLeitura;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
