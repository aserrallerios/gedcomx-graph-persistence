package org.gedcomx.graph.persistence.neo4j.embeded.model.source;

import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;

public class SourceDescription extends GENgraphNode {

	public SourceDescription(final GENgraph geNgraph, final org.gedcomx.source.SourceDescription gedcomXSourceDescription)
			throws MissingFieldException {
		super(geNgraph, NodeTypes.SOURCE_DESCRIPTION, gedcomXSourceDescription);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingFieldException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setInitialProperties(final Object gedcomXObject) {
		// TODO Auto-generated method stub

	}

}
