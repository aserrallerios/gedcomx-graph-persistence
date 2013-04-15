package org.gedcomx.persistence.graph.neo4j;

import org.gedcomx.persistence.graph.neo4j.dao.ConfigurationProvider;
import org.gedcomx.persistence.graph.neo4j.dao.ConfigurationProviderImpl;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAO;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAOImpl;
import org.gedcomx.persistence.graph.neo4j.service.GENgraphPersistenceService;
import org.gedcomx.persistence.graph.neo4j.service.GENgraphPersistenceServiceImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

public class GuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		// Singletons
		this.bind(GENgraphPersistenceServiceImpl.class).in(Singleton.class);
		this.bind(GENgraphDAOImpl.class).in(Singleton.class);
		this.bind(ConfigurationProviderImpl.class).in(Singleton.class);

		// Bindings
		this.bind(GENgraphPersistenceService.class).to(GENgraphPersistenceServiceImpl.class);
		this.bind(GENgraphDAO.class).to(GENgraphDAOImpl.class);
		this.bind(ConfigurationProvider.class).annotatedWith(Names.named("DefaultConfigurationProvider"))
				.to(ConfigurationProviderImpl.class);

		// Instance bindings
		this.bind(String.class).annotatedWith(Names.named("PropertiesDaoFile")).toInstance("config/dao.properties");
	}
}
