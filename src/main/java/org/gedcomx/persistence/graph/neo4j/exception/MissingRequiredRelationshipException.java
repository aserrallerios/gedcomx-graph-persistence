package org.gedcomx.persistence.graph.neo4j.exception;

public class MissingRequiredRelationshipException extends MissingFieldException {

	private final String nodeType;
	private final String relationship;
	private String id;

	public MissingRequiredRelationshipException(final String nodeType, final String rel) {
		super();
		this.nodeType = nodeType;
		this.relationship = rel;
	}

	public MissingRequiredRelationshipException(final String nodeType, final String id, final String rel) {
		super();
		this.nodeType = nodeType;
		this.id = id;
		this.relationship = rel;
	}

	public String getId() {
		return this.id;
	}

	public String getNodeType() {
		return this.nodeType;
	}

	public String getRelationship() {
		return this.relationship;
	}

}
