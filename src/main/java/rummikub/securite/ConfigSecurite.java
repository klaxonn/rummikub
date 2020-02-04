package rummikub.securite;

import rummikub.joueurs.RepertoireJoueurConnecte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class ConfigSecurite extends WebSecurityConfigurerAdapter {

	@Autowired
	private ServiceJwt serviceJwt;

	@Autowired
	private RepertoireJoueurConnecte repertoireJoueurConnecte;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/0/creerPartie").permitAll()
            .antMatchers("/*/ajouterJoueur").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilter(new FiltreAutorisationJwt(authenticationManager(), serviceJwt,
               repertoireJoueurConnecte))
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        ;
    }
}