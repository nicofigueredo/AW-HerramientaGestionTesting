package unlam.edu.metricas.impl;

import java.util.List;

import unlam.edu.ayuda.Cadenas;
import unlam.edu.entidades.Clase;
import unlam.edu.entidades.Metodo;
import unlam.edu.entidades.Nombrable;
import unlam.edu.metricas.Metrica;
import unlam.edu.metricas.ResultadoMetrica;

public class FanOut implements Metrica, Nombrable {

    private List<Clase> proyecto;
    private Integer fanOut;

    public FanOut(List<Clase> proyecto) {
        this.proyecto = proyecto;
    }

    public String getNombre() {
        return "Fan Out";
    }

    public void calcular(Metodo metodo) {
        this.fanOut = 0;
        for (Clase claseProyecto : this.proyecto) {
            for (Metodo metodoClaseProyecto : claseProyecto.getMetodos()) {
                this.fanOut += Cadenas.cantidadOcurrenciasMetodo(metodoClaseProyecto.getNombre(), metodo.getCodigo());
            }
        }
    }

    public ResultadoMetrica obtenerResultado() {
        return new ResultadoMetrica(this.getNombre(), this.fanOut.toString());
    }

}
