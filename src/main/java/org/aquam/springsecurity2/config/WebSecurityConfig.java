package org.aquam.springsecurity2.config;

import org.aquam.springsecurity2.models.AppUserRole;
import org.aquam.springsecurity2.service.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import javax.servlet.Filter;
import java.util.Arrays;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // annotations in controller
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DefaultUserService defaultUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SuccessLoginHandler successLoginHandler;

    @Autowired
    public WebSecurityConfig(DefaultUserService defaultUserService, BCryptPasswordEncoder bCryptPasswordEncoder, SuccessLoginHandler successLoginHandler) {
        this.defaultUserService = defaultUserService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.successLoginHandler = successLoginHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .authorizeRequests()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/person/**").hasAuthority(AppUserRole.PERSON.name())
                .antMatchers("/admin/**").hasAuthority(AppUserRole.ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login?error=true")
                    .successHandler(successLoginHandler)
                    .usernameParameter("MyUsername")
                    .passwordParameter("MyPassword")
                // param - in form - name
                .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // this MUST be POST if csrf is enabled; whenever we go to this /logout url with this GET method, we logout
                //.clearAuthentication(true)
                    .logoutSuccessUrl("/");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(defaultUserService);
        return provider;
    }

    @Bean
    public FilterRegistrationBean hiddenHttpMethodFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new HiddenHttpMethodFilter());
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        return filterRegistrationBean;
    }

}