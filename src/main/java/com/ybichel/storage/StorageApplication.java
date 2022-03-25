package com.ybichel.storage;

import com.ybichel.storage.common.exception.GlobalControllerAdviceExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
@Import(GlobalControllerAdviceExceptionHandler.class)
public class StorageApplication {
	public static void main(String[] args) {

		SpringApplication.run(StorageApplication.class, args);
	}
}
