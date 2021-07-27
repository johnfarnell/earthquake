package au.com.auspost;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.models.dto.builder.ApiInfoBuilder;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

@Configuration
@EnableSwagger
public class SwaggerConfig {

	@Inject
	private SpringSwaggerConfig swaggerConfig;
	
	@Bean
	public SwaggerSpringMvcPlugin configureSwagger() {
		SwaggerSpringMvcPlugin swaggerPlugin = new SwaggerSpringMvcPlugin(this.swaggerConfig);
		
		ApiInfo apiInfo = new ApiInfoBuilder()
							        .title("Earthquakes API - John Farnell")
							        .description("Earthquakes Api for viewing and creating earthquake data")
							        .termsOfServiceUrl("none")
							        .contact("john.farnell@gmail.com")
							        .license("none")
							        .licenseUrl("none")
							        .build();
		
		swaggerPlugin
					.apiInfo(apiInfo)
					.apiVersion("1.0")
					.includePatterns("/stations/*.*", "/regions/*.*", "/earthquakes/*.*");


		swaggerPlugin.useDefaultResponseMessages(false);
		
	    return swaggerPlugin;
	}
}
