package org.gedcomx.graph.persistence.neo4j.embeded.model;

import java.util.ArrayList;
import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.dao.GENgraphDAO;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.RelTypes;
import org.neo4j.graphdb.Node;

public abstract class GENgraphNode {

	private final Node underlyingNode;

	private final GENgraph graph;

	protected GENgraphNode(final GENgraph graph, final NodeTypes nodeType, final Object gedcomXObject) throws MissingFieldException {
		this.graph = graph;
		this.checkRequiredProperties(gedcomXObject);
		this.underlyingNode = this.graph.getDao().createNode();
		this.setNodeType(nodeType);
		this.setInitialProperties(gedcomXObject);
		this.setRelations(gedcomXObject);
	}

	protected void addNodeToResolveReferences() {
		this.graph.addNodeToResolveReferences(this);
	}

	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingFieldException {
		return;
	}

	protected void createRelationship(final RelTypes relType, final GENgraphNode node) {
		this.getDAO().createRelationship(this.underlyingNode, relType, node.getUnderlyingNode());
	}

	@Override
	public boolean equals(final Object object) {
		return this.underlyingNode.equals(object);
	}

	private GENgraphDAO getDAO() {
		return this.getGraph().getDao();
	}

	protected GENgraph getGraph() {
		return this.graph;
	}

	public NodeTypes getNodeType() {
		return NodeTypes.valueOf((String) this.getDAO().getNodeProperty(this.underlyingNode, NodeProperties.Generic.NODE_TYPE));
	}

	protected Object getProperty(final NodeProperties property) {
		return this.getDAO().getNodeProperty(this.underlyingNode, property);
	}

	public Node getUnderlyingNode() {
		return this.underlyingNode;
	}

	protected List<ResourceReference> getURIListProperties(final NodeProperties property) {
		final String[] values = (String[]) this.getDAO().getNodeProperty(this.underlyingNode, property);
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

	protected abstract void setInitialProperties(final Object gedcomXObject);

	private void setNodeType(final NodeTypes nodeType) {
		this.getDAO().addNodeProperty(this.underlyingNode, NodeProperties.Generic.NODE_TYPE, nodeType.name());
		if (NodeProperties.Generic.NODE_TYPE.isIndexed()) {
			this.graph.getDao().addNodeToIndex(NodeProperties.Generic.NODE_TYPE.getIndexName(), this.underlyingNode,
					NodeProperties.Generic.NODE_TYPE, nodeType.name());
		}
	}

	protected void setProperty(final NodeProperties property, final Object value) {
		this.getDAO().addNodeProperty(this.underlyingNode, property, value);
		if (property.isIndexed()) {
			this.graph.getDao().addNodeToIndex(property.getIndexName(), this.underlyingNode, property, value);
		}
	}

	protected void setRelations(final Object gedcomXObject) throws MissingFieldException {
		return;
	}

	protected void setURIListProperties(final NodeProperties property, final List<ResourceReference> resourceList) {
		final String[] values = new String[resourceList.size()];
		int i = 0;
		for (final ResourceReference resource : resourceList) {
			values[i++] = resource.getResource().toString();
		}
		this.getDAO().addNodeProperty(this.underlyingNode, property, values);
	}

	@Override
	public String toString() {
		return this.underlyingNode.toString();
	}
}
