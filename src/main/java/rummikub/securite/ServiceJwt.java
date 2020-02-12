package rummikub.securite;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;

/**
 * Gestion des JWT : JSON Web Token
 */
@Service
public class ServiceJwt {

    private SecretKey cle;

	/**
	 * Constructeur par défaut
	 */
    public ServiceJwt() {
		cle = Keys.secretKeyFor(SignatureAlgorithm.HS384);
	}

	/**
	 * Analyse un token.
	 *
	 * @param token le token à analyser
	 * @return le joueur correspondant au token ou <code>null</code> en cas d'échec
	 */
    public JoueurConnecte parseToken(String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(cle)
                    .parseClaimsJws(token)
                    .getBody();

            JoueurConnecte joueur = new JoueurConnecte(body.getSubject());
            joueur.setId(body.get("id", Integer.class));
            joueur.setIdPartie(body.get("idPartie", Integer.class));
			joueur.setAdresseIP(body.get("adresseIP", String.class));
            return joueur;

        } catch (JwtException | ClassCastException e) {
            return null;
        }
    }

	/**
	 * Produit un token correspondant au joueur.
	 *
	 * @param joueur le joueur dont on veut un token
	 * @return le token
	 */
    public String creerToken(JoueurConnecte joueur) {
        return Jwts.builder()
                .setSubject(joueur.getNom())
                .claim("id", joueur.getId())
				.claim("idPartie", joueur.getIdPartie())
				.claim("adresseIP", joueur.getAdresseIP())
                .signWith(cle)
                .compact();
    }
}
