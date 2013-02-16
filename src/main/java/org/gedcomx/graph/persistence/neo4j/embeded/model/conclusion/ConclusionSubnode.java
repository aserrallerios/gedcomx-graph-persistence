package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;

public abstract class ConclusionSubnode extends GENgraphNode {

	protected ConclusionSubnode(final GENgraph graph, final NodeTypes nodeType, final Object gedcomXObject) throws MissingFieldException {
		super(graph, nodeType, gedcomXObject);
	}

}
