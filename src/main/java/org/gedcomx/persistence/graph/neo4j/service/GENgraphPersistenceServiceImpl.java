package org.gedcomx.persistence.graph.neo4j.service;

import java.util.Map;

import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAOUtil;
import org.gedcomx.persistence.graph.neo4j.exception.GenericError;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.model.Agent;
import org.gedcomx.persistence.graph.neo4j.model.Conclusion;
import org.gedcomx.persistence.graph.neo4j.model.Document;
import org.gedcomx.persistence.graph.neo4j.model.Event;
import org.gedcomx.persistence.graph.neo4j.model.NodeWrapper;
import org.gedcomx.persistence.graph.neo4j.model.Person;
import org.gedcomx.persistence.graph.neo4j.model.PlaceDescription;
import org.gedcomx.persistence.graph.neo4j.model.Relationship;
import org.gedcomx.persistence.graph.neo4j.model.SourceDescription;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeProperties;
import org.neo4j.graphdb.Node;

public class GENgraphPersistenceServiceImpl implements GENgraphPersistenceService {

	GENgraphPersistenceServiceImpl() {
	}

	@Override
	public Agent addAgent(final org.gedcomx.agent.Agent agent) throws MissingFieldException {
		Agent a = new Agent(agent);
		a.resolveReferences();
		return a;
	}

	@Override
	public Conclusion addConclusion(final org.gedcomx.conclusion.Conclusion conclusion) throws MissingFieldException {
		Conclusion c = null;
		if (conclusion instanceof org.gedcomx.conclusion.Person) {
			c = new Person((org.gedcomx.conclusion.Person) conclusion);
		} else if (conclusion instanceof org.gedcomx.conclusion.Document) {
			c = new Document((org.gedcomx.conclusion.Document) conclusion);
		} else if (conclusion instanceof org.gedcomx.conclusion.Event) {
			c = new Event((org.gedcomx.conclusion.Event) conclusion);
		} else if (conclusion instanceof org.gedcomx.conclusion.Relationship) {
			c = new Relationship((org.gedcomx.conclusion.Relationship) conclusion);
		} else if (conclusion instanceof org.gedcomx.conclusion.PlaceDescription) {
			c = new PlaceDescription((org.gedcomx.conclusion.PlaceDescription) conclusion);
		} else {
			throw new GenericError("Unknown GedcomX Conclusion type");
		}
		c.resolveReferences();
		return c;
	}

	@Override
	public SourceDescription addSource(final org.gedcomx.source.SourceDescription sourceDescription) throws MissingFieldException {
		SourceDescription s = new SourceDescription(sourceDescription);
		s.resolveReferences();
		return s;
	}

	@Override
	public NodeWrapper addTopLevelElement(final Object gedcomxElement) throws MissingFieldException {
		NodeWrapper n = null;
		if (gedcomxElement instanceof org.gedcomx.agent.Agent) {
			n = this.addAgent((org.gedcomx.agent.Agent) gedcomxElement);
		} else if (gedcomxElement instanceof org.gedcomx.conclusion.Conclusion) {
			n = this.addConclusion((org.gedcomx.conclusion.Conclusion) gedcomxElement);
		} else if (gedcomxElement instanceof org.gedcomx.source.SourceDescription) {
			n = this.addSource((org.gedcomx.source.SourceDescription) gedcomxElement);
		} else {
			throw new GenericError("Unknown GedcomX Top level type");
		}
		return n;
	}

	@Override
	public void createGraph(final Map<String, String> metadata, final Object[] gedcomxElements) throws MissingFieldException {
		final Node rootNode = this.getInitialGraphNode();

		GENgraphDAOUtil.setNodeProperties(rootNode, metadata);

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
		return GENgraphDAOUtil.getReferenceNode();
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
