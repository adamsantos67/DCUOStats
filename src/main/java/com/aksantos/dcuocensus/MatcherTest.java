package com.aksantos.dcuocensus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherTest {

	public static void main(String[] args) {

		String text = "<DDPredicate_IsHero/><DDPredicate_OR>HasMovementMode<DDPredicate_HasTankRole/><DDPredicate_HasHealerRole/></DDPredicate_OR>";

		String patternString1 = "Is(Hero|Villain)";

		Pattern pattern = Pattern.compile(patternString1);
		Matcher matcher = pattern.matcher(text);

		while (matcher.find()) {
			System.out.println("found: " + matcher.group(1));
		}
	}
}
