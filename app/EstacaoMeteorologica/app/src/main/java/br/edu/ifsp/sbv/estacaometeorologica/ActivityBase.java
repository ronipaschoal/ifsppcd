package br.edu.ifsp.sbv.estacaometeorologica;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 *
 * Classe: ActivityBase
 * Objetivo: Classe base
 *
 * @author Isadora Giacomini de Moraes
 * @since 02/12/2018
 *
 */
public class ActivityBase {

    /**
     * Exibir mensagem
     * @param message - Mensagem a ser exibida
     */
    static void showMessage(Context context, String message){

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }
}
