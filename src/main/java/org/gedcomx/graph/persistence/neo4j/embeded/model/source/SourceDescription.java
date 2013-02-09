package org.gedcomx.graph.persistence.neo4j.embeded.model.source;

import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;
import org.neo4j.graphdb.Node;

public class SourceDescription extends GENgraphNode {

	public SourceDescription(final Node underlyingNode, final org.gedcomx.source.SourceDescription gedcomXSourceDescription) {
		super(underlyingNode, NodeTypes.SOURCE_DESCRIPTION);
	}

}
