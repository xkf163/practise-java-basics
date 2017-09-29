package com.xukangfeng;

import com.xukangfeng.hello.HelloSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MqApplicationTests {

	@Autowired
	private HelloSender helloSender;

	@Test
	public void helloTest() throws Exception{
		helloSender.sender();
	}

}
