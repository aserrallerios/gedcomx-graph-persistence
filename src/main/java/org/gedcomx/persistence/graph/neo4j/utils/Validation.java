package org.gedcomx.persistence.graph.neo4j.utils;

import java.util.Collection;

public class Validation {

	private static boolean nullOrEmpty(final Collection<?> col) {
		for (final Object ob : col) {
			if (Validation.nullOrEmpty(ob)) {
				return true;
			}
		}
		return false;
	}

	public static boolean nullOrEmpty(final Object object) {
		if (object == null) {
			return true;
		}
		if (object instanceof Collection) {
			return Validation.nullOrEmpty((Collection<?>) object);
		}
		if (object instanceof String) {
			return Validation.nullOrEmpty((String) object);
		}
		return false;
	}

	private static boolean nullOrEmpty(final String string) {
		return false;
	}

}
