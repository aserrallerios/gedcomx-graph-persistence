package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.neo4j.graphdb.Node;

@NodeType("CITATION_FIELD")
public class CitationField extends NodeWrapper {

	protected CitationField(final Node node) throws MissingFieldException, UnknownNodeType {
		super(node);
	}

	public CitationField(final org.gedcomx.source.CitationField gedcomXCitationField) throws MissingFieldException {
		super(gedcomXCitationField);
	}

	public CitationField(final URI name, final String value) throws MissingFieldException {
		super(name, value);
	}

	@Override
	protected void deleteAllReferences() {
		return;
	}

	public SourceCitation getCitation() {
		return (SourceCitation) this.getParentNode(GENgraphRelTypes.HAS_CITATION_FIELD);
	}

	@Override
	protected org.gedcomx.source.CitationField getGedcomX() {
		final org.gedcomx.source.CitationField gedcomXCitationField = new org.gedcomx.source.CitationField();

		gedcomXCitationField.setName(this.getName());
		gedcomXCitationField.setValue(this.getValue());

		return gedcomXCitationField;
	}

	public URI getName() {
		return new URI((String) this.getProperty(SourceProperties.NAME));
	}

	public String getValue() {
		return (String) this.getProperty(GenericProperties.VALUE);
	}

	@Override
	protected void resolveReferences() {
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
		this.setProperty(SourceProperties.NAME, name);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.setName((URI) properties[0]);
		this.setValue((String) properties[1]);
	}

	public void setValue(final String value) {
		this.setProperty(GenericProperties.VALUE, value);
	}

	@Override
	protected void validateUnderlyingNode() throws MissingRequiredPropertyException {
		if (ValidationTools.nullOrEmpty(this.getName())) {
			throw new MissingRequiredPropertyException(CitationField.class, GenericProperties.VALUE);
		}
		if (ValidationTools.nullOrEmpty(this.getValue())) {
			throw new MissingRequiredPropertyException(CitationField.class, SourceProperties.NAME);
		}

	}

}