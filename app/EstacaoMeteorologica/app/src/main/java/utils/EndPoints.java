package utils;

public final class EndPoints {

    public static String URL_SERVER = "http://192.168.0.109/webservice/";

    public static String GET_ESTACOES = URL_SERVER + "TempoReal/getEstacoes.php";
    public static String SET_ESTACAO = URL_SERVER + "TempoReal/atualizaTempo.php";

    public static  String GET_LEITURAS = URL_SERVER + "TempoReal/medicaoCompleta.php";
    public static  String SET_LEITURA = URL_SERVER + "TempoReal/atualizaLeitura.php";

    public static  String GET_TEMPO_REAL =  URL_SERVER + "TempoReal/getTempoReal.php";
    public static  String GET_HISTORICO =  URL_SERVER + "TempoReal/getHistorico.php";
}