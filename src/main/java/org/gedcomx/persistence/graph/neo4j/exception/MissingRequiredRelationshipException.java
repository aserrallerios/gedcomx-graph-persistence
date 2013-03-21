package org.gedcomx.persistence.graph.neo4j.exception;

import org.neo4j.graphdb.RelationshipType;

public class MissingRequiredRelationshipException extends MissingFieldException {

	private final Class node;
	private final RelationshipType relationship;
	private String id;

	public MissingRequiredRelationshipException(final Class node, final RelationshipType rel) {
		super();
		this.node = node;
		this.relationship = rel;
	}

	public MissingRequiredRelationshipException(final Class node, final String id, final RelationshipType rel) {
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

	public RelationshipType getRelationship() {
		return this.relationship;
	}

}
