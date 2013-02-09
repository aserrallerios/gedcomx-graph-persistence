package org.gedcomx.graph.persistence.neo4j.embeded.model;

import java.util.ArrayList;
import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.dao.impl.GENgraphNodeUtils;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;
import org.neo4j.graphdb.Node;

public abstract class GENgraphNode {

	protected final Node underlyingNode;

	protected GENgraphNode(final Node underlyingNode, final NodeTypes nodeType) {
		this.underlyingNode = underlyingNode;
		this.setNodeType(nodeType);
	}

	public NodeTypes getNodeType() {
		return NodeTypes.valueOf((String) GENgraphNodeUtils.getNodeProperty(this.underlyingNode, NodeProperties.Generic.NODE_TYPE));
	}

	public Node getUnderlyingNode() {
		return this.underlyingNode;
	}

	protected List<ResourceReference> getURIListProperties(final NodeProperties property) {
		final String[] values = (String[]) GENgraphNodeUtils.getNodeProperty(this.underlyingNode, property);
		final List<ResourceReference> resourceList = new ArrayList<>();
		for (final String uri : values) {
			resourceList.add(new ResourceReference(new URI(uri)));
		}
		return resourceList;
	}

	private void setNodeType(final NodeTypes nodeType) {
		GENgraphNodeUtils.addNodeProperty(this.underlyingNode, NodeProperties.Generic.NODE_TYPE, nodeType.name());
	}

	protected void setURIListProperties(final List<ResourceReference> resourceList, final NodeProperties property) {
		final String[] values = new String[resourceList.size()];
		int i = 0;
		for (final ResourceReference resource : resourceList) {
			values[i++] = resource.getResource().toString();
		}
		GENgraphNodeUtils.addNodeProperty(this.underlyingNode, property, values);
	}
}
