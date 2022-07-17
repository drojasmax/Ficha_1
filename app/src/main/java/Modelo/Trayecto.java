package Modelo;

public class Trayecto {
    private int idP;
    private String patente;
    private String inicio;
    private String destino;

    public Trayecto(int idP, String patente, String inicio, String destino) {
        this.idP = idP;
        this.patente = patente;
        this.inicio = inicio;
        this.destino = destino;
    }

    public int getIdP() {
        return idP;
    }

    public void setIdP(int idP) {
        this.idP = idP;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }
}
