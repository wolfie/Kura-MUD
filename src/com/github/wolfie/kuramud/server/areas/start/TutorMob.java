package com.github.wolfie.kuramud.server.areas.start;

import com.github.wolfie.kuramud.server.NonPlayerCharacter;

public class TutorMob extends NonPlayerCharacter {
	public TutorMob() {
		super("A tutor");
	}
	
	@Override
	public String getLongDescription() {
		return "The tutor looks very knowledgeable and kind. He could probably beat your ass if he wanted to.";
	}
}
