package com.devsuperior.bds04.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer // processa a funcionalidade do Oauth2
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private Environment env; // para liberar o H2 no perfil test

	@Autowired
	private JwtTokenStore tokenStore;

	private static final String[] PUBLIC = { "/oauth/token", "/h2-console/**" }; // Rota pública
	
	private static final String[] PUBLIC_GET = { "/cities/**", "/events/**" };
	
	private static final String[] CLIENT_POST = {"/events/**" };
	
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore); // configurando o tokenstore e validando-o
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

		// se eu estou rodando um profile test, quero liberar o h2
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable(); // liberar o H2 - desabilita os frames
		}

		http.authorizeRequests().antMatchers(PUBLIC).permitAll() // rotas até public todos tem acesso liberado
		
				// GET em "/cities/**", "/events/**" está liberada:
				.antMatchers(HttpMethod.GET, PUBLIC_GET).permitAll()
				
				// CLIENT só pode dar post em events:
				.antMatchers(HttpMethod.POST, CLIENT_POST).hasAnyRole("CLIENT")
				
				// demais rotas, somente o ADMIN pode:
				.anyRequest().hasAnyRole("ADMIN");

	}
}