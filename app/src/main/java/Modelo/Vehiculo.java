package Modelo;

public class Vehiculo {

    private String patente;
    private String linea;
    private int idC;
    private String tipo;
    private int ano;
    private String color;

    public Vehiculo(String patente, String linea, int idC, String tipo, int ano, String color) {
        this.patente = patente;
        this.linea = linea;
        this.idC = idC;
        this.tipo = tipo;
        this.ano = ano;
        this.color = color;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public int getIdC() {
        return idC;
    }

    public void setIdC(int idC) {
        this.idC = idC;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
