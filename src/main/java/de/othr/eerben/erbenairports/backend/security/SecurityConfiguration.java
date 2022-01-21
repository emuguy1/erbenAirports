package de.othr.eerben.erbenairports.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String[] ALLOW_ACCESS_WITHOUT_AUTHENTICATION = {
            "/css/**",
            "/js/**",
            "/image/**",
            "/fonts/**",
            "/",
            "/login",
            "/register",
            "/icons/**",
            "/departures",
            "/arrivals",
            "/departure",
            "/arrival",
            "/connection",
            "/error",
            "/flight/*/details",
            "/api/rest/**"
    };
    private static final String[] ALLOW_ACCESS_AS_CUSTOMER = {
            "/customer/**"
    };
    private static final String[] ALLOW_ACCESS_AS_EMPLOYEE = {
            "/airport/new",
            "/employee/new",
            "/customers"
    };
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AirportSecurityUtilities securityUtilities;

    private BCryptPasswordEncoder passwordEncoder() {
        return securityUtilities.passwordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(ALLOW_ACCESS_WITHOUT_AUTHENTICATION)
                .permitAll()
                .antMatchers(ALLOW_ACCESS_AS_EMPLOYEE)
                .hasAuthority("EMPLOYEE")
                .antMatchers(ALLOW_ACCESS_AS_CUSTOMER)
                .hasAuthority("CUSTOMER")
                .anyRequest().authenticated();
        http
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=true")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .deleteCookies("remember-me")
                .permitAll()
                .and()
                .rememberMe();
        // Cross-Site Request Forgery ausschalten
        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

}

