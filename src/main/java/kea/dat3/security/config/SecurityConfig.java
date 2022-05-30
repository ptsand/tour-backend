package kea.dat3.security.config;

import kea.dat3.repositories.PersonRepository;
import kea.dat3.security.UserDetailsServiceImp;
import kea.dat3.security.UserWithPassword;
import kea.dat3.security.jwt.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(
        //securedEnabled = true,
        jsr250Enabled = true // enables @RolesAllowed annotation.
        //prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserDetailsServiceImp userDetailsService;
  private final JwtTokenFilter jwtTokenFilter;

  public SecurityConfig(UserDetailsServiceImp userDetailsService, JwtTokenFilter jwtTokenFilter) {
    this.userDetailsService = userDetailsService;
    this.jwtTokenFilter = jwtTokenFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return UserWithPassword.getPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // Enable CORS and disable CSRF
    http.cors().and().csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .exceptionHandling()
            .authenticationEntryPoint(
                    (request, response, ex) -> {
                      response.sendError(
                              HttpServletResponse.SC_UNAUTHORIZED,
                              ex.getMessage()
                      );
                    }
            )
            .and()
            .authorizeRequests()
            .antMatchers("/api/auth/**").permitAll()
            .antMatchers("/error").permitAll() //Without this you are not allowed to see security related error responses
            .antMatchers(HttpMethod.GET, "/api/message/all").permitAll()
            .antMatchers(HttpMethod.GET, "/index.html").permitAll()
            // All other endpoints are private
            //.anyRequest().authenticated();
            .anyRequest().permitAll();  //Disable Security TODO: enable security
    http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

}
