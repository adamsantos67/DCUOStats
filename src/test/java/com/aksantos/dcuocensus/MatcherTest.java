package com.aksantos.dcuocensus;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class MatcherTest {

    @Test
	public void testMatcher() {

		String text = "<DDPredicate_IsHero/><DDPredicate_OR>HasMovementMode<DDPredicate_HasTankRole/><DDPredicate_HasHealerRole/></DDPredicate_OR>";

		String patternString1 = "Is(Hero|Villain)";

		Pattern pattern = Pattern.compile(patternString1);
		Matcher matcher = pattern.matcher(text);

		while (matcher.find()) {
		    assertEquals("Hero", matcher.group(1));
			System.out.println("found: " + matcher.group(1));
		}
	}
}
