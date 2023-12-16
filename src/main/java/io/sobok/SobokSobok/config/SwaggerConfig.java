package io.sobok.SobokSobok.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(
            @Value("${springdoc.version}") String version
    ) {

        Info info = new Info()
                .title("SobokSobok API 문서") // 타이틀
                .version(version) // 문서 버전
                .description("소복소복 서버 API 명세서 입니다.") // 문서 설명
                .contact(new Contact() // 연락처
                        .name("dev-Crayon")
                        .email("seungheon328@gmail.com"));

        return new OpenAPI()
                .info(info);
    }
}
