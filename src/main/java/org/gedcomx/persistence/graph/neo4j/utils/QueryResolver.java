package org.gedcomx.persistence.graph.neo4j.utils;

import java.io.IOException;
import java.util.Properties;

import org.gedcomx.persistence.graph.neo4j.ServiceInjector;
import org.gedcomx.persistence.graph.neo4j.exceptions.GenericError;
import org.gedcomx.persistence.graph.neo4j.properties.Messages;

public class QueryResolver {

	private static Properties prop;
	private static final String QUERIES_RESOURCE = "queries/cypher.properties";

	static {
		QueryResolver.prop = new Properties();
		try {
			QueryResolver.prop.load(ServiceInjector.class.getClassLoader()
					.getResourceAsStream(QUERIES_RESOURCE));
		} catch (final IOException e) {
			throw new GenericError(Messages.DAO_CANT_LOAD_PROPERTIES);
		}
	}

	public static String resolve(final String property) {
		final String query = QueryResolver.prop.getProperty(property);
		return query == null ? property : query;
	}

}
