package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;

@NodeType("SOURCE_CITATION")
public class SourceCitation extends NodeWrapper {

	protected SourceCitation(final Node node) throws WrongNodeType, MissingFieldException {
		super(node);
	}

	protected SourceCitation(final org.gedcomx.source.SourceCitation gedcomXSourceCitation) throws MissingFieldException {
		super(gedcomXSourceCitation);
	}

	public SourceCitation(final String value) throws MissingFieldException {
		super(new Object[] { value });
	}

	public void addField(final CitationField citationField) {
		this.addRelationship(GENgraphRelTypes.HAS_CITATION_FIELD, citationField);
	}

	@Override
	protected void deleteAllReferences() {
		this.deleteReferencedNodes(CitationField.class, GENgraphRelTypes.HAS_CITATION_FIELD);
	}

	public ResourceReference getCitationTemplate() {
		return new ResourceReference(new URI((String) this.getProperty(NodeProperties.SourceDescription.CITATION_TEMPLATE)));
	}

	public SourceDescription getDescription() {
		return this.getNodeByRelationship(SourceDescription.class, GENgraphRelTypes.HAS_CITATION, Direction.INCOMING);
	}

	public List<CitationField> getFields() {
		return this.getNodesByRelationship(CitationField.class, GENgraphRelTypes.HAS_CITATION_FIELD);
	}

	@Override
	protected org.gedcomx.source.SourceCitation getGedcomX() {
		final org.gedcomx.source.SourceCitation gedcomXSourceCitation = new org.gedcomx.source.SourceCitation();

		gedcomXSourceCitation.setCitationTemplate(this.getCitationTemplate());
		gedcomXSourceCitation.setFields(this.getGedcomXList(org.gedcomx.source.CitationField.class, this.getFields()));
		gedcomXSourceCitation.setLang(this.getLang());
		gedcomXSourceCitation.setValue(this.getValue());

		return gedcomXSourceCitation;
	}

	public String getLang() {
		return (String) this.getProperty(NodeProperties.Generic.LANG);
	}

	public String getValue() {
		return (String) this.getProperty(NodeProperties.Generic.VALUE);
	}

	@Override
	protected void resolveReferences() {
		return;
	}

	public void setCitationTemplate(final ResourceReference citationTemplate) {
		this.setProperty(NodeProperties.SourceDescription.CITATION_TEMPLATE, citationTemplate.getResource().toString());
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

		for (final org.gedcomx.source.CitationField field : gedcomXSourceCitation.getFields()) {
			this.addField(new CitationField(field));
		}
	}

	public void setLang(final String lang) {
		this.setProperty(NodeProperties.Generic.LANG, lang);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.setValue((String) properties[0]);
	}

	public void setValue(final String value) {
		this.setProperty(NodeProperties.Generic.VALUE, value);
	}

	@Override
	protected void validateUnderlyingNode() throws MissingRequiredPropertyException {
		if (ValidationTools.nullOrEmpty(this.getValue())) {
			throw new MissingRequiredPropertyException(SourceCitation.class, NodeProperties.Generic.VALUE);
		}
	}

}
