package org.sarge.jove.demo.triangle;

import org.apache.commons.lang3.builder.*;
import org.sarge.jove.common.TransientObject;
import org.sarge.jove.io.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TriangleDemo {
	@Bean
	public static DataSource source() {
		return new ClasspathDataSource();
	}

	@Bean
	static DestructionAwareBeanPostProcessor destroyer() {
		return new DestructionAwareBeanPostProcessor() {
			@Override
			public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
				if(bean instanceof TransientObject obj && !obj.isDestroyed()) {
					obj.destroy();
				}
			}
		};
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
		SpringApplication.run(TriangleDemo.class, args);
	}
}
