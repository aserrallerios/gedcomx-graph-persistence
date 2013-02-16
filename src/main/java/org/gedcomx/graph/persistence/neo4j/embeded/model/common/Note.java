package org.gedcomx.graph.persistence.neo4j.embeded.model.common;

import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;

public class Note extends GENgraphNode {

	public Note(final GENgraph graph, final Object gedcomXObject) throws MissingFieldException {
		super(graph, NodeTypes.NOTE, gedcomXObject);
	}

	@Override
	protected void setInitialProperties(final Object gedcomXObject) {
		// TODO Auto-generated method stub

	}

}
