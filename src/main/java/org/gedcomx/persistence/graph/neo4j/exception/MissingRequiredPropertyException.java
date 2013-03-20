package org.gedcomx.persistence.graph.neo4j.exception;

import org.gedcomx.persistence.graph.neo4j.model.NodeWrapper;
import org.gedcomx.persistence.graph.neo4j.model.NodeWrapper.NodeProperties;

public class MissingRequiredPropertyException extends MissingFieldException {

	private final Class<? extends NodeWrapper> node;
	private final NodeProperties property;
	private String id;

	public MissingRequiredPropertyException(final Class<? extends NodeWrapper> node, final NodeProperties property) {
		super();
		this.node = node;
		this.property = property;
	}

	public MissingRequiredPropertyException(final Class<? extends NodeWrapper> node, final String id, final NodeProperties property) {
		super();
		this.node = node;
		this.id = id;
		this.property = property;
	}

	public String getId() {
		return this.id;
	}

	public Class<? extends NodeWrapper> getNode() {
		return this.node;
	}

	public NodeProperties getProperty() {
		return this.property;
	}

}
