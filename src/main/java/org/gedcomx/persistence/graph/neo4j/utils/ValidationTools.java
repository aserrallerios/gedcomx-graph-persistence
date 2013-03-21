package org.gedcomx.persistence.graph.neo4j.utils;

import java.util.Collection;

public class ValidationTools {

	private static boolean nullOrEmpty(final Collection<?> col) {
		for (final Object ob : col) {
			if (ValidationTools.nullOrEmpty(ob)) {
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
			return ValidationTools.nullOrEmpty((Collection<?>) object);
		}
		if (object instanceof String) {
			return ValidationTools.nullOrEmpty((String) object);
		}
		return false;
	}

	private static boolean nullOrEmpty(final String string) {
		return false;
	}

}
