package com.github.wolfie.kuramud;

import java.util.Collection;

public class Util {
	public static String join(final Collection<? extends Object> parts,
			final String joiner) {
		
		if (parts == null) {
			throw new IllegalArgumentException("parts may not be null");
		}
		
		if (joiner == null) {
			throw new IllegalArgumentException("joiner may not be null");
		}
		
		if (parts.size() == 0) {
			return "";
		} else if (parts.size() == 1) {
			return parts.iterator().next().toString();
		} else {
			boolean firstPartInserted = false;
			String string = "";
			for (final Object object : parts) {
				if (firstPartInserted) {
					string += joiner;
				}
				string += object.toString();
				firstPartInserted = true;
			}
			return string;
		}
	}
}
