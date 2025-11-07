/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda_vt.domain;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
//import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author melissa
 */

@Data
@Entity
@Table (name ="categoria")
public class Categoria implements Serializable {
    private static final long serialVersionUID=1L;
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Integer idCategoria;
    
    @Column (unique=true, nullable=false, length=50)
    //@NotNull
    //@Size(max=50)
    private String descripcion;
    
    
    //@Size (max=1024)
    private String rutaImagen;
    private boolean activo;
    
    @OneToMany(mappedBy="categoria")
    private List<Producto> productos;
    
    
    
}
