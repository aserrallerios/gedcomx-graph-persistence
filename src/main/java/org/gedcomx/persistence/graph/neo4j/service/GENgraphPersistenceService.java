package org.gedcomx.persistence.graph.neo4j.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.gedcomx.persistence.graph.neo4j.dto.QueryResult;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.model.Agent;
import org.gedcomx.persistence.graph.neo4j.model.Conclusion;
import org.gedcomx.persistence.graph.neo4j.model.NodeWrapper;
import org.gedcomx.persistence.graph.neo4j.model.SourceDescription;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeProperties;

public interface GENgraphPersistenceService {

    Agent addAgent(org.gedcomx.agent.Agent agent) throws MissingFieldException;

    Conclusion addConclusion(org.gedcomx.conclusion.Conclusion conclusion)
            throws MissingFieldException;

    SourceDescription addSource(
            org.gedcomx.source.SourceDescription sourceDescription)
            throws MissingFieldException;

    NodeWrapper addTopLevelElement(final Object gedcomxElement)
            throws MissingFieldException;

    void createGraphByGedcomX(Map<String, String> metadata,
            Collection<Object> gedcomxElements);

    List<Object> getGedcomXFromGraph();

    QueryResult getGraph();

    NodeWrapper getNodeByGedcomXId(String id);

    NodeWrapper getNodeById(Long id);

    List<NodeWrapper> getNodesByFilters(Map<NodeProperties, Object> filters);

    List<NodeWrapper> getNodesByType(String type);

    QueryResult searchNodesByCypher(String query);

}
