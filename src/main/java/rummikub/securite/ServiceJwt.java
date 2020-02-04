package rummikub.securite;

import rummikub.joueurs.JoueurConnecte;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;

@Service
public class ServiceJwt {

    private SecretKey cle;

    public ServiceJwt() {
		cle = Keys.secretKeyFor(SignatureAlgorithm.HS384);
	}

    public JoueurConnecte parseToken(String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(cle)
                    .parseClaimsJws(token)
                    .getBody();

            JoueurConnecte joueur = new JoueurConnecte();
            joueur.setNom(body.getSubject());

            return joueur;

        } catch (JwtException | ClassCastException e) {
            return null;
        }
    }

    public String creerToken(JoueurConnecte joueur) {
        Claims claims = Jwts.claims().setSubject(joueur.getNom());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(cle)
                .compact();
    }
}
