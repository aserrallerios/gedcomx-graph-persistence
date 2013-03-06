package org.gedcomx.persistence.graph.neo4j.service;

import java.util.Map;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.model.GENgraphNode;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;
import org.neo4j.graphdb.Node;

public interface GENgraphPersistenceService {

	public void addAgent(org.gedcomx.agent.Agent agent) throws MissingFieldException;

	public void addConclusion(org.gedcomx.conclusion.Conclusion conclusion) throws MissingFieldException;

	public void addSource(org.gedcomx.source.SourceDescription sourceDescription) throws MissingFieldException;

	public void addSource(String id, URI about, String citationValue) throws MissingFieldException;

	public void addTopLevelElement(Object gedcomxElement) throws MissingFieldException;

	public void createGedcomXGraph(Map<String, String> metadata, Object[] gedcomxElements) throws MissingFieldException;

	public Object getGedcomX();

	public Node[] getGraph();

	public GENgraphNode getNodeByGedcomXId(String id);

	public GENgraphNode getNodeById(Long id);

	public GENgraphNode[] getNodesByFilters(Map<NodeProperties, Object> filters);

	public GENgraphNode[] getNodesByType(NodeTypes type);

	public Node[] searchAlivePeopleWithoutChildren();

	public Node[] searchAllPeopleAndRelationships();

	public Node[] searchNodesByCypher(String query);

}