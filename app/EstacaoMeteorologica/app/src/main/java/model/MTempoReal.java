package model;

import java.io.Serializable;
import java.util.Date;

public class MTempoReal implements Serializable {

    private int id;
    private MLeitura leitura;
    private String valor;
    private Date data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MLeitura getLeitura() {
        return leitura;
    }

    public void setLeitura(MLeitura leitura) {
        this.leitura = leitura;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
