package org.gedcomx.graph.persistence.neo4j.embeded.model.contributor;

import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;
import org.neo4j.graphdb.Node;

public class Address extends GENgraphNode {

	protected Address(final Node underlyingNode) {
		super(underlyingNode, NodeTypes.ADDRESS);
	}

}
