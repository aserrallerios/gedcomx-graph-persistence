package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;

public class Relationship extends ConclusionSubnode {

	protected Relationship(final GENgraph graph, final org.gedcomx.conclusion.Relationship gedcomXRelationship)
			throws MissingFieldException {
		super(graph, NodeTypes.RELATIONSHIP, gedcomXRelationship);
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
