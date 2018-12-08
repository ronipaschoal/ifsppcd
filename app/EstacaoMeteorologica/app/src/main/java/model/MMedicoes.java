package model;

import java.util.Date;

/**
 *
 * Classe: MMedicoes
 * Objetivo: Classe model de Medições
 *
 * @author Isadora Giacomini de Moraes
 * @since 02/12/2018
 *
 */
public class MMedicoes {

    private int id;
    private Date date;
    private String valor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
