package Modelo;

public class Pasajero {

    private int id;
    private String nombre;
    private String apellido;
    private String contra;
    private int numero;
    private String foto;

    public Pasajero(int id, String nombre, String apellido, String contra, int numero, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.contra = contra;
        this.numero = numero;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
