package org.gedcomx.persistence.graph.neo4j.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAO;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAOUtil;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.UninitializedNode;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.RelationshipProperties;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

public abstract class NodeWrapper {

	private final Node underlyingNode;

	protected NodeWrapper(final Node underlyingNode) throws MissingFieldException, WrongNodeType {
		final Transaction t = GENgraphDAOUtil.beginTransaction();
		try {
			this.underlyingNode = underlyingNode;
			if (this.getNodeType() != this.getClass().getAnnotation(NodeType.class).value()) {
				throw new WrongNodeType();
			}
			this.validateUnderlyingNode();
			GENgraphDAOUtil.commitTransaction(t);
		} finally {
			GENgraphDAOUtil.endTransaction(t);
		}
	}

	protected NodeWrapper(final Object... properties) throws MissingFieldException {
		final Transaction t = GENgraphDAOUtil.beginTransaction();
		try {
			this.underlyingNode = GENgraphDAOUtil.createNode();
			this.setNodeType(this.getClass().getAnnotation(NodeType.class).value());
			if ((properties != null) && (properties.length > 0)) {
				this.setRequiredProperties(properties);
			}
			this.validateUnderlyingNode();
			GENgraphDAOUtil.commitTransaction(t);
		} finally {
			GENgraphDAOUtil.endTransaction(t);
		}
	}

	protected NodeWrapper(final Object gedcomXObject) throws MissingFieldException {
		final Transaction t = GENgraphDAOUtil.beginTransaction();
		try {
			this.underlyingNode = GENgraphDAOUtil.createNode();
			this.setNodeType(this.getClass().getAnnotation(NodeType.class).value());
			this.setGedcomXProperties(gedcomXObject);
			this.setGedcomXRelations(gedcomXObject);
			this.validateUnderlyingNode();
			GENgraphDAOUtil.commitTransaction(t);
		} finally {
			GENgraphDAOUtil.endTransaction(t);
		}
	}

	protected void addRelationship(final GENgraphRelTypes relType, final NodeWrapper node) {
		if (relType.isOrdered()) {
			final Transaction t = GENgraphDAOUtil.beginTransaction();
			try {
				final int index = this.getMaxRelationshipIndex(relType);
				final Map<RelationshipProperties, Integer> properties = new HashMap<>();
				properties.put(RelationshipProperties.INDEX, Integer.valueOf(index + 1));
				GENgraphDAOUtil.createRelationship(this.getUnderlyingNode(), relType, node.underlyingNode);
				GENgraphDAOUtil.commitTransaction(t);
			} finally {
				GENgraphDAOUtil.endTransaction(t);
			}
		} else {
			GENgraphDAOUtil.createRelationship(this.getUnderlyingNode(), relType, node.underlyingNode);
		}
	}

