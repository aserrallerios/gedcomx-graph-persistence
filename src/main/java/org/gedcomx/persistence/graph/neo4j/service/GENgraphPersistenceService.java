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

	public Agent addAgent(org.gedcomx.agent.Agent agent) throws MissingFieldException;

	public Conclusion addConclusion(org.gedcomx.conclusion.Conclusion conclusion) throws MissingFieldException;

	public SourceDescription addSource(org.gedcomx.source.SourceDescription sourceDescription) throws MissingFieldException;

	public void createGraph(Map<String, String> metadata, Object[] gedcomxElements) throws MissingFieldException;

	public Object getGedcomX();

	public Node[] getGraph();

	public NodeWrapper getNodeByGedcomXId(String id);

	public NodeWrapper getNodeById(Long id);

	public NodeWrapper[] getNodesByFilters(Map<NodeProperties, Object> filters);

	public NodeWrapper[] getNodesByType(String type);

	public Node[] searchAlivePeopleWithoutChildren();

	public Node[] searchAllPeopleAndRelationships();

	public Node[] searchNodesByCypher(String query);

}
