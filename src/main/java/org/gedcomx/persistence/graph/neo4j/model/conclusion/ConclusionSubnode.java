package org.gedcomx.persistence.graph.neo4j.model.conclusion;

import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.model.GENgraph;
import org.gedcomx.persistence.graph.neo4j.model.GENgraphNode;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;

public abstract class ConclusionSubnode extends GENgraphNode {

	protected ConclusionSubnode(final GENgraph graph, final NodeTypes nodeType, final Object gedcomXObject) throws MissingFieldException {
		super(graph, nodeType, gedcomXObject);
	}

}
