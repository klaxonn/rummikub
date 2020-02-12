package rummikub.securite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configure la sécurité de l'application.
 */
@Configuration
@EnableWebSecurity
public class ConfigSecurite extends WebSecurityConfigurerAdapter {

	@Autowired
	private ServiceJwt serviceJwt;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
            .csrf().disable()
            .requiresChannel()
            .anyRequest()
            .requiresSecure()
            .and()
            .authorizeRequests()
            .antMatchers("/0/*").permitAll()
            .antMatchers("/*/ajouterJoueur").permitAll()
            .anyRequest().authenticated()
            .and()
            .exceptionHandling().authenticationEntryPoint(new GestionAccesInterdit())
            .and()
            .addFilterBefore(new FiltreAutorisationJwt(serviceJwt),
                UsernamePasswordAuthenticationFilter.class)
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        ;
    }
}
