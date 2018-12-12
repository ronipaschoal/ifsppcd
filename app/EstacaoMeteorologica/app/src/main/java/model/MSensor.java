package model;

import java.io.Serializable;

/**
 *
 * Classe: MSensor
 * Objetivo: Classe model de Sensor
 *
 * @author Isadora Giacomini de Moraes
 * @since 02/12/2018
 *
 */
public class MSensor implements Serializable {

    private int id;
    private String nome;

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
}
