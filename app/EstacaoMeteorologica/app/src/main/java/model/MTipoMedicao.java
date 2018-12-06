package model;

/**
 *
 * Classe: MTipoMedicao
 * Objetivo: Classe model de Tipo de Medição
 *
 * @author Isadora Giacomini de Moraes
 * @since 02/12/2018
 *
 */
public class MTipoMedicao {

    private int id;
    private String unidadeMedida;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }
}
