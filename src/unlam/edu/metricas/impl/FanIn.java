package unlam.edu.metricas.impl;

import java.util.List;

import unlam.edu.ayuda.Cadenas;
import unlam.edu.entidades.Clase;
import unlam.edu.entidades.Metodo;
import unlam.edu.entidades.Nombrable;
import unlam.edu.metricas.Metrica;
import unlam.edu.metricas.ResultadoMetrica;

public class FanIn implements Metrica, Nombrable {

    private List<Clase> proyecto;
    private Integer fanIn;

    public FanIn(List<Clase> proyecto) {
        this.proyecto = proyecto;
    }

    public String getNombre() {
        return "Fan In";
    }

    public void calcular(Metodo metodo) {
        this.fanIn = 0;
        for (Clase claseProyecto : this.proyecto) {
            for (Metodo metodoClaseProyecto : claseProyecto.getMetodos()) {
                this.fanIn += Cadenas.cantidadOcurrenciasMetodo(metodo.getNombre(), metodoClaseProyecto.getCodigo());
            }
        }
    }

    public ResultadoMetrica obtenerResultado() {
        return new ResultadoMetrica(this.getNombre(), this.fanIn.toString());
    }

}
