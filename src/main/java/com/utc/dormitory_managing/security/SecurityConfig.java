package com.utc.dormitory_managing.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.utc.dormitory_managing.security.securityv2.CustomUserDetailsService;
import com.utc.dormitory_managing.security.securityv2.JwtAuthenticationEntryPoint;
import com.utc.dormitory_managing.security.securityv2.JwtAuthenticationFilter;
import com.utc.dormitory_managing.security.securityv2.UserPrincipal;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig {
	@Autowired
	CustomUserDetailsService customUserDetailsService;
 
	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;

	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;
	
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Autowired
	private NoPasswordAuthenticationProvider noPasswordAuthenticationProvider;

//	  @Bean
//	  public AuthTokenFilter authenticationJwtTokenFilter() {
//	    return new AuthTokenFilter();
//	  }
	
	private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/auth/**",
            "/**"
	};
	  
	  @Bean
	  public DaoAuthenticationProvider authenticationProvider() {
	      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	       
	      authProvider.setUserDetailsService(customUserDetailsService);
	      authProvider.setPasswordEncoder(passwordEncoder());
	   
	      return authProvider;
	  }
	  @Bean
	  public JwtAuthenticationFilter authenticationJwtTokenFilter() {
	    return new JwtAuthenticationFilter();
	  }
	  
	  @Bean
	  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
	    return authConfig.getAuthenticationManager();
	  }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	 @Bean
	  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.csrf(csrf -> csrf.disable())
	    .cors(cors -> cors.disable())
	    .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .authorizeHttpRequests((auth) -> 
	          auth.requestMatchers(WHITE_LIST_URL
					).permitAll()
	          .requestMatchers("/swagger-ui/index.html#/").permitAll()
	              .anyRequest().authenticated()
	        );
	    
	    http.authenticationProvider(authenticationProvider());

	    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	    
	    return http.build();
		 
//		 return http
//	                .csrf()
//	                .disable()
//	                .authorizeHttpRequests()
//	                .requestMatchers(AntPathRequestMatcher
//	                                .antMatcher("/"),
//	                        AntPathRequestMatcher
//	                                .antMatcher("/error"),
//	                        AntPathRequestMatcher
//	                                .antMatcher("/favicon.ico"),
//	                        AntPathRequestMatcher
//	                                .antMatcher("/actuator/*"),
//	                        AntPathRequestMatcher
//	                                .antMatcher("/a")
//	                )
//	                .permitAll()
//	                .anyRequest()
//	                .authenticated()
//	                .and()
//	                .sessionManagement()
//	                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//	                .and()
//	                .authenticationProvider(authenticationProvider())
//	                .addFilterBefore(new JwtAuthenticationFilter(userDetailsService()), UsernamePasswordAuthenticationFilter.class)
//	                .build();
	  }

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOriginPattern("*"); // Allows all origins
		config.addAllowedHeader("*"); // Allows all headers
		config.addAllowedMethod("*"); // Allows all methods (GET, POST, etc.)
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}