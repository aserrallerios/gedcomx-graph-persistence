package org.gedcomx.persistence.graph.neo4j.dao;

import java.io.IOException;
import java.util.Properties;

import org.gedcomx.persistence.graph.neo4j.exception.GenericError;
import org.gedcomx.persistence.graph.neo4j.messages.ErrorMessages;

public class ConfigurationProvider {

	private static Properties prop = new Properties();

	static {
		try {
			ConfigurationProvider.prop.load(ConfigurationProvider.class.getClassLoader().getResourceAsStream("config/dao.properties"));
		} catch (final IOException e) {
			e.printStackTrace();
			throw new GenericError(ErrorMessages.DAO_CANT_LOAD_PROPERTIES);
		}
	}

	public static String getDBPath() {
		return ConfigurationProvider.prop.getProperty("neo4j.db.path");
	}

	public static String getPropertiesFile() {
		final String fileName = ConfigurationProvider.prop.getProperty("neo4j.properties.file");
		return ConfigurationProvider.class.getClassLoader().getResource(fileName).getPath();
	}
}
