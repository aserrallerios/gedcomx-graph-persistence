package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;
import org.neo4j.graphdb.Node;

public class PlaceDescription extends GENgraphNode implements ConclusionSubnode {

	protected PlaceDescription(final Node underlyingNode, final NodeTypes nodeType) {
		super(underlyingNode, NodeTypes.PLACE_DESCRIPTION);
	}

}
