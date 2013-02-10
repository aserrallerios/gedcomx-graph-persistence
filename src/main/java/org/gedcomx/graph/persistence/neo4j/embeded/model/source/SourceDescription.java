package org.gedcomx.graph.persistence.neo4j.embeded.model.source;

import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredPropertyException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;

public class SourceDescription extends GENgraphNode {

	public SourceDescription(final org.gedcomx.source.SourceDescription gedcomXSourceDescription) throws MissingRequiredPropertyException {
		super(NodeTypes.SOURCE_DESCRIPTION, gedcomXSourceDescription);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingRequiredPropertyException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setInitialProperties(final Object gedcomXObject) {
		// TODO Auto-generated method stub

	}

}
