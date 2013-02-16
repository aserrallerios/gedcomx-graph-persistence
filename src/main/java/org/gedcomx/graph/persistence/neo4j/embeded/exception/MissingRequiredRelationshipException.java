package org.gedcomx.graph.persistence.neo4j.embeded.exception;

import org.gedcomx.graph.persistence.neo4j.embeded.utils.RelTypes;

public class MissingRequiredRelationshipException extends MissingFieldException {

	private final Class node;
	private final RelTypes relationship;

	public MissingRequiredRelationshipException(final Class node, final RelTypes rel) {
		super();
		this.node = node;
		this.relationship = rel;
	}

	public Class getNode() {
		return this.node;
	}

	public RelTypes getRelationship() {
		return this.relationship;
	}

}
