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
import org.gedcomx.persistence.graph.neo4j.exception.UninitializedDataBase;
import org.gedcomx.persistence.graph.neo4j.exception.UninitializedNode;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.utils.RelTypes;
import org.gedcomx.persistence.graph.neo4j.utils.RelationshipProperties;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;

public abstract class GENgraphNode {

	private final GENgraphDAO dao;

	private final Node underlyingNode;

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
		if (relType.isOrdered()) {
			this.getDao().beginTransaction();
			try {
				final int index = this.getMaxRelationshipIndex(relType);
				final Map<RelationshipProperties, Integer> properties = new HashMap<>();
				properties.put(RelationshipProperties.INDEX, Integer.valueOf(index + 1));
				this.getDao().createRelationship(this.getUnderlyingNode(), relType, node.underlyingNode);
				this.getDao().commitTransaction();
			} finally {
				this.getDao().endTransaction();
			}
		} else {
			this.getDao().createRelationship(this.getUnderlyingNode(), relType, node.underlyingNode);
		}
	}

	protected <T extends GENgraphNode> T createNode(final Class<T> type, final Node node) {
		Constructor<T> constructor;
		T wrapper = null;
		try {
			constructor = type.getConstructor(GENgraphDAO.class, Node.class);
			wrapper = constructor.newInstance(this.dao, node);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
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
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wrapper;
	}

	protected void createRelationship(final RelTypes relType, final GENgraphNode node) {
		final boolean rel = this.getDao().hasSingleRelationship(this.getUnderlyingNode(), relType, Direction.OUTGOING);

		if (!rel) {
			this.addRelationship(relType, node);
		} else {
			GENgraphNode wrapper = this.getNodeByRelationship(node.getClass(), relType, Direction.OUTGOING);
			wrapper.delete();
			this.addRelationship(relType, node);
		}
	}

	abstract public void delete();

	protected <T extends GENgraphNode> void deleteReferences(final Class<T> type, final RelTypes relation) {
		for (final T wrapper : this.getNodesByRelationship(type, relation, Direction.OUTGOING)) {
			wrapper.delete();
		}
	}

	protected void deleteSelf() {
		this.getDao().delete(this.getUnderlyingNode());
	}

	@Override
	public boolean equals(final Object object) {
		return this.getUnderlyingNode().equals(object);
	}

	private GENgraphDAO getDao() {
		if (this.dao == null) {
			throw new UninitializedDataBase();
		}
		return this.dao;
	}

	protected abstract <T> T getGedcomX();

	protected <T> List<T> getGedcomXList(Class<T> type, List<? extends GENgraphNode> nodes) {
		List<T> list = new ArrayList<>();
		for (GENgraphNode a : nodes) {
			list.add(type.cast(a.getGedcomX()));
		}
		return list;
	}

	private int getMaxRelationshipIndex(final RelTypes relType) {
		// TODO Auto-generated method stub
		// Traversal
		return 0;
	}

	protected <T extends GENgraphNode> T getNodeByRelationship(final Class<T> type, final RelTypes relation) {
		return this.getNodeByRelationship(type, relation, Direction.OUTGOING);
	}

	protected <T extends GENgraphNode> T getNodeByRelationship(final Class<T> type, final RelTypes relation, final Direction dir) {
		final Node node = this.getDao().getSingleNodeByRelationship(this.getUnderlyingNode(), relation, dir);
		return this.createNode(type, node);
	}

	protected <T extends GENgraphNode> List<T> getNodesByRelationship(final Class<T> type, final RelTypes relation) {
		return this.getNodesByRelationship(type, relation, Direction.OUTGOING);
	}

	protected <T extends GENgraphNode> List<T> getNodesByRelationship(final Class<T> type, final RelTypes relation, final Direction dir) {
		final Iterable<Node> nodes = this.getDao().getNodesByRelationship(this.getUnderlyingNode(), relation, dir);

		final List<T> wrappers = new ArrayList<>();
		for (final Node node : nodes) {
			wrappers.add(this.createNode(type, node));
		}
		return wrappers;
	}

	public NodeTypes getNodeType() {
		return NodeTypes.valueOf((String) this.getDao().getNodeProperty(this.getUnderlyingNode(), NodeProperties.Generic.NODE_TYPE));
	}

	protected Object getProperty(final NodeProperties property) {
		return this.getDao().getNodeProperty(this.getUnderlyingNode(), property);
	}

	private Node getUnderlyingNode() {
		if (this.underlyingNode == null) {
			throw new UninitializedNode();
		}
		return this.underlyingNode;
	}

	protected List<ResourceReference> getURIListProperties(final NodeProperties property) {
		final String[] values = (String[]) this.getDao().getNodeProperty(this.getUnderlyingNode(), property);
		final List<ResourceReference> resourceList = new ArrayList<>();
		for (final String uri : values) {
			resourceList.add(new ResourceReference(new URI(uri)));
		}
		return resourceList;
	}

	@Override
	public int hashCode() {
		return this.getUnderlyingNode().hashCode();
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
		this.getDao().setNodeProperty(this.getUnderlyingNode(), property, value);
		if (property.isIndexed()) {
			this.getDao().removeNodeFromIndex(property.getIndexName(), this.getUnderlyingNode(), property);
			this.getDao().setNodeToIndex(property.getIndexName(), this.getUnderlyingNode(), property, value);
		}
	}

	protected void setURIListProperties(final NodeProperties property, final List<ResourceReference> resourceList) {
		final String[] values = new String[resourceList.size()];
		int i = 0;
		for (final ResourceReference resource : resourceList) {
			values[i++] = resource.getResource().toString();
		}
		this.getDao().setNodeProperty(this.getUnderlyingNode(), property, values);
	}

	@Override
	public String toString() {
		return this.getUnderlyingNode().toString();
	}

	protected void validateGedcomXObject(final Object gedcomXObject) throws MissingFieldException {
		return;
	}
}
