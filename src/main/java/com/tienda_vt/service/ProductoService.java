package com.tienda_vt.service;

import com.tienda_vt.domain.Producto;
import com.tienda_vt.repository.ProductoRepository;
import java.io.IOException;
import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<Producto> getProductos(boolean activo) {
        if (activo) {
            return productoRepository.findByActivoTrue();
        }
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Producto> getProducto(Integer idProducto) {
        return productoRepository.findById(idProducto);
    }

    @Transactional(readOnly = true)
    public void eliminar(Integer idProducto) {
        //Se verifica que el idProducto exista
        if (!productoRepository.existsById(idProducto)) {
            //Lanza la excepción...
            throw new IllegalArgumentException("La categoría " + idProducto + " no existe");
        }
        try {
            productoRepository.deleteById(idProducto);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar porque tiene datos asociados " + e);
        }
    }

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    //Este método tiene 2 funciones... si idProducto NO tiene información, entonces se crea un registro (insert)
    //Si idCateoria tiene info, se actualiza el registro que tiene ese idProducto
    @Transactional
    public void save(Producto producto, MultipartFile imageFile) {
        producto = productoRepository.save(producto);

        // Evitar NullPointerException
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                System.out.println("Voy a guardar");
                String rutaImagen = firebaseStorageService.uploadImage(
                        imageFile, "producto", producto.getIdProducto()
                );
                producto.setRutaImagen(rutaImagen);
                productoRepository.save(producto);
            } catch (IOException e) {
                System.out.println("Hubo un error");
                e.printStackTrace(); // opcional, para debug
            }
        }
    }

    @Transactional(readOnly = true)
    public List<Producto> consultaDerivada(BigDecimal precioInf, BigDecimal precioSup) {

        return productoRepository.findByPrecioBetweenOrderByPrecioAsc(precioInf, precioSup);
    }

    @Transactional(readOnly = true)
    public List<Producto> consultaJPQL(BigDecimal precioInf, BigDecimal precioSup) {

        return productoRepository.consultaJPQL(precioInf, precioSup);
    }

    @Transactional(readOnly = true)
    public List<Producto> consultaSQL(BigDecimal precioInf, BigDecimal precioSup) {

        return productoRepository.consultaSQL(precioInf, precioSup);
    }

    @Transactional(readOnly = true)
    public List<Producto> consultaAmpliada() {
        var lista = new ArrayList<Producto>();
        lista.add(productoRepository.findFirstByOrderByPrecioAsc());  // Min
        lista.add(productoRepository.findFirstByOrderByPrecioDesc()); // Max
        return lista;
    }
}
