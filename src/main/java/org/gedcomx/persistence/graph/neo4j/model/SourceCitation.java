package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.SourceProperties;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.neo4j.graphdb.Node;

@NodeType("SOURCE_CITATION")
public class SourceCitation extends NodeWrapper {

	protected SourceCitation(final Node node) throws UnknownNodeType, MissingFieldException {
		super(node);
	}

	protected SourceCitation(final org.gedcomx.source.SourceCitation gedcomXSourceCitation) throws MissingFieldException {
		super(gedcomXSourceCitation);
	}

	public SourceCitation(final String value) throws MissingFieldException {
		super(new Object[] { value });
	}

	public void addField(final CitationField citationField) {
		this.addRelationship(RelationshipTypes.HAS_CITATION_FIELD, citationField);
	}

	@Override
	protected void deleteAllReferences() {
		this.deleteReferencedNodes(this.getFields());
	}

	public ResourceReference getCitationTemplate() {
		return new ResourceReference(new URI((String) this.getProperty(SourceProperties.CITATION_TEMPLATE)));
	}

	public SourceDescription getDescription() {
		return (SourceDescription) this.getParentNode(RelationshipTypes.HAS_CITATION);
	}

	public List<CitationField> getFields() {
		return this.getNodesByRelationship(CitationField.class, RelationshipTypes.HAS_CITATION_FIELD);
	}

	@Override
	public org.gedcomx.source.SourceCitation getGedcomX() {
		final org.gedcomx.source.SourceCitation gedcomXSourceCitation = new org.gedcomx.source.SourceCitation();

		gedcomXSourceCitation.setCitationTemplate(this.getCitationTemplate());
		gedcomXSourceCitation.setFields(this.getGedcomXList(org.gedcomx.source.CitationField.class, this.getFields()));
		gedcomXSourceCitation.setLang(this.getLang());
		gedcomXSourceCitation.setValue(this.getValue());

		return gedcomXSourceCitation;
	}

	public String getLang() {
		return (String) this.getProperty(GenericProperties.LANG);
	}

	public String getValue() {
		return (String) this.getProperty(GenericProperties.VALUE);
	}

	@Override
	public void resolveReferences() {
		return;
	}

	public void setCitationTemplate(final ResourceReference citationTemplate) {
		this.setProperty(SourceProperties.CITATION_TEMPLATE, citationTemplate);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.source.SourceCitation gedcomXSourceCitation = (org.gedcomx.source.SourceCitation) gedcomXObject;

		this.setLang(gedcomXSourceCitation.getLang());
		this.setValue(gedcomXSourceCitation.getValue());
		this.setCitationTemplate(gedcomXSourceCitation.getCitationTemplate());
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.source.SourceCitation gedcomXSourceCitation = (org.gedcomx.source.SourceCitation) gedcomXObject;

		if (gedcomXSourceCitation.getFields() != null) {
			for (final org.gedcomx.source.CitationField field : gedcomXSourceCitation.getFields()) {
				this.addField(new CitationField(field));
			}
		}
	}

	public void setLang(final String lang) {
		this.setProperty(GenericProperties.LANG, lang);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.setValue((String) properties[0]);
	}

	public void setValue(final String value) {
		this.setProperty(GenericProperties.VALUE, value);
	}

	@Override
	protected void validateUnderlyingNode() throws MissingRequiredPropertyException {
		if (ValidationTools.nullOrEmpty(this.getValue())) {
			throw new MissingRequiredPropertyException(this.getAnnotatedNodeType(), GenericProperties.VALUE.toString());
		}
	}

}
