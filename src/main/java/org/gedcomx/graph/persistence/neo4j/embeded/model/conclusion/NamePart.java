package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredPropertyException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;

public class NamePart extends GENgraphNode {

	protected NamePart(final GENgraph graph, final org.gedcomx.conclusion.NamePart gedcomXNamePart) throws MissingFieldException {
		super(graph, NodeTypes.NAME_PART, gedcomXNamePart);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.NamePart gedcomXNamePart = (org.gedcomx.conclusion.NamePart) gedcomXObject;

		if ((gedcomXNamePart.getValue() == null) || gedcomXNamePart.getValue().isEmpty()) {
			throw new MissingRequiredPropertyException(NamePart.class, NodeProperties.Generic.VALUE);
		}
	}

	public List<ResourceReference> getQualifiers(final List<ResourceReference> qualifiers) {
		return this.getURIListProperties(NodeProperties.Conclusion.QUALIFIERS);
	}

	public URI getType(final URI type) {
		return new URI((String) this.getProperty(NodeProperties.Generic.TYPE));
	}

	public String getValue(final String value) {
		return (String) this.getProperty(NodeProperties.Generic.VALUE);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.NamePart gedcomXNamePart = (org.gedcomx.conclusion.NamePart) gedcomXObject;

		this.setType(gedcomXNamePart.getType());
		this.setValue(gedcomXNamePart.getValue());
		this.setQualifiers(gedcomXNamePart.getQualifiers());

	}

	public void setQualifiers(final List<ResourceReference> qualifiers) {
		this.setURIListProperties(NodeProperties.Conclusion.QUALIFIERS, qualifiers);
	}

	public void setType(final URI type) {
		this.setProperty(NodeProperties.Generic.TYPE, type);
	}

	public void setValue(final String value) {
		this.setProperty(NodeProperties.Generic.VALUE, value);
	}

}
