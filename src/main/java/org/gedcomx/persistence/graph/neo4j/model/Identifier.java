package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.gedcomx.types.IdentifierType;
import org.neo4j.graphdb.Node;

@NodeType("IDENTIFIER")
public class Identifier extends NodeWrapper {

	public Identifier(final Node node) throws UnknownNodeType, MissingFieldException {
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
		return super.getParentNode(RelationshipTypes.HAS_IDENTIFIER);
	}

	@Deprecated
	public URI getType() {
		return new URI((String) this.getProperty(GenericProperties.TYPE));
	}

	public URI getValue() {
		return new URI((String) this.getProperty(GenericProperties.VALUE));
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
		this.setProperty(GenericProperties.TYPE, type);
	}

	public void setValue(final URI value) {
		this.setProperty(GenericProperties.VALUE, value);
	}

	@Override
	protected void validateUnderlyingNode() throws MissingRequiredPropertyException {
		if (ValidationTools.nullOrEmpty(this.getValue())) {
			throw new MissingRequiredPropertyException(this.getAnnotatedNodeType(), GenericProperties.VALUE.toString());
		}
	}

}
