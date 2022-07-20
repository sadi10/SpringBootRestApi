package com.synesisit.gpvas;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@Slf4j
class DateFormatTest {

	@Test
	void contextLoads() throws ParseException {

		String dateString  = "2022-06-19 17:32:01.408";

		Date registrationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);

		log.info("date format: " + registrationDate);


	}

}
