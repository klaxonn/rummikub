package rummikub.joueurs;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RepertoireJoueurConnecte extends JpaRepository<JoueurConnecte, Long> {
    JoueurConnecte findByNom(String nom);
}
