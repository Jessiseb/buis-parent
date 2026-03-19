package com.buis.web.core.config;

import com.buis.common.config.RuoYiConfig;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {
    /**
     * 系统基础配置
     */
    @Autowired
    private RuoYiConfig ruoyiConfig;

    /**
     * 是否开启swagger
     */
    @Value("${swagger.enabled}")
    private boolean enabled;


    /**
     * 创建API
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .enable(enabled)
                .groupName("2. 后台管理端API") // Group name for management system APIs
                .apiInfo(apiInfo())
                .select()
                // Management System APIs
                .apis(RequestHandlerSelectors.basePackage("com.buis.web.controller.manager"))
                .paths(PathSelectors.any()) // Adjusted for management paths
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .useDefaultResponseMessages(false);
    }

    @Bean
    public Docket createFrontendApi() {
        return new Docket(DocumentationType.OAS_30)
                .enable(enabled)
                .groupName("1. 学生、教师前台API") // Group name for frontend APIs
                .apiInfo(apiInfo())
                .select()
                // Frontend APIs, excluding '/buis' prefix
                .apis(RequestHandlerSelectors.basePackage("com.buis.web.controller.api"))
                .paths(PathSelectors.any()) // Adjusted for management paths
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    @Bean
    public Docket createCommonApi() {
        return new Docket(DocumentationType.OAS_30)
                .enable(enabled)
                .groupName("3. 通用功能API") // Group name for frontend APIs
                .apiInfo(apiInfo())
                .select()
                // Frontend APIs, excluding '/buis' prefix
                .apis(RequestHandlerSelectors.basePackage("com.buis.web.controller.common"))
                .paths(PathSelectors.any()) // Adjusted for management paths
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    /**
     * 安全模式，这里指定token通过Authorization头请求头传递
     */
    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> apiKeyList = new ArrayList<>();
        apiKeyList.add(new ApiKey("Authorization", "Authorization", In.HEADER.toValue()));
        return apiKeyList;
    }

    /**
     * 安全上下文
     */
    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(SecurityContext.builder()
                .securityReferences(defaultAuth())
                .operationSelector(o -> o.requestMappingPattern().matches("/.*"))
                .build());
        return securityContexts;
    }

    /**
     * 默认的安全上引用
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }

    /**
     * 添加摘要信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 设置标题
                .title("标题：广白创新创业赛事平台管理系统_接口文档")
                // 描述
                .description("描述：用于管理广白创新创业赛事平台信息,具体包括比赛模块,组队模块...")
                // 作者信息
                .contact(new Contact(ruoyiConfig.getName(), null, null))
                // 版本
                .version("版本号:" + ruoyiConfig.getVersion())
                .build();
    }
}
