package org.gedcomx.persistence.graph.neo4j.exception;

import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;

public class MissingRequiredRelationshipException extends MissingFieldException {

	private final Class node;
	private final GENgraphRelTypes relationship;
	private String id;

	public MissingRequiredRelationshipException(final Class node, final GENgraphRelTypes rel) {
		super();
		this.node = node;
		this.relationship = rel;
	}

	public MissingRequiredRelationshipException(final Class node, final String id, final GENgraphRelTypes rel) {
		super();
		this.node = node;
		this.id = id;
		this.relationship = rel;
	}

	public String getId() {
		return this.id;
	}

	public Class getNode() {
		return this.node;
	}

	public GENgraphRelTypes getRelationship() {
		return this.relationship;
	}

}
