package fr.esgi.avis.port.out;

import fr.esgi.avis.business.Avatar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AvatarPort {
    Avatar save(Avatar avatar);
    Optional<Avatar> findById(Long id);
    List<Avatar> findAll();
    Page<Avatar> findAll(Pageable pageable);
    List<Avatar> findAllByJoueurId(Long joueurId);
    void deleteById(Long id);
}
