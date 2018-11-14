package es.upm.miw.firebase.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Delivery {
    private String id;
    private User usuario;
    private Photo foto;
    private String fechaRegistro;
    private String fechaEntrega;
    private String incidencia;
    private String fotoQueja;
    private String repartidor;

    public Delivery() {
    }

    public Delivery(User usuario, Photo foto, String incidencia, String fotoQueja) {
        Date date = new Date();

        this.id = "Ref" + new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        this.usuario = usuario;
        this.foto = foto;
        this.fechaRegistro = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(date);
        this.fechaEntrega = "";
        this.incidencia = incidencia;
        this.fotoQueja = fotoQueja;
        this.repartidor = "repartidor por defecto";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Photo getFoto() {
        return foto;
    }

    public void setFoto(Photo foto) {
        this.foto = foto;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getIncidencia() {
        return incidencia;
    }

    public void setIncidencia(String incidencia) {
        this.incidencia = incidencia;
    }

    public String getFotoQueja() {
        return fotoQueja;
    }

    public void setFotoQueja(String fotoQueja) {
        this.fotoQueja = fotoQueja;
    }

    public String getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(String repartidor) {
        this.repartidor = repartidor;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id='" + id + '\'' +
                ", usuario=" + usuario +
                ", foto=" + foto +
                ", fechaRegistro='" + fechaRegistro + '\'' +
                ", fechaEntrega='" + fechaEntrega + '\'' +
                ", incidencia='" + incidencia + '\'' +
                ", fotoQueja='" + fotoQueja + '\'' +
                ", repartidor='" + repartidor + '\'' +
                '}';
    }
}

