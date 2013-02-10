package org.gedcomx.graph.persistence.neo4j.embeded.model.common;

import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredPropertyException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;

public class Identifier extends GENgraphNode {

	public Identifier(final org.gedcomx.conclusion.Identifier gedcomXIdentifier) throws MissingRequiredPropertyException {
		super(NodeTypes.IDENTIFIER, gedcomXIdentifier);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingRequiredPropertyException {
		final org.gedcomx.conclusion.Identifier gedcomXIdentifier = (org.gedcomx.conclusion.Identifier) gedcomXObject;
		if (gedcomXIdentifier.getValue() == null) {
			throw new MissingRequiredPropertyException();
		}
	}

	public URI getType() {
		final String type = (String) this.getProperty(NodeProperties.Generic.IDENTIFIER_TYPE);
		return new URI(type);
	}

	public URI getValue() {
		final String value = (String) this.getProperty(NodeProperties.Generic.VALUE);
		return new URI(value);
	}

	@Override
	protected void setInitialProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Identifier gedcomXIdentifier = (org.gedcomx.conclusion.Identifier) gedcomXObject;
		this.setValue(gedcomXIdentifier.getValue());
		this.setType(gedcomXIdentifier.getType());
	}

	public void setType(final URI type) {
		this.setProperty(NodeProperties.Generic.IDENTIFIER_TYPE, type.toString());
	}

	public void setValue(final URI value) {
		this.setProperty(NodeProperties.Generic.VALUE, value.toString());
	}

}
