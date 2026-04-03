package fr.esgi.avis.infrastructure.repository;

import fr.esgi.avis.business.Moderateur;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(ModerateurAdapter.class)
@DisplayName("ModerateurAdapter @DataJpaTest")
class ModerateurAdapterJpaTest {

    @Autowired
    private ModerateurAdapter moderateurAdapter;

    @Test
    @DisplayName("save + findById doit persister et remapper")
    void saveAndFindById() {
        Moderateur m = new Moderateur("0600000000");
        m.setPseudo("mod1");
        m.setEmail("mod1@mail.com");
        m.setMotDePasse("pwd");

        Moderateur saved = moderateurAdapter.save(m);
        assertThat(saved.getId()).isNotNull();

        var found = moderateurAdapter.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getNumeroDeTelephone()).isEqualTo("0600000000");
        assertThat(found.get().getEmail()).isEqualTo("mod1@mail.com");
    }

    @Test
    @DisplayName("findByEmail doit retrouver le modérateur")
    void findByEmail() {
        Moderateur m = new Moderateur("0700000000");
        m.setPseudo("mod2");
        m.setEmail("mod2@mail.com");
        m.setMotDePasse("pwd");
        moderateurAdapter.save(m);

        var found = moderateurAdapter.findByEmail("mod2@mail.com");
        assertThat(found).isPresent();
        assertThat(found.get().getPseudo()).isEqualTo("mod2");
    }
}

