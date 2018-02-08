package rest;

import java.util.Properties;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

import rest.service.FileDubboCustomerService;

@SpringBootApplication
@EnableScheduling
@ComponentScan(value = "rest.*")
@RestController
public class FilePreviewApplication {
	
	/*@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().authorizeRequests().anyRequest().authenticated();
	}
	@Bean
	HeaderHttpSessionStrategy sessionStrategy() {
		return new HeaderHttpSessionStrategy();
	}*/
	
	@Autowired
	private FileDubboCustomerService fileDubboCustomerService;
	
	@Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(1024L * 1024L * 100);
        return factory.createMultipartConfig();
    }

	public static void main(String[] args) {
        Properties properties = System.getProperties();
        System.out.println(properties.get("user.dir"));
        SpringApplication.run(FilePreviewApplication.class, args);
	}
	
	/*@RequestMapping(value = "/getsession")
	@ResponseBody
	@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = { "x-auth-token", "x-requested-with" })
	public String home(HttpSession session) {
		
		String resutl = fileDubboCustomerService.testDubbo();
		System.out.println(resutl);
		
		
		SecurityContext context = (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT");
		User user = (User)context.getAuthentication().getPrincipal();
		
		String comNum = user.getUsername();
		System.out.println(comNum);
		if(null == comNum){
			throw new RuntimeException("请登录");
		}else{
			return comNum;
		}
	}*/
}
