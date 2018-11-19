package com.wqs.qctwe;

import com.wqs.qctwe.util.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
@SpringBootApplication
@MapperScan("com.wqs.qctwe.mapper")
@ComponentScan("com.wqs.qctwe")
public class QctWEApplication {

	@Bean
	public SpringUtil getSpringUtil(){
		return new SpringUtil();
	}

	public static void main(String[] args) {
		SpringApplication.run(QctWEApplication.class, args);
	}
}
