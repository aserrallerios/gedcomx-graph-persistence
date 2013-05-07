package org.gedcomx.persistence.graph.neo4j;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;


public class MessageResolver {

    public static ResourceBundle resource;
    public static final String MESSAGES_LOCALE_PROPERTY = "messages.locale";

    static {
        Locale locale;

        final Properties prop = new Properties();
        try {
            prop.load(ServiceInjector.class.getClassLoader()
                    .getResourceAsStream("config/service.properties"));
            locale = Locale.forLanguageTag(prop
                    .getProperty(MessageResolver.MESSAGES_LOCALE_PROPERTY));
        } catch (final IOException e) {
            locale = Locale.ENGLISH;
        }
        MessageResolver.resource = ResourceBundle.getBundle(
                "properties.messages", locale);
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
