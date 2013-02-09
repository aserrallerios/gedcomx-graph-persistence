package org.gedcomx.graph.persistence.neo4j.embeded.model.contributor;

import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;
import org.neo4j.graphdb.Node;

public class Account extends GENgraphNode {

	protected Account(final Node underlyingNode, final NodeTypes nodeType) {
		super(underlyingNode, NodeTypes.ACCOUNT);
	}

}
