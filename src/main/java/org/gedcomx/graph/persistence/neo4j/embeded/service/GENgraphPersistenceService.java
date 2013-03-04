package org.gedcomx.graph.persistence.neo4j.embeded.service;

import java.util.Map;

import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;
import org.neo4j.graphdb.Node;

public interface GENgraphPersistenceService {

	public void addAgent(org.gedcomx.agent.Agent agent);

	public void addConclusion(org.gedcomx.conclusion.Conclusion conclusion);

	public void addSource(org.gedcomx.source.SourceDescription sourceDescription);

	public void addSource(String id, URI about, String citationValue);

	public void addTopLevelElement(Object gedcomxElement);

	public void createGedcomXGraph(Map<String, String> metadata, Object[] gedcomxElements) throws MissingFieldException;

	public Object getGedcomXFromGraph();

	public Node[] getGraph();

	public GENgraphNode getNodeByGedcomXId(String id);

	public GENgraphNode getNodeById(Long id);

	public GENgraphNode[] getNodesByFilters(Map<NodeProperties, Object> filters);

	public GENgraphNode[] getNodesByType(NodeTypes type);

	public Node[] searchAlivePeopleWithoutChildren();

	public Node[] searchNodesByCypher(String query);

}
