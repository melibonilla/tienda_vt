/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda_vt.domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@Entity
@Table (name ="producto")
public class Producto implements Serializable {
    private static final long serialVersionUID=1L;

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;  
    private Integer idCategoria;
    
    @Column (unique=true, nullable=false, length=50)
    @NotNull
    @Size(max=50)
    private String descripcion;
    
    @Column(columnDefinition = "TEXT")
    private String detalle;
    
    @Column (precision=12, scale=2)
    @NotNull (message="El precio debe ser definido")
    @DecimalMin(value="0.00", inclusive=true, message="El precio debe ser mayor o igual a 0.00")
    private BigDecimal precio;
    
    @NotNull (message= "Las existencias, deben estar definidas")
    @Min (value=0, message = "Las existencias deben ser mayores o iguales a 0")
    private Integer existencias;
    
    @Size (max=1024)
    private String rutaImagen;
    private boolean activo;
    
}
