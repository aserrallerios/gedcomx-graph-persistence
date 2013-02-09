package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;
import org.neo4j.graphdb.Node;

public class Event extends GENgraphNode implements ConclusionSubnode {

	protected Event(final Node underlyingNode, final NodeTypes nodeType) {
		super(underlyingNode, NodeTypes.EVENT);
		// TODO Auto-generated constructor stub
	}

}
