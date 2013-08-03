package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.SourceProperties;
import org.gedcomx.persistence.graph.neo4j.utils.Validation;
import org.neo4j.graphdb.Node;

@NodeType(NodeTypes.SOURCE_CITATION)
public class SourceCitation extends NodeWrapper {

	protected SourceCitation(final Node node) {
		super(node);
	}

	protected SourceCitation(
			final org.gedcomx.source.SourceCitation gedcomXSourceCitation) {
		super(gedcomXSourceCitation);
	}

	protected SourceCitation(final String value) {
		super(new Object[] { value });
	}

	private CitationField addField(final CitationField citationField) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_CITATION_FIELD, citationField);
		return citationField;
	}

	public CitationField addField(final URI name, final String value) {
		return this.addField(new CitationField(name, value));
	}

	@Override
	protected void deleteAllReferences() {
		NodeWrapper.nodeWrapperOperations.deleteReferencedNodes(this
				.getFields());
	}

	public ResourceReference getCitationTemplate() {
		return new ResourceReference(new URI(
				(String) NodeWrapper.nodeWrapperOperations.getProperty(this,
						SourceProperties.CITATION_TEMPLATE)));
	}

	public SourceDescription getDescription() {
		return (SourceDescription) NodeWrapper.nodeWrapperOperations
				.getParentNode(this, RelationshipTypes.HAS_CITATION);
	}

	public List<CitationField> getFields() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				CitationField.class, RelationshipTypes.HAS_CITATION_FIELD);
	}

	@Override
	public org.gedcomx.source.SourceCitation getGedcomX() {
		final org.gedcomx.source.SourceCitation gedcomXSourceCitation = new org.gedcomx.source.SourceCitation();

		gedcomXSourceCitation.setCitationTemplate(this.getCitationTemplate());
		gedcomXSourceCitation.setFields(NodeWrapper.nodeWrapperOperations
				.getGedcomXList(org.gedcomx.source.CitationField.class,
						this.getFields()));
		gedcomXSourceCitation.setLang(this.getLang());
		gedcomXSourceCitation.setValue(this.getValue());

		return gedcomXSourceCitation;
	}

	public String getLang() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				GenericProperties.LANG);
	}

	public String getValue() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				GenericProperties.VALUE);
	}

	@Override
	public void resolveReferences() {
		return;
	}

	public void setCitationTemplate(final ResourceReference citationTemplate) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				SourceProperties.CITATION_TEMPLATE, citationTemplate);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.source.SourceCitation gedcomXSourceCitation = (org.gedcomx.source.SourceCitation) gedcomXObject;

		this.setLang(gedcomXSourceCitation.getLang());
		this.setValue(gedcomXSourceCitation.getValue());
		this.setCitationTemplate(gedcomXSourceCitation.getCitationTemplate());
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) {
		final org.gedcomx.source.SourceCitation gedcomXSourceCitation = (org.gedcomx.source.SourceCitation) gedcomXObject;

		if (gedcomXSourceCitation.getFields() != null) {
			for (final org.gedcomx.source.CitationField field : gedcomXSourceCitation
					.getFields()) {
				this.addField(new CitationField(field));
			}
		}
	}

	public void setLang(final String lang) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.LANG, lang);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.setValue((String) properties[0]);
	}

	public void setValue(final String value) {
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
