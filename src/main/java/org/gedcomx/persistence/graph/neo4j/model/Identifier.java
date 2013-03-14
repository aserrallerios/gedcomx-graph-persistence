package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.neo4j.graphdb.Node;

@NodeType("IDENTIFIER")
public class Identifier extends NodeWrapper {

	public Identifier(final Node node) throws WrongNodeType, MissingFieldException {
		super(node);
	}

	public Identifier(final org.gedcomx.conclusion.Identifier gedcomXIdentifier) throws MissingFieldException {
		super(gedcomXIdentifier);
	}

	public Identifier(final URI value) throws MissingFieldException {
		super(new Object[] { value });
	}

	@Override
	protected void deleteAllReferences() {
		return;
	}

	@Override
	protected org.gedcomx.conclusion.Identifier getGedcomX() {
		final org.gedcomx.conclusion.Identifier gedcomXIdentifier = new org.gedcomx.conclusion.Identifier();

		gedcomXIdentifier.setValue(this.getValue());
		gedcomXIdentifier.setType(this.getType());

		return gedcomXIdentifier;
	}

	public NodeWrapper getParentNode() {
		return super.getParentNode(GENgraphRelTypes.HAS_IDENTIFIER);
	}

	public URI getType() {
		final String type = (String) this.getProperty(NodeProperties.Generic.TYPE);
		return new URI(type);
	}

	public URI getValue() {
		final String value = (String) this.getProperty(NodeProperties.Generic.VALUE);
		return new URI(value);
	}

	@Override
	protected void resolveReferences() {
		return;
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Identifier gedcomXIdentifier = (org.gedcomx.conclusion.Identifier) gedcomXObject;
		this.setValue(gedcomXIdentifier.getValue());
		this.setType(gedcomXIdentifier.getType());
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) {
		return;
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.setValue((URI) properties[0]);
	}

	public void setType(final URI type) {
		this.setProperty(NodeProperties.Generic.TYPE, type.toString());
	}

	public void setValue(final URI value) {
		this.setProperty(NodeProperties.Generic.VALUE, value.toString());
	}

	@Override
	protected void validateUnderlyingNode() throws MissingRequiredPropertyException {
		if (ValidationTools.nullOrEmpty(this.getValue())) {
			throw new MissingRequiredPropertyException(Identifier.class, NodeProperties.Generic.VALUE);
		}
	}

}
