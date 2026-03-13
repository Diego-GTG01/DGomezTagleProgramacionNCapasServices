
package com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "Modelo de dirección", example="{" + //
        "        \"Calle:Avenida San Sebastian ," + //
        "        \"Colonia: {" +
        "            \"Municipio\": {\r\n" + //
        "                \"Estado\": {\r\n" + //
        "                    \"Pais\": {\r\n" + //
        "                        \"IdPais\": 1,\r\n" + //
        "                        \"Nombre\": \"México\"\r\n" + //
        "                    },\r\n" + //
        "                    \"IdEstado\": 1,\r\n" + //
        "                    \"Nombre\": \"México\"\r\n" + //
        "                },\r\n" + //
        "                \"IdMunicipio\": 54,\r\n" + //
        "                \"Nombre\": \"Zumpango\"\r\n" + //
        "            },\r\n" + //
        "            \"CodigoPostal\": \"55600\",\r\n" + //
        "            \"IdColonia\": 8243,\r\n" + //
        "            \"Nombre\": \"San Sebastián\"\r\n" + //
        "        },\r\n" + //
        "        \"IdDireccion\": 4,\r\n" + //
        "        \"NumeroExterior\": \"17\",\r\n" + //
        "        \"NumeroInterior\": null\r\n" + //
        "    }")
@Entity
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddireccion")
    private int IdDireccion;
    @NotEmpty(message = "Este campo no Puede estar vacio")

    @Column(name = "calle")
    private String Calle;
    @NotEmpty(message = "Este campo no Puede estar vacio")

    @Column(name = "numeroexterior")
    private String NumeroExterior;

    @Column(name = "numerointerior")
    private String NumeroInterior;
    @Valid
    @ManyToOne
    @JoinColumn(name = "idcolonia")
    public Colonia Colonia;

    @ManyToOne(fetch = FetchType.LAZY) // carga Perezosa
    @JoinColumn(name = "idusuario")
    @JsonIgnore
    public Usuario Usuario;

    public Direccion() {
        this.Colonia = new Colonia();

    }

    public Direccion(Colonia Colonia) {
        this.Colonia = Colonia;
    }

    public Direccion(int IdDireccion, String Calle, String NumeroExterior, String NumeroInterior, Colonia Colonia) {
        this.IdDireccion = IdDireccion;
        this.Calle = Calle;
        this.NumeroExterior = NumeroExterior;
        this.NumeroInterior = NumeroInterior;
        this.Colonia = Colonia;
    }

    public int getIdDireccion() {
        return IdDireccion;
    }

    public void setIdDireccion(int IdDireccion) {
        this.IdDireccion = IdDireccion;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String Calle) {
        this.Calle = Calle;
    }

    public String getNumeroExterior() {
        return NumeroExterior;
    }

    public void setNumeroExterior(String NumeroExterior) {
        this.NumeroExterior = NumeroExterior;
    }

    public String getNumeroInterior() {
        return NumeroInterior;
    }

    public void setNumeroInterior(String NumeroInterior) {
        this.NumeroInterior = NumeroInterior;
    }

    

    @Override
    public String toString() {
        String Cadena = "";
        Cadena += "\nCalle = " + Calle;
        Cadena += "\nNumeroExterior = " + NumeroExterior;
        Cadena += "\nNumeroInterior = " + NumeroInterior;
        Cadena += "\nColonia = " + Colonia.getNombre();
        Cadena += "\nMunicipio = " + Colonia.Municipio.getNombre();
        Cadena += "\nEstado = " + Colonia.Municipio.Estado.getNombre();
        Cadena += "\nPais = " + Colonia.Municipio.Estado.Pais.getNombre();

        return Cadena;
    }

}
