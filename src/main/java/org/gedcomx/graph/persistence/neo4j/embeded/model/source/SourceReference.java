package org.gedcomx.graph.persistence.neo4j.embeded.model.source;

import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;

public class SourceReference extends GENgraphNode {

	public SourceReference(final GENgraph graph, final Object gedcomXObject) throws MissingFieldException {
		super(graph, NodeTypes.SOURCE_REFERENCE, gedcomXObject);
	}

	@Override
	protected void setInitialProperties(final Object gedcomXObject) {
		// TODO Auto-generated method stub

	}

}
