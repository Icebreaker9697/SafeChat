package hello;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class Application {
	public static void main(String[] args){
		Security.addProvider(new BouncyCastleProvider());
		ApplicationContext ac=SpringApplication.run(Application.class, args);
	}
}