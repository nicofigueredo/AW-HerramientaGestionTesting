package unlam.edu.metricas;

import unlam.edu.entidades.Metodo;

public interface Metrica {

    public void calcular(Metodo metodo);

    public ResultadoMetrica obtenerResultado();

}
