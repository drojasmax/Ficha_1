package Modelo;

import java.util.List;
/**
 * Clase Leg que posee una lista de pasos (Steps)
 *
 * Esta clase se usa para almacenar los pasos entre un punto de origen y uno de destino.
 *
 */
public class Leg {
    private List<Step> steps;

    public List<Step> getSteps() {
        return steps;
    }
}
