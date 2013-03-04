package org.gedcomx.graph.persistence.neo4j.embeded.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.dao.GENgraphDAO;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.RelTypes;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.RelationshipProperties;
import org.neo4j.graphdb.Node;

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
		if (relType.isOrdered()) {
			final int index = this.getMaxRelationshipIndex(relType);
			final Map<RelationshipProperties, Integer> properties = new HashMap<>();
			properties.put(RelationshipProperties.INDEX, Integer.valueOf(index));
			this.dao.createRelationship(this.underlyingNode, relType, node.getUnderlyingNode());
		} else {
			this.dao.createRelationship(this.underlyingNode, relType, node.getUnderlyingNode());
		}
	}

	protected <T extends GENgraphNode> T createNode(final Class<T> type, final Object gedcomXObject) {
		try {
			final Constructor<T> constructor = type.getConstructor(GENgraphDAO.class, Object.class);
			final T wrapper = constructor.newInstance(this.dao, gedcomXObject);
			return wrapper;
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
		}
		return null;
	}

	protected void createRelationship(final RelTypes relType, final GENgraphNode node) {
		if (!this.hasRelationship(relType)) {
			this.dao.createRelationship(this.underlyingNode, relType, node.getUnderlyingNode());
		} else {
			// TODO exception
		}
	}

	protected void deleteReference(final RelTypes relation) {
		// TODO
	}

	protected void deleteSubgraph(final RelTypes relation) {
		// TODO
	}

	@Override
	public boolean equals(final Object object) {
		return this.underlyingNode.equals(object);
	}

	private int getMaxRelationshipIndex(final RelTypes relType) {
		// TODO Auto-generated method stub
		return 0;
	}

	protected <T extends GENgraphNode> T getNodeByRelationship(final Class<T> type, final RelTypes relation) {
		return null;
	}

	protected <T extends GENgraphNode> T[] getNodesByRelationship(final Class<T> type, final RelTypes relation) {
		return null;
	}

	public NodeTypes getNodeType() {
		return NodeTypes.valueOf((String) this.dao.getNodeProperty(this.underlyingNode, NodeProperties.Generic.NODE_TYPE));
	}

	protected Object getProperty(final NodeProperties property) {
		return this.dao.getNodeProperty(this.underlyingNode, property);
	}

	private Node getUnderlyingNode() {
		return this.underlyingNode;
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

	private boolean hasRelationship(final RelTypes relType) {
		// TODO Auto-generated method stub
		return false;
	}

	protected void resolveReferences() {
		return;
	}

	protected abstract void setGedcomXProperties(final Object gedcomXObject);

	protected void setGedcomXRelations(final Object gedcomXObject) {
		return;

	}

	private void setNodeType(final NodeTypes nodeType) {
		this.dao.setNodeProperty(this.underlyingNode, NodeProperties.Generic.NODE_TYPE, nodeType.name());
		if (NodeProperties.Generic.NODE_TYPE.isIndexed()) {
			this.dao.setNodeToIndex(NodeProperties.Generic.NODE_TYPE.getIndexName(), this.underlyingNode, NodeProperties.Generic.NODE_TYPE,
					nodeType.name());
		}
	}

	protected void setProperty(final NodeProperties property, final Object value) {
		this.dao.setNodeProperty(this.underlyingNode, property, value);
		if (property.isIndexed()) {
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
