package org.gedcomx.persistence.graph.neo4j.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAO;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.utils.RelTypes;
import org.gedcomx.persistence.graph.neo4j.utils.RelationshipProperties;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public abstract class GENgraphNode {

	private final Node underlyingNode;

	private final GENgraphDAO dao;

	protected GENgraphNode(final GENgraphDAO dao, final Node underlyingNode) throws MissingFieldException {
		this.dao = dao;
		this.underlyingNode = underlyingNode;
	}

	protected GENgraphNode(final GENgraphDAO dao, final NodeTypes nodeType, final Object gedcomXObject) throws MissingFieldException {
		this.dao = dao;
		this.validateGedcomXObject(gedcomXObject);
		this.underlyingNode = this.dao.createNode();
		this.setNodeType(nodeType);
		this.setGedcomXProperties(gedcomXObject);
		this.setGedcomXRelations(gedcomXObject);
	}

	protected void addRelationship(final RelTypes relType, final GENgraphNode node) {
		this.addRelationship(relType, node, Direction.OUTGOING);
	}

	protected void addRelationship(final RelTypes relType, final GENgraphNode node, final Direction dir) {
		if (relType.isOrdered()) {
			this.dao.beginTransaction();
			try {
				final int index = this.getMaxRelationshipIndex(relType);
				final Map<RelationshipProperties, Integer> properties = new HashMap<>();
				properties.put(RelationshipProperties.INDEX, Integer.valueOf(index + 1));
				this.dao.createRelationship(this.underlyingNode, relType, node.underlyingNode, dir);
			} catch (final Exception e) {
				this.dao.rollbackTransaction();
			} finally {
				this.dao.endTransaction();
			}

		} else {
			this.dao.createRelationship(this.underlyingNode, relType, node.underlyingNode());
		}
	}

	protected <T extends GENgraphNode> T createNode(final Class<T> type, final Node node) {

		Constructor<T> constructor;
		T wrapper = null;
		try {
			constructor = type.getConstructor(GENgraphDAO.class, Node.class);
			wrapper = constructor.newInstance(this.dao, node);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wrapper;
	}

	protected <T extends GENgraphNode> T createNode(final Class<T> type, final Object gedcomXObject) {

		Constructor<T> constructor;
		T wrapper = null;
		try {
			constructor = type.getConstructor(GENgraphDAO.class, Object.class);
			wrapper = constructor.newInstance(this.dao, gedcomXObject);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wrapper;
	}

	protected void createRelationship(final RelTypes relType, final GENgraphNode node, final Direction dir) {
		final Relationship rel = this.underlyingNode.getSingleRelationship(relType, dir);

		if (rel == null) {
			this.addRelationship(relType, node, dir);
		} else {
			this.deleteSubgraph(relType, rel.getEndNode().getId());
			this.addRelationship(relType, node, dir);
		}
	}

	abstract public void delete();

	protected <T extends GENgraphNode> void deleteReferences(final Class<T> type, final RelTypes relation) {
		for (final T wrapper : this.getNodesByRelationship(type, relation)) {
			wrapper.delete();
		}
	}

	protected void deleteSelf() {
		this.underlyingNode.delete();
	}

	@Override
	public boolean equals(final Object object) {
		return this.underlyingNode.equals(object);
	}

	public abstract Object getGedcomX();

	private int getMaxRelationshipIndex(final RelTypes relType) {
		// TODO Auto-generated method stub
		return 0;
	}

	protected <T extends GENgraphNode> T getNodeByRelationship(final Class<T> type, final RelTypes relation, final Direction dir)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		final Relationship rel = this.underlyingNode.getSingleRelationship(relation, dir);

		return this.createNode(type, rel.getEndNode());
	}

	protected <T extends GENgraphNode> List<T> getNodesByRelationship(final Class<T> type, final RelTypes relation) {
		final Iterable<Relationship> relationships = this.underlyingNode.getRelationships(relation);

		final List<T> wrappers = new ArrayList<>();
		for (final Relationship rel : relationships) {
			wrappers.add(this.createNode(type, rel.getEndNode()));
		}

		return wrappers;
	}

	public NodeTypes getNodeType() {
		return NodeTypes.valueOf((String) this.dao.getNodeProperty(this.underlyingNode, NodeProperties.Generic.NODE_TYPE));
	}

	protected Object getProperty(final NodeProperties property) {
		return this.dao.getNodeProperty(this.underlyingNode, property);
	}

	protected List<ResourceReference> getURIListProperties(final NodeProperties property) {
		final String[] values = (String[]) this.dao.getNodeProperty(this.underlyingNode, property);
		final List<ResourceReference> resourceList = new ArrayList<>();
		for (final String uri : values) {
			resourceList.add(new ResourceReference(new URI(uri)));
		}
		return resourceList;
	}

	@Override
	public int hashCode() {
		return this.underlyingNode.hashCode();
	}

	protected void resolveReferences() {
		return;
	}

	protected abstract void setGedcomXProperties(final Object gedcomXObject);

	protected void setGedcomXRelations(final Object gedcomXObject) {
		return;
	}

	private void setNodeType(final NodeTypes nodeType) {
		this.setProperty(NodeProperties.Generic.NODE_TYPE, nodeType.name());
	}

	protected void setProperty(final NodeProperties property, final Object value) {
		this.dao.setNodeProperty(this.underlyingNode, property, value);
		if (property.isIndexed()) {
			this.dao.removeNodeFromIndex(property.getIndexName(), this.underlyingNode, property);
			this.dao.setNodeToIndex(property.getIndexName(), this.underlyingNode, property, value);
		}
	}

	protected void setURIListProperties(final NodeProperties property, final List<ResourceReference> resourceList) {
		final String[] values = new String[resourceList.size()];
		int i = 0;
		for (final ResourceReference resource : resourceList) {
			values[i++] = resource.getResource().toString();
		}
		this.dao.setNodeProperty(this.underlyingNode, property, values);
	}

	@Override
	public String toString() {
		return this.underlyingNode.toString();
	}

	protected void validateGedcomXObject(final Object gedcomXObject) throws MissingFieldException {
		return;
	}
}
