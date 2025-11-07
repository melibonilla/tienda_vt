package com.tienda_vt.repository;

import com.tienda_vt.domain.Categoria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author melissa
 */
@Repository 
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    
    public List<Categoria> findByActivoTrue();
}
