package de.hswt.fi.common;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PasswordGenerator {

	private final RandomStringGenerator randomStringGenerator;

	public PasswordGenerator() {
		randomStringGenerator = new RandomStringGenerator.Builder()
				.withinRange('0', 'z')
				.filteredBy(Character::isLetter, Character::isDigit)
				.build();
	}

	public String generatePassword(int length) {
		return randomStringGenerator.generate(length);
	}
}
