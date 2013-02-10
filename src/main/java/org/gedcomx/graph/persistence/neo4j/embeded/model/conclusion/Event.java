package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredPropertyException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;

public class Event extends GENgraphNode implements ConclusionSubnode {

	protected Event(final org.gedcomx.conclusion.Event gedcomXEvent) throws MissingRequiredPropertyException {
		super(NodeTypes.EVENT, gedcomXEvent);
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