	private <T extends NodeWrapper> T createNode(final Class<T> type, final Node node) {
		Constructor<T> constructor;
		T wrapper = null;
		try {
			constructor = type.getConstructor(GENgraphDAO.class, Node.class);
			wrapper = constructor.newInstance(node);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wrapper;
	}

	protected void createRelationship(final GENgraphRelTypes relType, final NodeWrapper node) {
		final boolean rel = GENgraphDAOUtil.hasSingleRelationship(this.getUnderlyingNode(), relType, Direction.OUTGOING);

		if (!rel) {
			this.addRelationship(relType, node);
		} else {
			final NodeWrapper wrapper = this.getNodeByRelationship(node.getClass(), relType, Direction.OUTGOING);
			wrapper.delete();
			this.addRelationship(relType, node);
		}
	}

	public void delete() {
		final Transaction t = GENgraphDAOUtil.beginTransaction();
		try {
			this.deleteAllReferences();
			this.deleteIncomingRelationships();
			this.deleteSelf();
			GENgraphDAOUtil.commitTransaction(t);
		} finally {
			GENgraphDAOUtil.endTransaction(t);
		}

	}

	protected abstract void deleteAllReferences();

	private void deleteIncomingRelationships() {
		final Iterable<Relationship> rels = GENgraphDAOUtil.getRelationships(this.getUnderlyingNode(), Direction.INCOMING);

		for (final Relationship rel : rels) {
			GENgraphDAOUtil.delete(rel);
		}
	}

	protected void deleteReference(final GENgraphRelTypes rel) {
		GENgraphDAOUtil.delete(GENgraphDAOUtil.getSingleRelationship(this.getUnderlyingNode(), rel, Direction.OUTGOING));
	}

	protected <T extends NodeWrapper> void deleteReferencedNode(final T wrapper) {
		if (wrapper != null) {
			wrapper.delete();
		}
	}

	protected <T extends NodeWrapper> void deleteReferencedNodes(final List<T> type) {
		for (final T wrapper : type) {
			wrapper.delete();
		}
	}

	protected void deleteReferences(final GENgraphRelTypes rel) {
		for (final Relationship relationship : GENgraphDAOUtil.getRelationships(this.getUnderlyingNode(), rel, Direction.OUTGOING)) {
			GENgraphDAOUtil.delete(relationship);
		}
	}

	private void deleteSelf() {
		GENgraphDAOUtil.delete(this.getUnderlyingNode());
	}

	@Override
	public boolean equals(final Object object) {
		return this.getUnderlyingNode().equals(object);
	}

	protected abstract <T extends Object> T getGedcomX();

	protected <T> List<T> getGedcomXList(final Class<T> type, final List<? extends NodeWrapper> nodes) {
		final List<T> list = new ArrayList<>();
		for (final NodeWrapper a : nodes) {
			list.add(type.cast(a.getGedcomX()));
		}
		return list;
	}

	private int getMaxRelationshipIndex(final GENgraphRelTypes relType) {
		final Iterable<Relationship> rels = GENgraphDAOUtil.getRelationships(this.getUnderlyingNode(), relType, Direction.OUTGOING);

		int max = 0;
		for (final Relationship rel : rels) {
			final Integer value = (Integer) GENgraphDAOUtil.getRelationshipProperty(rel, RelationshipProperties.INDEX.name());
			max = value > max ? value : max;
		}
		// TODO: traversal
		return max;
	}

	protected <T extends NodeWrapper> T getNodeByRelationship(final Class<T> type, final GENgraphRelTypes relation) {
		return this.getNodeByRelationship(type, relation, Direction.OUTGOING);
	}

	protected <T extends NodeWrapper> T getNodeByRelationship(final Class<T> type, final GENgraphRelTypes relation, final Direction dir) {
		final Node node = GENgraphDAOUtil.getSingleNodeByRelationship(this.getUnderlyingNode(), relation, dir);
		if (node != null) {
			return this.createNode(type, node);
		}
		return null;
	}

	private <T extends NodeWrapper> T getNodeByRelationship(final GENgraphRelTypes relation, final Direction dir) {
		// final Node node =
		// GENgraphDAOUtil.getSingleNodeByRelationship(this.getUnderlyingNode(),
		// relation, dir);
		// String nodeType = (String) GENgraphDAOUtil.getNodeProperty(node,
		// NodeProperties.Generic.NODE_TYPE.name());
		// TODO
		return null;
	}

	protected <T extends NodeWrapper> List<T> getNodesByRelationship(final Class<T> type, final GENgraphRelTypes relation) {
		return this.getNodesByRelationship(type, relation, Direction.OUTGOING);
	}

	private <T extends NodeWrapper> List<T> getNodesByRelationship(final Class<T> type, final GENgraphRelTypes relation, final Direction dir) {
		final Iterable<Node> nodes = GENgraphDAOUtil.getNodesByRelationship(this.getUnderlyingNode(), relation, dir,
				(dir == Direction.OUTGOING) && relation.isOrdered(), RelationshipProperties.INDEX.name());

		final List<T> wrappers = new ArrayList<>();
		for (final Node node : nodes) {
			wrappers.add(this.createNode(type, node));
		}
		return wrappers;
	}

	public String getNodeType() {
		return (String) GENgraphDAOUtil.getNodeProperty(this.getUnderlyingNode(), NodeProperties.Generic.NODE_TYPE.name());
	}

	protected NodeWrapper getParentNode(final GENgraphRelTypes relation) {
		return this.getNodeByRelationship(relation, Direction.INCOMING);
	}

	protected Object getProperty(final NodeProperties property) {
		return GENgraphDAOUtil.getNodeProperty(this.getUnderlyingNode(), property.name());
	}

	private Node getUnderlyingNode() {
		if (this.underlyingNode == null) {
			throw new UninitializedNode();
		}
		return this.underlyingNode;
	}

	protected List<ResourceReference> getURIListProperties(final NodeProperties property) {
		final String[] values = (String[]) GENgraphDAOUtil.getNodeProperty(this.getUnderlyingNode(), property.name());
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

	protected abstract void resolveReferences();

	protected abstract void setGedcomXProperties(final Object gedcomXObject);

	protected abstract void setGedcomXRelations(final Object gedcomXObject) throws MissingFieldException;

	private void setNodeType(final String nodeType) {
		this.setProperty(NodeProperties.Generic.NODE_TYPE, nodeType);
	}

	protected void setProperty(final NodeProperties property, final Object value) {
		if (value == null) {
			GENgraphDAOUtil.removeNodeProperty(this.getUnderlyingNode(), property.name());
		} else {
			GENgraphDAOUtil.setNodeProperty(this.getUnderlyingNode(), property.name(), value);
		}
		if (property.isIndexed()) {
			GENgraphDAOUtil.removeNodeFromIndex(property.getIndexName().name(), this.getUnderlyingNode(), property.name());
			GENgraphDAOUtil.setNodeToIndex(property.getIndexName().name(), this.getUnderlyingNode(), property.name(), value);
		}

	}

	protected abstract void setRequiredProperties(final Object... properties) throws MissingFieldException;

	protected void setURIListProperties(final NodeProperties property, final List<ResourceReference> resourceList) {
		final String[] values = new String[resourceList.size()];
		int i = 0;
		for (final ResourceReference resource : resourceList) {
			values[i++] = resource.getResource().toString();
		}
		GENgraphDAOUtil.setNodeProperty(this.getUnderlyingNode(), property.name(), values);
	}

	@Override
	public String toString() {
		return this.getUnderlyingNode().toString();
	}

	protected abstract void validateUnderlyingNode() throws MissingFieldException;
}
