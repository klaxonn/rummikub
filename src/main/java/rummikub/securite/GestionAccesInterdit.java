package rummikub.securite;

import rummikub.core.api.MessagePartie;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Gestion des échecs d'autorisation
 */
public class GestionAccesInterdit implements AuthenticationEntryPoint {

	/**
	 * Envoie un message en cas d'échec d'autorisation
	 */
    @Override
    public void commence(HttpServletRequest requete, HttpServletResponse reponse,
	   AuthenticationException authException) throws IOException {

        MessagePartie message = new MessagePartie();
		message.setTypeMessage(MessagePartie.TypeMessage.ERREUR);
		message.setMessageErreur("Opération non autorisée");
		reponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		reponse.setContentType("application/json");
        OutputStream out = reponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, message);
        out.flush();
    }
}

