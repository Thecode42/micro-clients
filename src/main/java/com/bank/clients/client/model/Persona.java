package com.bank.clients.client.model;

import jakarta.persistence.*;

@Entity
@Table(name = "persona", schema = "db_clientes")
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona", nullable = false)
    private long id;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "genero", length = 20)
    private String genero;

    @Column(name = "edad")
    private int edad;

    @Column(name = "identificacion", nullable = false, length = 20)
    private String identificacion;

    @Lob
    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @OneToOne(mappedBy = "idPersona")
    private Cliente cliente;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

}