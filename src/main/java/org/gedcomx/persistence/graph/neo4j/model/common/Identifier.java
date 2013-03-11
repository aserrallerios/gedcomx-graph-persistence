package org.gedcomx.persistence.graph.neo4j.model.common;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.model.GENgraphNode;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.utils.RelTypes;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;

public class Identifier extends GENgraphNode {

	public Identifier(final Node node) throws WrongNodeType {
		super(NodeTypes.IDENTIFIER, node);
	}

	public Identifier(final org.gedcomx.conclusion.Identifier gedcomXIdentifier) throws MissingFieldException {
		super(NodeTypes.IDENTIFIER, gedcomXIdentifier);
	}

	public Identifier(final URI value) {
		super(NodeTypes.IDENTIFIER, new Object[] { value });
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

	public GENgraphNode getParentNode() {
		// TODO
		return this.getNodeByRelationship(GENgraphNode.class, RelTypes.HAS_IDENTIFIER, Direction.INCOMING);
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
	protected void validateGedcomXObject(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.Identifier gedcomXIdentifier = (org.gedcomx.conclusion.Identifier) gedcomXObject;
		if (gedcomXIdentifier.getValue() == null) {
			throw new MissingRequiredPropertyException(Identifier.class, NodeProperties.Generic.VALUE);
		}
	}

	@Override
	protected void validateUnderlyingNode() throws WrongNodeType {
		if ((this.getValue() == null)) {
			throw new WrongNodeType();
		}
	}

}
