package bilallafdili.u5_w3_d5.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import enums.RuoloUtente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "utenti")
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties({"password", "credentialsNonExpired", "enabled",
        "accountNonExpired", "authorities", "accountNonLocked"})
public class Utente implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    private RuoloUtente ruolo;

    public Utente(String username, String nome, String cognome, String email, String password, RuoloUtente ruolo) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.avatarUrl = "https://ui-avatars.com/api/?name=" + nome + "+" + cognome;
        this.ruolo = ruolo != null ? ruolo : RuoloUtente.UTENTE_NORMALE;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.ruolo.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
