package com.koala.uaa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.koala.uaa.mapper")
@SpringBootApplication
public class KoalaUaaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KoalaUaaApplication.class, args);
	}

}
