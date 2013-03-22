package org.gedcomx.persistence.graph.neo4j.service;

import java.util.Map;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAO;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.model.Agent;
import org.gedcomx.persistence.graph.neo4j.model.Conclusion;
import org.gedcomx.persistence.graph.neo4j.model.NodeWrapper;
import org.gedcomx.persistence.graph.neo4j.model.NodeWrapper.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.model.SourceDescription;
import org.neo4j.graphdb.Node;

public class GENgraphPersistenceServiceImpl implements GENgraphPersistenceService {

	private final GENgraphDAO dao;

	public GENgraphPersistenceServiceImpl(final GENgraphDAO dao) {
		this.dao = dao;
	}

	@Override
	public void addAgent(final org.gedcomx.agent.Agent agent) throws MissingFieldException {
		new Agent(agent);
	}

	@Override
	public void addConclusion(final org.gedcomx.conclusion.Conclusion conclusion) {
		if ((conclusion instanceof org.gedcomx.conclusion.Person) || (conclusion instanceof org.gedcomx.conclusion.Document)
				|| (conclusion instanceof org.gedcomx.conclusion.Event) || (conclusion instanceof org.gedcomx.conclusion.Relationship)
				|| (conclusion instanceof org.gedcomx.conclusion.PlaceDescription)) {
			new Conclusion(conclusion);
		}
	}

	@Override
	public void addSource(final org.gedcomx.source.SourceDescription sourceDescription) throws MissingFieldException {
		new SourceDescription(sourceDescription);
	}

	@Override
	public void addSource(final String id, final URI about, final String citationValue) {

	}

	@Override
	public void addTopLevelElement(final Object gedcomxElement) throws MissingFieldException {
		if (gedcomxElement instanceof org.gedcomx.agent.Agent) {
			this.addAgent((org.gedcomx.agent.Agent) gedcomxElement);
		}
		if (gedcomxElement instanceof org.gedcomx.conclusion.Conclusion) {
			this.addConclusion((org.gedcomx.conclusion.Conclusion) gedcomxElement);
		}
		if (gedcomxElement instanceof org.gedcomx.source.SourceDescription) {
			this.addSource((org.gedcomx.source.SourceDescription) gedcomxElement);
		}
	}

	@Override
	public void createGedcomXGraph(final Map<String, String> metadata, final Object[] gedcomxElements) throws MissingFieldException {
		final Node rootNode = this.getInitialGraphNode();

		this.dao.setNodeProperties(rootNode, metadata);

		for (final Object gedcomxElement : gedcomxElements) {
			this.addTopLevelElement(gedcomxElement);
		}
		this.resolveReferences();
	}

	@Override
	public Object getGedcomX() {
		return null;
	}

	@Override
	public Node[] getGraph() {
		return null;
	}

	private Node getInitialGraphNode() {
		return this.dao.getReferenceNode();
	}

	@Override
	public NodeWrapper getNodeByGedcomXId(final String id) {
		return null;
	}

	@Override
	public NodeWrapper getNodeById(final Long id) {
		return null;
	}

	@Override
	public NodeWrapper[] getNodesByFilters(final Map<NodeProperties, Object> filters) {
		return null;
	}

	@Override
	public NodeWrapper[] getNodesByType(final NodeTypes type) {
		return null;
	}

	private void resolveReferences() {

	}

	@Override
	public Node[] searchAlivePeopleWithoutChildren() {
		return this.searchNodesByTraversal(new Object());
	}

	@Override
	public Node[] searchAllPeopleAndRelationships() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node[] searchNodesByCypher(final String query) {
		return null;
	}

	private Node[] searchNodesByTraversal(final Object traversal) {
		return null;
	}
}