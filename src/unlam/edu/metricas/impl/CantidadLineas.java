package unlam.edu.metricas.impl;

import java.util.List;

import unlam.edu.entidades.Metodo;
import unlam.edu.entidades.Nombrable;
import unlam.edu.metricas.Metrica;
import unlam.edu.metricas.ResultadoMetrica;

public class CantidadLineas implements Metrica, Nombrable {

    private Integer cantidadLineas;

    public String getNombre() {
        return "Cantidad de lineas";
    }

    public void calcular(Metodo metodo) {
        List<String> codigo = metodo.getCodigo();
        this.cantidadLineas = codigo.size();
    }

    public ResultadoMetrica obtenerResultado() {
        return new ResultadoMetrica(this.getNombre(), this.cantidadLineas.toString());
    }
}
