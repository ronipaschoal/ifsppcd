package model;

import java.io.Serializable;

/**
 *
 * Classe: MLeitura
 * Objetivo: Classe model de Sensor Instalado
 *
 * @author Isadora Giacomini de Moraes
 * @since 02/12/2018
 *
 */
public class MLeitura implements Serializable {

    private int id;
    private MEstacao estacao;
    private MSensor sensor;
    private String codigo;
    private String nome;
    private String unidadeMedida;
    private boolean ativo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MEstacao getEstacao() {
        return estacao;
    }

    public void setEstacao(MEstacao estacao) {
        this.estacao = estacao;
    }

    public MSensor getSensor() {
        return sensor;
    }

    public void setSensor(MSensor sensor) {
        this.sensor = sensor;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
