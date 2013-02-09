package org.gedcomx.graph.persistence.neo4j.embeded.model.common;

import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;
import org.neo4j.graphdb.Node;

public class Identifier extends GENgraphNode {

	protected Identifier(final Node underlyingNode, final NodeTypes nodeType) {
		super(underlyingNode, NodeTypes.IDENTIFIER);
	}

}
