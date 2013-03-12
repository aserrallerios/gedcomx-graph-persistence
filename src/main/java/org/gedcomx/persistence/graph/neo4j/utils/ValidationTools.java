package org.gedcomx.persistence.graph.neo4j.utils;

import java.util.Collection;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;

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
		if (object instanceof URI) {
			return ValidationTools.nullOrEmpty((URI) object);
		}
		if (object instanceof ResourceReference) {
			return ValidationTools.nullOrEmpty((ResourceReference) object);
		}
		return false;
	}

	private static boolean nullOrEmpty(final ResourceReference resource) {

		return false;
	}

	private static boolean nullOrEmpty(final String string) {
		return false;
	}

	private static boolean nullOrEmpty(final URI uri) {

		return false;
	}

}
