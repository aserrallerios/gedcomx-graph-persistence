package org.gedcomx.graph.persistence.neo4j.embeded.service.impl;

import java.util.Map;

import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.dao.GENgraphDAO;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion.Conclusion;
import org.gedcomx.graph.persistence.neo4j.embeded.model.contributor.Agent;
import org.gedcomx.graph.persistence.neo4j.embeded.model.source.SourceDescription;
import org.gedcomx.graph.persistence.neo4j.embeded.service.GENgraphPersistenceService;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;
import org.neo4j.graphdb.Node;

public class GENgraphPersistenceServiceImpl implements GENgraphPersistenceService {

	private final GENgraphDAO dao;

	public GENgraphPersistenceServiceImpl(final GENgraphDAO dao) {
		this.dao = dao;
	}

	public void addAgent(final org.gedcomx.agent.Agent agent) {
		new Agent(this.dao, agent);
	}

	public void addConclusion(final org.gedcomx.conclusion.Conclusion conclusion) {
		if ((conclusion instanceof org.gedcomx.conclusion.Person) || (conclusion instanceof org.gedcomx.conclusion.Document)
				|| (conclusion instanceof org.gedcomx.conclusion.Event) || (conclusion instanceof org.gedcomx.conclusion.Relationship)
				|| (conclusion instanceof org.gedcomx.conclusion.PlaceDescription)) {
			new Conclusion(this.dao, conclusion);
		}
	}

	public void addSource(final org.gedcomx.source.SourceDescription sourceDescription) {
		new SourceDescription(this.dao, sourceDescription);
	}

	public void addSource(final String id, final URI about, final String citationValue) {

	}

	public void addTopLevelElement(final Object gedcomxElement) {
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

	public void createGedcomXGraph(final Map<String, String> metadata, final Object[] gedcomxElements) throws MissingFieldException {
		final Node rootNode = this.getInitialGraphNode();

		this.dao.setNodeProperties(rootNode, metadata);

		for (final Object gedcomxElement : gedcomxElements) {
			this.addTopLevelElement(gedcomxElement);
		}
		this.resolveReferences();
	}

	public Object getGedcomXFromGraph() {
		return null;
	}

	public Node[] getGraph() {
		return null;
	}

	private Node getInitialGraphNode() {
		return this.dao.getReferenceNode();
	}

	public GENgraphNode getNodeByGedcomXId(final String id) {
		return null;
	}

	public GENgraphNode getNodeById(final Long id) {
		return null;
	}

	public GENgraphNode[] getNodesByFilters(final Map<NodeProperties, Object> filters) {
		return null;
	}

	public GENgraphNode[] getNodesByType(final NodeTypes type) {
		return null;
	}

	private void resolveReferences() {

	}

	public Node[] searchAlivePeopleWithoutChildren() {
		return this.searchNodesByTraversal(new Object());
	}

	public Node[] searchNodesByCypher(final String query) {
		return null;
	}

	private Node[] searchNodesByTraversal(final Object traversal) {
		return null;
	}
}
