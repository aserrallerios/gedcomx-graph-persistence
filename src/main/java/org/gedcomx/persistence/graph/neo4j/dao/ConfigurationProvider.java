package org.gedcomx.persistence.graph.neo4j.dao;

public interface ConfigurationProvider {

	String getDBPath();

	String getPropertiesFile();
}
