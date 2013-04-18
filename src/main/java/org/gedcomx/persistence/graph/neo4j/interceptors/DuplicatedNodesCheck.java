package org.gedcomx.persistence.graph.neo4j.interceptors;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.gedcomx.persistence.graph.neo4j.annotations.EmbededDB;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAO;
import org.gedcomx.persistence.graph.neo4j.exception.NodeIdentifierAlreadyExists;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.IndexNames;
import org.neo4j.graphdb.Node;

import com.google.inject.Inject;

public class DuplicatedNodesCheck implements MethodInterceptor {

    GENgraphDAO dao;

    @Inject
    public DuplicatedNodesCheck() {
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final Object gedcomxElement = invocation.getArguments()[0];

        String id = null;
        if (gedcomxElement instanceof org.gedcomx.agent.Agent) {
            id = ((org.gedcomx.agent.Agent) gedcomxElement).getId();
        } else if (gedcomxElement instanceof org.gedcomx.conclusion.Conclusion) {
            id = ((org.gedcomx.conclusion.Conclusion) gedcomxElement).getId();
        } else if (gedcomxElement instanceof org.gedcomx.source.SourceDescription) {
            id = ((org.gedcomx.source.SourceDescription) gedcomxElement)
                    .getId();
        }

        if (id != null) {
            final Node node = this.dao.getSingleNodeFromIndex(
                    IndexNames.IDS.name(), GenericProperties.ID.name(), id);
            if (node != null) {
                throw new NodeIdentifierAlreadyExists(id);
            }
        }
        return invocation.proceed();
    }

    @Inject
    public void setDao(final @EmbededDB GENgraphDAO dao) {
        this.dao = dao;
    }
}
