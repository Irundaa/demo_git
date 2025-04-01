package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


class DemoApplicationTests {
	Calculator calculator = new Calculator();

	@Test
	void addNumbers() {
		// given
		int numberOne = 20;
		int numberTwo = 30;

		// when
		int result = calculator.add(numberOne, numberTwo);

		//then
//		assertThat(result).isEqualTo(50);
		assertEquals(50, result);
	}

	class Calculator {
	int add(int a, int b) {
		return a + b;
		}
	}

}
