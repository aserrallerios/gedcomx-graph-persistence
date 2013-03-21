package org.gedcomx.persistence.graph.neo4j.exception;

public class MissingRequiredRelationshipException extends MissingFieldException {

	private final Class node;
	private final String relationship;
	private String id;

	public MissingRequiredRelationshipException(final Class node, final String rel) {
		super();
		this.node = node;
		this.relationship = rel;
	}

	public MissingRequiredRelationshipException(final Class node, final String id, final String rel) {
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

	public String getRelationship() {
		return this.relationship;
	}

}
