package com.synesisit.gpvas;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GpVasApplicationTests {

	@Test
	void contextLoads() {
		//long conversion test
		String dateString = "2022062300000000001";
		System.out.println("parse long value: " + Long.valueOf(dateString));
	}

}
