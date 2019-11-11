package api.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

//@Configuration
//class WebConfig : WebMvcConfigurer {
//
//    override fun addCorsMappings(registry: CorsRegistry?) {
//        registry!!.addMapping("/**")
//                .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH")
//                .allowedOrigins("*")
//                .allowedHeaders("X-Requested-With, X-Auth, Content-Type")
//                .maxAge(3600)
//                .allowCredentials(true)
//                .exposedHeaders("Location")
//    }
//
//    override fun configureHandlerExceptionResolvers(resolvers: MutableList<HandlerExceptionResolver>) {
//        super.configureHandlerExceptionResolvers(resolvers)
//    }
//}
