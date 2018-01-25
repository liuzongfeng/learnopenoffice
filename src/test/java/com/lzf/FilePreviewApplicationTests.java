package com.lzf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import rest.service.FileDubboCustomerService;

@RunWith(SpringRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！ 
@SpringBootTest
public class FilePreviewApplicationTests {

	@Autowired
	private FileDubboCustomerService fileDubboCustomerService;
	@Test
	public void contextLoads() {
		String result = fileDubboCustomerService.testDubbo();
		System.out.println(result);
	}

}
