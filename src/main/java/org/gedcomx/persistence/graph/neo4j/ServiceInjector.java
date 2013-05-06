package org.gedcomx.persistence.graph.neo4j;

import java.io.IOException;
import java.util.Properties;

import org.gedcomx.persistence.graph.neo4j.annotations.CheckForDuplicates;
import org.gedcomx.persistence.graph.neo4j.annotations.EmbededDB;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeWrapperReflections;
import org.gedcomx.persistence.graph.neo4j.annotations.Transactional;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAO;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAOImpl;
import org.gedcomx.persistence.graph.neo4j.exception.GenericError;
import org.gedcomx.persistence.graph.neo4j.interceptors.DuplicatedNodesCheck;
import org.gedcomx.persistence.graph.neo4j.interceptors.TransactionWrapper;
import org.gedcomx.persistence.graph.neo4j.messages.ErrorMessages;
import org.gedcomx.persistence.graph.neo4j.model.NodeWrapper;
import org.gedcomx.persistence.graph.neo4j.model.utils.NodeTypeMapper;
import org.gedcomx.persistence.graph.neo4j.service.GENgraphPersistenceService;
import org.gedcomx.persistence.graph.neo4j.service.GENgraphPersistenceServiceImpl;
import org.reflections.Reflections;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;

public class ServiceInjector extends AbstractModule {

    private static final String NEO4J_PROPERTIES_FILE = "neo4j.properties.file";
    private static final String NEO4J_DB_PATH = "neo4j.db.path";

    @Override
    protected void configure() {
        // Node wrappers hold a static reference to DAO
        this.requestStaticInjection(NodeWrapper.class);

        // Untargetted Binding
        this.bind(NodeTypeMapper.class).in(Singleton.class);

        // Bindings
        this.bind(GENgraphPersistenceService.class)
                .to(GENgraphPersistenceServiceImpl.class).in(Singleton.class);
        this.bind(GENgraphDAO.class).annotatedWith(EmbededDB.class)
                .to(GENgraphDAOImpl.class).in(Singleton.class);

        // Instance bindings
        final Properties prop = new Properties();
        try {
            prop.load(ServiceInjector.class.getClassLoader()
                    .getResourceAsStream("config/dao.properties"));
        } catch (final IOException e) {
            e.printStackTrace();
            throw new GenericError(ErrorMessages.DAO_CANT_LOAD_PROPERTIES);
        }
        this.bind(String.class).annotatedWith(Names.named("Neo4jDBPath"))
                .toInstance(prop.getProperty(ServiceInjector.NEO4J_DB_PATH));
        final String fileName = prop
                .getProperty(ServiceInjector.NEO4J_PROPERTIES_FILE);
        this.bind(String.class)
                .annotatedWith(Names.named("Neo4jPropFile"))
                .toInstance(
                        ServiceInjector.class.getClassLoader()
                                .getResource(fileName).getPath());

        // Interceptor
        final DuplicatedNodesCheck interceptor = new DuplicatedNodesCheck();
        this.requestInjection(interceptor);
        this.bindInterceptor(Matchers.any(),
                Matchers.annotatedWith(CheckForDuplicates.class), interceptor);

        final TransactionWrapper transactionInterceptor = new TransactionWrapper();
        this.requestInjection(transactionInterceptor);
        this.bindInterceptor(Matchers.any(),
                Matchers.annotatedWith(Transactional.class),
                transactionInterceptor);
    }

    @Provides
    @NodeWrapperReflections
    Reflections provideReflectionsInstance() {
        final Reflections reflections = new Reflections(NodeWrapper.class
                .getPackage().getName());
        return reflections;
    }
}
