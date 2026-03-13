package com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@Entity
public class Colonia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcolonia")
    @Min(value = 1, message = "Selecciona una opción")
    
    private int IdColonia;

    @Column(name = "nombre")
    private String Nombre;

    @Column(name = "codigopostal")
    private String CodigoPostal;

    @ManyToOne
    @JoinColumn(name = "idmunicipio")
    @Valid
    public Municipio Municipio;

    public Colonia() {
        this.Municipio = new Municipio();
    }

    public Colonia(Municipio Municipio) {
        this.Municipio = Municipio;
    }


    public int getIdColonia() {
        return IdColonia;
    }

    public void setIdColonia(int IdColonia) {
        this.IdColonia = IdColonia;
    }

    @Override
    public String toString() {
        return "Colonia{" + "IdColonia=" + IdColonia + ", Nombre=" + Nombre + ", CodigoPostal=" + CodigoPostal
                + ", Municipio=" + Municipio + '}';
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getCodigoPostal() {
        return CodigoPostal;
    }

    public void setCodigoPostal(String CodigoPostal) {
        this.CodigoPostal = CodigoPostal;
    }

}
