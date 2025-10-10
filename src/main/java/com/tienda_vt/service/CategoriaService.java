package com.tienda_vt.service;

import com.tienda_vt.domain.Categoria;
import com.tienda_vt.repository.CategoriaRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean activo) {
        if (activo) {
            return categoriaRepository.findByActivoTrue();
        }
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Categoria> getCategoria(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }

    @Transactional(readOnly = true)
    public void eliminar(Integer idCategoria) {
           //Se verifica que el idCategoria exista
        if (!categoriaRepository.existsById(idCategoria)) {
            //Lanza la excepción...
            throw new IllegalArgumentException("La categoría " + idCategoria + " no existe");
        }
        try {
            categoriaRepository.deleteById(idCategoria);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar porque tiene datos asociados " + e);
        }
    }
  
    @Autowired
    private FirebaseStorageService firebaseStorageService;
    
    //Este método tiene 2 funciones... si idCategoria NO tiene información, entonces se crea un registro (insert)
    //Si idCateoria tiene info, se actualiza el registro que tiene ese idCategoria
    @Transactional
    public void save(Categoria categoria, MultipartFile imageFile) {
        categoria = categoriaRepository.save(categoria);
        if (!imageFile.isEmpty()) {
            try {
                String rutaImagen = firebaseStorageService.uploadImage(imageFile, "categoria", categoria.getIdCategoria());
                categoria.setRutaImagen(rutaImagen);
                categoriaRepository.save(categoria);
            } catch (IOException e) {
            }
        }
    }

    public void addFlashAttribute(String todoOk, String message) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
