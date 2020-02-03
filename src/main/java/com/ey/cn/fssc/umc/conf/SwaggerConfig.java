package com.ey.cn.fssc.umc.conf;

import com.ey.cn.fssc.umc.constant.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author King 2019/6/10 下午2:06
 * <>
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket wdAPI() {
//添加head参数start
        ParameterBuilder token = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        token.name("Authorization").description("Token").modelRef(new ModelRef("string"))
                .parameterType("header").required(false).build();
        pars.add(token.build());
        return new Docket(DocumentationType.SWAGGER_2).groupName(Constant.SWAGGER_GROUP_NAME).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.ey.cn.pi.cc")).paths(PathSelectors.any()).build().globalOperationParameters(pars);
    }

    @SuppressWarnings("deprecation")
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(Constant.SWAGGER_TITLE).description(Constant.SWAGGER_DESCRIPTION)
                .termsOfServiceUrl("http://www.ey.com/").contact("api-umc@cn.ey.com").version("1.0").build();
    }
}
