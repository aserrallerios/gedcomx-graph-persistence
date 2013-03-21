package org.gedcomx.persistence.graph.neo4j.exception;



public class MissingRequiredPropertyException extends MissingFieldException {

	private final String nodeType;
	private final String property;
	private String id;

	public MissingRequiredPropertyException(final String nodeType, final String property) {
		super();
		this.nodeType = nodeType;
		this.property = property;
	}

	public MissingRequiredPropertyException(final String nodeType, final String id, final String property) {
		super();
		this.nodeType = nodeType;
		this.id = id;
		this.property = property;
	}

	public String getId() {
		return this.id;
	}

	public String getNodeType() {
		return this.nodeType;
	}

	public String getProperty() {
		return this.property;
	}

}
