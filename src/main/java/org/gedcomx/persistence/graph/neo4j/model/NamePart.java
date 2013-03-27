package org.gedcomx.persistence.graph.neo4j.model;

import java.util.ArrayList;
import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.gedcomx.types.NamePartQualifierType;
import org.gedcomx.types.NamePartType;
import org.neo4j.graphdb.Node;

@NodeType("NAME_PART")
public class NamePart extends NodeWrapper {

	protected NamePart(final Node node) throws MissingFieldException, UnknownNodeType {
		super(node);
	}

	protected NamePart(final org.gedcomx.conclusion.NamePart gedcomXNamePart) throws MissingFieldException {
		super(gedcomXNamePart);
	}

	protected NamePart(final String value) throws MissingFieldException {
		super(value);
	}

	@Override
	protected void deleteAllReferences() {
		return;
	}

	@Override
	public org.gedcomx.conclusion.NamePart getGedcomX() {
		final org.gedcomx.conclusion.NamePart gedcomXNamePart = new org.gedcomx.conclusion.NamePart();

		gedcomXNamePart.setType(this.getType());
		gedcomXNamePart.setKnownType(this.getKnownType());
		gedcomXNamePart.setValue(this.getValue());
		gedcomXNamePart.setQualifiers(this.getQualifiers());

		return gedcomXNamePart;
	}

	public List<NamePartQualifierType> getKnownQualifiers() {
		final List<ResourceReference> references = this.getURIListProperties(ConclusionProperties.QUALIFIERS);

		final List<NamePartQualifierType> result = new ArrayList<NamePartQualifierType>();
		for (final ResourceReference ref : references) {
			result.add(NamePartQualifierType.fromQNameURI(ref.getResource()));
		}
		return result;
	}

	public NamePartType getKnownType() {
		return NamePartType.fromQNameURI(this.getType());
	}

	public NameForm getNameForm() {
		return (NameForm) this.getParentNode(RelationshipTypes.HAS_NAME_PART);
	}

	@Deprecated
	public List<ResourceReference> getQualifiers() {
		return this.getURIListProperties(ConclusionProperties.QUALIFIERS);
	}

	@Deprecated
	public URI getType() {
		return new URI((String) this.getProperty(GenericProperties.TYPE));
	}

	public String getValue() {
		return (String) this.getProperty(GenericProperties.VALUE);
	}

	@Override
	public void resolveReferences() {
		return;
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.NamePart gedcomXNamePart = (org.gedcomx.conclusion.NamePart) gedcomXObject;

		this.setType(gedcomXNamePart.getType());
		this.setValue(gedcomXNamePart.getValue());
		this.setQualifiers(gedcomXNamePart.getQualifiers());
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) throws MissingFieldException {
		return;
	}

	public void setKnownQualifiers(final List<NamePartQualifierType> qualifiers) {
		this.setProperty(ConclusionProperties.QUALIFIERS, qualifiers);
	}

	public void setKnownType(final NamePartType type) {
		this.setType(type.toQNameURI());
	}

	@Deprecated
	public void setQualifiers(final List<ResourceReference> qualifiers) {
		this.setURIListProperties(ConclusionProperties.QUALIFIERS, qualifiers);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) throws MissingFieldException {
		this.setValue((String) properties[0]);
	}

	@Deprecated
	public void setType(final URI type) {
		this.setProperty(GenericProperties.TYPE, type);
	}

	public void setValue(final String value) {
		this.setProperty(GenericProperties.VALUE, value);
	}

	@Override
	protected void validateUnderlyingNode() throws MissingFieldException {
		if (ValidationTools.nullOrEmpty(this.getValue())) {
			throw new MissingRequiredPropertyException(this.getAnnotatedNodeType(), GenericProperties.VALUE.toString());
		}
	}

}
