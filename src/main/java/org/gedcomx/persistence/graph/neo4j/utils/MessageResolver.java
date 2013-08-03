package org.gedcomx.persistence.graph.neo4j.utils;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class MessageResolver {

	private static ResourceBundle resource;
	private static final String MESSAGES_LOCALE_PROPERTY = "messages.locale";
	private static final String MESSAGES_RESOURCE = "properties.messages";
	private static final String CONFIG_RESOURCE = "config/service.properties";

	static {
		Locale locale;

		final Properties prop = new Properties();
		try {
			prop.load(MessageResolver.class.getClassLoader()
					.getResourceAsStream(CONFIG_RESOURCE));
			locale = Locale.forLanguageTag(prop
					.getProperty(MessageResolver.MESSAGES_LOCALE_PROPERTY));
		} catch (final IOException e) {
			locale = Locale.ENGLISH;
		}
		MessageResolver.resource = ResourceBundle.getBundle(MESSAGES_RESOURCE,
				locale);
	}

	public static String resolve(final String property) {
		return MessageResolver.resource.getString(property);
	}

	public static String resolve(final String property,
			final Object... parameters) {
		return String.format(MessageResolver.resource.getString(property),
				parameters);
	}

}
