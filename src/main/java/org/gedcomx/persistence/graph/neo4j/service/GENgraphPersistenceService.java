package org.gedcomx.persistence.graph.neo4j.service;

import java.util.Map;

import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.model.Agent;
import org.gedcomx.persistence.graph.neo4j.model.Conclusion;
import org.gedcomx.persistence.graph.neo4j.model.NodeWrapper;
import org.gedcomx.persistence.graph.neo4j.model.NodeWrapper.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.model.SourceDescription;
import org.neo4j.graphdb.Node;

public interface GENgraphPersistenceService {

	Agent addAgent(org.gedcomx.agent.Agent agent) throws MissingFieldException;

	Conclusion addConclusion(org.gedcomx.conclusion.Conclusion conclusion) throws MissingFieldException;

	SourceDescription addSource(org.gedcomx.source.SourceDescription sourceDescription) throws MissingFieldException;

	NodeWrapper addTopLevelElement(final Object gedcomxElement) throws MissingFieldException;

	void createGraph(Map<String, String> metadata, Object[] gedcomxElements) throws MissingFieldException;

	Object getGedcomX();

	Node[] getGraph();

	NodeWrapper getNodeByGedcomXId(String id);

	NodeWrapper getNodeById(Long id);

	NodeWrapper[] getNodesByFilters(Map<NodeProperties, Object> filters);

	NodeWrapper[] getNodesByType(String type);

	Node[] searchAlivePeopleWithoutChildren();

	Node[] searchAllPeopleAndRelationships();

	Node[] searchNodesByCypher(String query);

}
