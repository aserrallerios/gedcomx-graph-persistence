package org.gedcomx.persistence.graph.neo4j;

import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAO;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAOImpl;
import org.gedcomx.persistence.graph.neo4j.dao.config.ConfigurationProvider;
import org.gedcomx.persistence.graph.neo4j.dao.config.ConfigurationProviderImpl;
import org.gedcomx.persistence.graph.neo4j.model.NodeTypeMapper;
import org.gedcomx.persistence.graph.neo4j.model.NodeWrapper;
import org.gedcomx.persistence.graph.neo4j.service.CheckForDuplicates;
import org.gedcomx.persistence.graph.neo4j.service.DuplicatedNodesInterceptor;
import org.gedcomx.persistence.graph.neo4j.service.GENgraphPersistenceService;
import org.gedcomx.persistence.graph.neo4j.service.GENgraphPersistenceServiceImpl;
import org.reflections.Reflections;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;

public class ServiceInjector extends AbstractModule {

    @Override
    protected void configure() {
        // Node wrappers hold a static reference to DAO
        this.requestStaticInjection(NodeWrapper.class);

        // Untargetted Binding
        this.bind(NodeTypeMapper.class).in(Singleton.class);

        // Bindings
        this.bind(GENgraphPersistenceService.class)
                .to(GENgraphPersistenceServiceImpl.class).in(Singleton.class);
        this.bind(GENgraphDAO.class).annotatedWith(Names.named("EmbededDB"))
                .to(GENgraphDAOImpl.class).in(Singleton.class);
        this.bind(ConfigurationProvider.class)
                .annotatedWith(Names.named("DefaultConfigurationProvider"))
                .to(ConfigurationProviderImpl.class).in(Singleton.class);

        // Instance bindings
        this.bind(String.class).annotatedWith(Names.named("PropertiesDaoFile"))
                .toInstance("config/dao.properties");

        // Interceptor
        final DuplicatedNodesInterceptor interceptor = new DuplicatedNodesInterceptor();
        this.requestInjection(interceptor);
        this.bindInterceptor(Matchers.any(),
                Matchers.annotatedWith(CheckForDuplicates.class), interceptor);
    }

    @Provides
    Reflections provideTransactionLog() {
        final Reflections reflections = new Reflections(NodeWrapper.class
                .getPackage().getName());
        return reflections;
    }
}
