package Modelo;

import java.util.List;
/**
 * Clase Polilinea que posee un String de puntos (Points)
 *
 * Esta clase se usa para trazar las polilineas relacionadas a los puntos.
 *
 */
public class Route {
    private List<Leg> legs;

    public List<Leg> getLegs() {
        return legs;
    }
}
