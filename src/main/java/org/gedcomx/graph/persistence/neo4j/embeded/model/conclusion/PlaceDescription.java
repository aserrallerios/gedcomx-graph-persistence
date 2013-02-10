package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredPropertyException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;

public class PlaceDescription extends GENgraphNode implements ConclusionSubnode {

	protected PlaceDescription(final org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription)
			throws MissingRequiredPropertyException {
		super(NodeTypes.PLACE_DESCRIPTION, gedcomXPlaceDescription);
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
