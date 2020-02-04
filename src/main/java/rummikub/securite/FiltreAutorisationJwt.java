package rummikub.securite;

import rummikub.joueurs.JoueurConnecte;
import rummikub.joueurs.RepertoireJoueurConnecte;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FiltreAutorisationJwt extends BasicAuthenticationFilter {

	private ServiceJwt serviceJwt;
    private RepertoireJoueurConnecte repertoireJoueurConnecte;
    private static final String HEADER = "Authorisation";
    private static final String PREFIXE_HEADER = "Bearer ";

	private static final Logger logger = LoggerFactory.getLogger(FiltreAutorisationJwt.class);

    public FiltreAutorisationJwt(AuthenticationManager authenticationManager, ServiceJwt serviceJwt,
      RepertoireJoueurConnecte repertoireJoueurConnecte) {
        super(authenticationManager);
        this.serviceJwt = serviceJwt;
        this.repertoireJoueurConnecte = repertoireJoueurConnecte;
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
			UsernamePasswordAuthenticationToken authentication = getAutorisation(requete);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(requete, reponse);
		}
    }

    private UsernamePasswordAuthenticationToken getAutorisation(HttpServletRequest requete) {
        String token = requete.getHeader(HEADER);

        if (token != null) {
			token = token.substring(7);
			JoueurConnecte joueur = serviceJwt.parseToken(token);

            if (joueur != null) {
				logger.info("Autorisation Joueur trouvé: "+joueur);
                return new UsernamePasswordAuthenticationToken(joueur.getNom(), null, new ArrayList<>());
            }
            logger.info("Autorisation Joueur non trouvé");
            return null;
        }
        return null;
    }
}
