package rummikub.securite;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.net.MalformedURLException;

/**
 * Gestion de l'autorisation.
 *
 * Donne une autorisation aux joueurs et vérifie leur identité et autorisation.
 */
public class FiltreAutorisationJwt extends OncePerRequestFilter {

	private ServiceJwt serviceJwt;
    private static final String HEADER = "Authorization";
    private static final String PREFIXE_HEADER = "Bearer ";

    public FiltreAutorisationJwt(ServiceJwt serviceJwt) {
        this.serviceJwt = serviceJwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest requete,
                                    HttpServletResponse reponse,
                                    FilterChain chain) throws IOException, ServletException {
		String header = requete.getHeader(HEADER);

        if (header == null || !header.startsWith(PREFIXE_HEADER)) {
			chain.doFilter(requete, reponse);
        }
        else {
			UsernamePasswordAuthenticationToken authentication = getAutorisation(requete,
			  header.substring(PREFIXE_HEADER.length()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(requete, reponse);
		}
    }

    private UsernamePasswordAuthenticationToken getAutorisation(HttpServletRequest requete, String token) throws MalformedURLException {
		JoueurConnecte joueur = serviceJwt.parseToken(token);

		if (joueur != null) {
			URL url = new URL(requete.getRequestURL().toString());
			String[] elements = url.getPath().substring(1).split("/");
			int idPartieAdresse = Integer.parseInt(elements[0]);
			if(idPartieAdresse > 0) {
				int idJoueurAdresse = Integer.parseInt(elements[1]);
				String adresseIP = requete.getLocalAddr();
				if(joueur.getId() == idJoueurAdresse && joueur.getIdPartie() == idPartieAdresse
				  && joueur.getAdresseIP().equals(adresseIP)) {
					return new UsernamePasswordAuthenticationToken(joueur.getNom(), null, new ArrayList<>());
				}
				else {
					return null;
				}
			}
			else {
				return new UsernamePasswordAuthenticationToken(joueur.getNom(), null, new ArrayList<>());
			}
        }
        return null;
    }
}
