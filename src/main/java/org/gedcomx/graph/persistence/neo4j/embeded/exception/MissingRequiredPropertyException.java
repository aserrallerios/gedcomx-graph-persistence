package org.gedcomx.graph.persistence.neo4j.embeded.exception;

import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;

public class MissingRequiredPropertyException extends MissingFieldException {

	private final Class node;
	private final NodeProperties property;

	public MissingRequiredPropertyException(final Class node, final NodeProperties property) {
		super();
		this.node = node;
		this.property = property;
	}

	public Class getNode() {
		return this.node;
	}

	public NodeProperties getProperty() {
		return this.property;
	}

}
