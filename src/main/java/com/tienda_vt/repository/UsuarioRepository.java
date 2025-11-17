package com.tienda_vt.repository;

import com.tienda_vt.domain.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author melissa
 */
@Repository 
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    public Optional<Usuario> findByUsernameAndActivoTrue(String username);
}
