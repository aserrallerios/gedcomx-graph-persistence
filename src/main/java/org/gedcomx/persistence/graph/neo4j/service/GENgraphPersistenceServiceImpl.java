package org.gedcomx.persistence.graph.neo4j.service;

import java.util.Map;

import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAO;
import org.gedcomx.persistence.graph.neo4j.exception.GenericError;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.model.Agent;
import org.gedcomx.persistence.graph.neo4j.model.Conclusion;
import org.gedcomx.persistence.graph.neo4j.model.Document;
import org.gedcomx.persistence.graph.neo4j.model.Event;
import org.gedcomx.persistence.graph.neo4j.model.NodeWrapper;
import org.gedcomx.persistence.graph.neo4j.model.NodeWrapper.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.model.Person;
import org.gedcomx.persistence.graph.neo4j.model.PlaceDescription;
import org.gedcomx.persistence.graph.neo4j.model.Relationship;
import org.gedcomx.persistence.graph.neo4j.model.SourceDescription;
import org.neo4j.graphdb.Node;

public class GENgraphPersistenceServiceImpl implements GENgraphPersistenceService {

	private final GENgraphDAO dao;

	public GENgraphPersistenceServiceImpl(final GENgraphDAO dao) {
		this.dao = dao;
	}

	@Override
	public Agent addAgent(final org.gedcomx.agent.Agent agent) throws MissingFieldException {
		return new Agent(agent);
	}

	@Override
	public Conclusion addConclusion(final org.gedcomx.conclusion.Conclusion conclusion) throws MissingFieldException {
		if (conclusion instanceof org.gedcomx.conclusion.Person) {
			return new Person((org.gedcomx.conclusion.Person) conclusion);
		}
		if (conclusion instanceof org.gedcomx.conclusion.Document) {
			return new Document((org.gedcomx.conclusion.Document) conclusion);
		}
		if (conclusion instanceof org.gedcomx.conclusion.Event) {
			return new Event((org.gedcomx.conclusion.Event) conclusion);
		}
		if (conclusion instanceof org.gedcomx.conclusion.Relationship) {
			return new Relationship((org.gedcomx.conclusion.Relationship) conclusion);
		}
		if (conclusion instanceof org.gedcomx.conclusion.PlaceDescription) {
			return new PlaceDescription((org.gedcomx.conclusion.PlaceDescription) conclusion);
		}
		throw new GenericError("Unknown GedcomX Conclusion type");

	}

	@Override
	public SourceDescription addSource(final org.gedcomx.source.SourceDescription sourceDescription) throws MissingFieldException {
		return new SourceDescription(sourceDescription);
	}

	@Override
	public NodeWrapper addTopLevelElement(final Object gedcomxElement) throws MissingFieldException {
		if (gedcomxElement instanceof org.gedcomx.agent.Agent) {
			return this.addAgent((org.gedcomx.agent.Agent) gedcomxElement);
		}
		if (gedcomxElement instanceof org.gedcomx.conclusion.Conclusion) {
			return this.addConclusion((org.gedcomx.conclusion.Conclusion) gedcomxElement);
		}
		if (gedcomxElement instanceof org.gedcomx.source.SourceDescription) {
			return this.addSource((org.gedcomx.source.SourceDescription) gedcomxElement);
		}
		throw new GenericError("Unknown GedcomX Top level type");
	}

	@Override
	public void createGraph(final Map<String, String> metadata, final Object[] gedcomxElements) throws MissingFieldException {
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
	public NodeWrapper[] getNodesByType(final String type) {
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
