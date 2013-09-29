package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.SourceProperties;
import org.gedcomx.persistence.graph.neo4j.utils.Validation;
import org.neo4j.graphdb.Node;

@NodeType(NodeTypes.CITATION_FIELD)
public class CitationField extends NodeWrapper {

	protected CitationField(final Node node) {
		super(node);
	}

	protected CitationField(
			final org.gedcomx.source.CitationField gedcomXCitationField) {
		super(gedcomXCitationField);
	}

	protected CitationField(final URI name, final String value) {
		super(name, value);
	}

	@Override
	protected void deleteAllReferences() {
		return;
	}

	public SourceCitation getCitation() {
		return (SourceCitation) NodeWrapper.nodeWrapperOperations
				.getParentNode(this, RelationshipTypes.HAS_CITATION_FIELD);
	}

	@Override
	public org.gedcomx.source.CitationField getGedcomX() {
		final org.gedcomx.source.CitationField gedcomXCitationField = new org.gedcomx.source.CitationField();

		gedcomXCitationField.setName(this.getName());
		gedcomXCitationField.setValue(this.getValue());

		return gedcomXCitationField;
	}

	public URI getName() {
		return new URI((String) NodeWrapper.nodeWrapperOperations.getProperty(
				this, SourceProperties.NAME));
	}

	@Override
	public SourceCitation getParentNode() {
		return this.getCitation();
	}

	public String getValue() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				GenericProperties.VALUE);
	}

	@Override
	public void resolveReferences() {
		return;
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.source.CitationField gedcomXCitationField = (org.gedcomx.source.CitationField) gedcomXObject;

		this.setName(gedcomXCitationField.getName());
		this.setValue(gedcomXCitationField.getValue());
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) {
		return;
	}

	public void setName(final URI name) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				SourceProperties.NAME, name);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.setName((URI) properties[0]);
		this.setValue((String) properties[1]);
	}

	public void setValue(final String value) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.VALUE, value);
	}

	@Override
	protected void validateUnderlyingNode() {
		if (Validation.nullOrEmpty(this.getName())) {
			throw new MissingRequiredPropertyException(
					NodeWrapper.nodeWrapperOperations
							.getAnnotatedNodeType(this),
					GenericProperties.VALUE.toString());
		}
		if (Validation.nullOrEmpty(this.getValue())) {
			throw new MissingRequiredPropertyException(
					NodeWrapper.nodeWrapperOperations
							.getAnnotatedNodeType(this),
					SourceProperties.NAME.toString());
		}
	}

}
