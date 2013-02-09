package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;
import org.neo4j.graphdb.Node;

public class Relationship extends GENgraphNode implements ConclusionSubnode {

	protected Relationship(final Node underlyingNode, final NodeTypes nodeType) {
		super(underlyingNode, NodeTypes.RELATIONSHIP);
	}

}
