package Modelo;

import java.util.List;
/**
 * Clase Direction que posee una lista de rutas (Routes)
 *
 * Esta clase se usa para acumular las rutas relacionadas a una dirección
 *
 */
public class Direction {
    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }
}
