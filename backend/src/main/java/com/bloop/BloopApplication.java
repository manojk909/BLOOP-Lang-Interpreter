package com.bloop;

import com.bloop.util.OutputCapture;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BloopApplication {

    /**
     * Install the thread-routing output capture BEFORE Spring starts,
     * so all System.out/err writes from BLOOP are thread-safely captured
     * without interfering with Spring Boot's own logging.
     */
    static {
        OutputCapture.install();
    }

    public static void main(String[] args) {
        SpringApplication.run(BloopApplication.class, args);
    }

    /**
     * Global CORS configuration — allow all origins so the Vercel frontend
     * can call the Render backend without browser CORS errors.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "OPTIONS")
                        .allowedHeaders("*")
                        .maxAge(3600);
            }
        };
    }
}
