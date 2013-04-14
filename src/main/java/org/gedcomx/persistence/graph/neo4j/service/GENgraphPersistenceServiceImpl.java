package org.gedcomx.persistence.graph.neo4j.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAOUtil;
import org.gedcomx.persistence.graph.neo4j.exception.GenericError;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.messages.ErrorMessages;
import org.gedcomx.persistence.graph.neo4j.messages.InfoMessages;
import org.gedcomx.persistence.graph.neo4j.model.Agent;
import org.gedcomx.persistence.graph.neo4j.model.Conclusion;
import org.gedcomx.persistence.graph.neo4j.model.Document;
import org.gedcomx.persistence.graph.neo4j.model.Event;
import org.gedcomx.persistence.graph.neo4j.model.NodeTypeMapper;
import org.gedcomx.persistence.graph.neo4j.model.NodeWrapper;
import org.gedcomx.persistence.graph.neo4j.model.Person;
import org.gedcomx.persistence.graph.neo4j.model.PlaceDescription;
import org.gedcomx.persistence.graph.neo4j.model.Relationship;
import org.gedcomx.persistence.graph.neo4j.model.SourceDescription;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.IndexNames;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class GENgraphPersistenceServiceImpl implements GENgraphPersistenceService {

	private static Logger logger = LogManager.getLogger("GENGraphService");

	GENgraphPersistenceServiceImpl() {
	}

	@Override
	public Agent addAgent(final org.gedcomx.agent.Agent agent) throws MissingFieldException {
		return new Agent(agent);
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
			throw new GenericError(ErrorMessages.GEDCOMX_CONCLUSION_TYPE);
		}
		return c;
	}

	@Override
	public SourceDescription addSource(final org.gedcomx.source.SourceDescription sourceDescription) throws MissingFieldException {
		return new SourceDescription(sourceDescription);
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
			throw new GenericError(ErrorMessages.GEDCOMX_UNKNOWN_TYPE);
		}
		GENgraphPersistenceServiceImpl.logger.info(InfoMessages.GEDCOMX_NODE_CREATED);
		return n;
	}

	@Override
	public void createGraphByGedcomX(final Map<String, String> metadata, final Collection<Object> gedcomxElements) {
		final Node rootNode = this.getInitialGraphNode();

		final Transaction t = GENgraphDAOUtil.beginTransaction();
		try {
			GENgraphDAOUtil.setNodeProperties(rootNode, metadata);

			final List<NodeWrapper> wrappers = new ArrayList<>();

			for (final Object gedcomxElement : gedcomxElements) {
				try {
					wrappers.add(this.addTopLevelElement(gedcomxElement));
				} catch (final MissingFieldException e) {
					GENgraphPersistenceServiceImpl.logger.warn(ErrorMessages.GEDCOMX_MISSING_FIELD);
				}
			}

			for (final NodeWrapper wrapper : wrappers) {
				wrapper.resolveReferences();
			}
			GENgraphDAOUtil.commitTransaction(t);
		} finally {
			GENgraphDAOUtil.endTransaction(t);
		}
	}

	private List<Object> getGedcomXByType(final String type) {
		final List<Object> gedcomXObjects = new ArrayList<>();

		final List<NodeWrapper> wrappers = this.getNodesByType(type);

		for (final NodeWrapper wrapper : wrappers) {
			gedcomXObjects.add(wrapper.getGedcomX());
		}
		return gedcomXObjects;
	}

	@Override
	public List<Object> getGedcomXFromGraph() {
		final List<Object> gedcomXObjects = new ArrayList<>();

		gedcomXObjects.addAll(this.getGedcomXByType(NodeTypes.AGENT.toString()));
		gedcomXObjects.addAll(this.getGedcomXByType(NodeTypes.SOURCE_DESCRIPTION.toString()));
		gedcomXObjects.addAll(this.getGedcomXByType(NodeTypes.PERSON.toString()));
		gedcomXObjects.addAll(this.getGedcomXByType(NodeTypes.RELATIONSHIP.toString()));
		gedcomXObjects.addAll(this.getGedcomXByType(NodeTypes.DOCUMENT.toString()));
		gedcomXObjects.addAll(this.getGedcomXByType(NodeTypes.PLACE_DESCRIPTION.toString()));
		gedcomXObjects.addAll(this.getGedcomXByType(NodeTypes.EVENT.toString()));

		return gedcomXObjects;
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
		final Node node = GENgraphDAOUtil.getSingleNodeFromIndex(IndexNames.IDS.toString(), GenericProperties.ID.toString(), id);

		return this.getWrapperByNode(node);

	}

	@Override
	public NodeWrapper getNodeById(final Long id) {
		return this.getWrapperByNode(GENgraphDAOUtil.getNode(id));
	}

	@Override
	public List<NodeWrapper> getNodesByFilters(final Map<NodeProperties, Object> filters) {
		return null;
	}

	@Override
	public List<NodeWrapper> getNodesByType(final String type) {
		final Iterator<Node> nodes = GENgraphDAOUtil.getNodesFromIndex(IndexNames.NODE_TYPES.toString(),
				GenericProperties.NODE_TYPE.toString(), type);

		final List<NodeWrapper> wrappers = new ArrayList<>();
		while (nodes.hasNext()) {
			wrappers.add(NodeTypeMapper.createNode(type, nodes.next()));
		}
		return wrappers;
	}

	private NodeWrapper getWrapperByNode(final Node node) {
		return this.getWrapperByNode(node, null);
	}

	private NodeWrapper getWrapperByNode(final Node node, String type) {
		if (type == null) {
			type = (String) GENgraphDAOUtil.getNodeProperty(node, GenericProperties.TYPE.toString());
		}
		return NodeTypeMapper.createNode(type, node);
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
		GENgraphDAOUtil.executeCypherQuery(query);
		// TODO
		return null;
	}

	private Node[] searchNodesByTraversal(final Object traversal) {
		return null;
	}
}
