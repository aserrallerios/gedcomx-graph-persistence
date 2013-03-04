package org.gedcomx.graph.persistence.neo4j.embeded.model.source;

import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredPropertyException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;

public class CitationField extends GENgraphNode {

	protected CitationField(final GENgraph graph, final org.gedcomx.source.CitationField gedcomXCitationField) throws MissingFieldException {
		super(graph, NodeTypes.CITATION_FIELD, gedcomXCitationField);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.source.CitationField gedcomXCitationField = (org.gedcomx.source.CitationField) gedcomXObject;

		if ((gedcomXCitationField.getValue() == null) || gedcomXCitationField.getValue().isEmpty()) {
			throw new MissingRequiredPropertyException(CitationField.class, NodeProperties.Generic.VALUE);
		}
		if ((gedcomXCitationField.getName() == null)) {
			throw new MissingRequiredPropertyException(CitationField.class, NodeProperties.SourceDescription.NAME);
		}
	}

	public URI getName() {
		return new URI((String) this.getProperty(NodeProperties.SourceDescription.NAME));
	}

	public String getValue() {
		return (String) this.getProperty(NodeProperties.Generic.VALUE);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.source.CitationField gedcomXCitationField = (org.gedcomx.source.CitationField) gedcomXObject;

		this.setName(gedcomXCitationField.getName());
		this.setValue(gedcomXCitationField.getValue());
	}

	public void setName(final URI name) {
		this.setProperty(NodeProperties.SourceDescription.NAME, name);
	}

	public void setValue(final String value) {
		this.setProperty(NodeProperties.Generic.VALUE, value);
	}

}
