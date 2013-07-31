package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.utils.Validation;
import org.gedcomx.types.IdentifierType;
import org.neo4j.graphdb.Node;

@NodeType(NodeTypes.IDENTIFIER)
public class Identifier extends NodeWrapper {

	protected Identifier(final Node node) {
		super(node);
	}

	protected Identifier(
			final org.gedcomx.conclusion.Identifier gedcomXIdentifier) {
		super(gedcomXIdentifier);
	}

	protected Identifier(final URI value) {
		super(new Object[] { value });
	}

	@Override
	protected void deleteAllReferences() {
		return;
	}

	@Override
	public org.gedcomx.conclusion.Identifier getGedcomX() {
		final org.gedcomx.conclusion.Identifier gedcomXIdentifier = new org.gedcomx.conclusion.Identifier();

		gedcomXIdentifier.setValue(this.getValue());
		gedcomXIdentifier.setType(this.getType());

		return gedcomXIdentifier;
	}

	public IdentifierType getKnownType() {
		return IdentifierType.fromQNameURI(this.getType());
	}

	public NodeWrapper getParentNode() {
		return NodeWrapper.nodeWrapperOperations.getParentNode(this,
				RelationshipTypes.HAS_IDENTIFIER);
	}

	@Deprecated
	public URI getType() {
		return new URI((String) NodeWrapper.nodeWrapperOperations.getProperty(
				this, GenericProperties.TYPE));
	}

	public URI getValue() {
		return new URI((String) NodeWrapper.nodeWrapperOperations.getProperty(
				this, GenericProperties.VALUE));
	}

	@Override
	public void resolveReferences() {
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

	public void setKnownType(final IdentifierType type) {
		this.setType(type.toQNameURI());
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.setValue((URI) properties[0]);
	}

	@Deprecated
	public void setType(final URI type) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.TYPE, type);
	}

	public void setValue(final URI value) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.VALUE, value);
	}

	@Override
	protected void validateUnderlyingNode() {
		if (Validation.nullOrEmpty(this.getValue())) {
			throw new MissingRequiredPropertyException(
					NodeWrapper.nodeWrapperOperations
							.getAnnotatedNodeType(this),
					GenericProperties.VALUE.toString());
		}
	}

}
