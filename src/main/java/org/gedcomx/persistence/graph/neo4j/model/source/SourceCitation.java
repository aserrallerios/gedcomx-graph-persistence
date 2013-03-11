package org.gedcomx.persistence.graph.neo4j.model.source;

import java.util.LinkedList;
import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.model.GENgraphNode;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.utils.RelTypes;
import org.neo4j.graphdb.Node;

public class SourceCitation extends GENgraphNode {

	private final List<CitationField> fields = new LinkedList<>();

	protected SourceCitation(final Node node) throws WrongNodeType {
		super(NodeTypes.SOURCE_CITATION, node);
	}

	protected SourceCitation(final org.gedcomx.source.SourceCitation gedcomXSourceCitation) throws MissingFieldException {
		super(NodeTypes.SOURCE_CITATION, gedcomXSourceCitation);
	}

	public SourceCitation(final String value) {
		super(NodeTypes.SOURCE_CITATION, new Object[] { value });
	}

	public void addField(final CitationField citationField) {
		this.fields.add(citationField);
		this.createRelationship(RelTypes.HAS_CITATION_FIELD, citationField);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.source.SourceCitation gedcomXSourceCitation = (org.gedcomx.source.SourceCitation) gedcomXObject;

		if ((gedcomXSourceCitation.getValue() == null) || gedcomXSourceCitation.getValue().isEmpty()) {
			throw new MissingRequiredPropertyException(SourceCitation.class, NodeProperties.Generic.VALUE);
		}

		return;
	}

	public ResourceReference getCitationTemplate() {
		return new ResourceReference(new URI((String) this.getProperty(NodeProperties.SourceDescription.CITATION_TEMPLATE)));
	}

	public List<CitationField> getFields() {
		return this.fields;
	}

	public String getLang(final String lang) {
		return (String) this.getProperty(NodeProperties.Generic.LANG);

	}

	public String getValue() {
		return (String) this.getProperty(NodeProperties.Generic.VALUE);

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
			this.addField(new CitationField(this.getGraph(), field));
		}
	}

	public void setLang(final String lang) {
		this.setProperty(NodeProperties.Generic.LANG, lang);

	}

	public void setValue(final String value) {
		this.setProperty(NodeProperties.Generic.VALUE, value);

	}

}
