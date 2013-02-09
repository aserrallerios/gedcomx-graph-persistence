package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;
import org.neo4j.graphdb.Node;

public class Name extends GENgraphNode implements ConclusionSubnode {

	protected Name(final Node underlyingNode, final NodeTypes nodeType) {
		super(underlyingNode, NodeTypes.NAME);
	}

}
