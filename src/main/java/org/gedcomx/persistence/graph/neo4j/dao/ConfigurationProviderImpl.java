package org.gedcomx.persistence.graph.neo4j.dao;

import java.io.IOException;
import java.util.Properties;

import org.gedcomx.persistence.graph.neo4j.exception.GenericError;
import org.gedcomx.persistence.graph.neo4j.messages.ErrorMessages;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class ConfigurationProviderImpl implements ConfigurationProvider {

	private static final String NEO4J_PROPERTIES_FILE = "neo4j.properties.file";
	private static final String NEO4J_DB_PATH = "neo4j.db.path";

	private final Properties prop = new Properties();

	@Inject
	ConfigurationProviderImpl(final @Named("PropertiesDaoFile") String propertiesDaoFile) {
		try {
			this.prop.load(ConfigurationProviderImpl.class.getClassLoader().getResourceAsStream(propertiesDaoFile));
		} catch (final IOException e) {
			e.printStackTrace();
			throw new GenericError(ErrorMessages.DAO_CANT_LOAD_PROPERTIES);
		}
	}

	@Override
	public String getDBPath() {
		return this.prop.getProperty(ConfigurationProviderImpl.NEO4J_DB_PATH);
	}

	@Override
	public String getPropertiesFile() {
		final String fileName = this.prop.getProperty(ConfigurationProviderImpl.NEO4J_PROPERTIES_FILE);
		return ConfigurationProviderImpl.class.getClassLoader().getResource(fileName).getPath();
	}
}
