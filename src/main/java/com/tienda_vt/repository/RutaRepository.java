package com.tienda_vt.repository;

import com.tienda_vt.domain.Ruta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author melissa
 */
@Repository 
public interface RutaRepository extends JpaRepository<Ruta, Integer> {
    
    public List<Ruta> findAllByOrderByRequiereRolAsc();
}
