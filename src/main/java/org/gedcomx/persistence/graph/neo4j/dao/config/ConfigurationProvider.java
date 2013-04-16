package org.gedcomx.persistence.graph.neo4j.dao.config;

public interface ConfigurationProvider {

	String getDBPath();

	String getPropertiesFile();
}
