package org.gedcomx.persistence.graph.neo4j.model.source;

import java.util.Date;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.model.GENgraph;
import org.gedcomx.persistence.graph.neo4j.model.GENgraphNode;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.utils.RelTypes;

public class SourceReference extends GENgraphNode {

	private SourceDescription description;
	private URI descriptionURI;

	public SourceReference(final GENgraph graph, final org.gedcomx.source.SourceReference gedcomXSourceReference)
			throws MissingFieldException {
		super(NodeTypes.SOURCE_REFERENCE, gedcomXSourceReference);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.source.SourceReference gedcomXSourceReference = (org.gedcomx.source.SourceReference) gedcomXObject;
		if (gedcomXSourceReference.getDescriptionRef() == null) {
			throw new MissingRequiredRelationshipException(SourceReference.class, RelTypes.DESCRIPTION);
		}
	}

	public String getAttributionChangeMessage() {
		return (String) this.getProperty(NodeProperties.Generic.ATTRIBUTION_CHANGE_MESSAGE);
	}

	public Date getAttributionModifiedConfidence() {
		return new Date((Long) this.getProperty(NodeProperties.Generic.ATTRIBUTION_MODIFIED));
	}

	public SourceDescription getDescription() {
		return this.description;
	}

	@Override
	protected void resolveReferences() {
		if ((this.description == null) && (this.descriptionURI != null)) {
			final SourceDescription source = this.getGraph().getSource(this.descriptionURI);
			if (source != null) {
				this.setDescription(source);
			}
		}
	}

	public void setAttributionChangeMessage(final String changeMessage) {
		this.setProperty(NodeProperties.Generic.ATTRIBUTION_CHANGE_MESSAGE, changeMessage);

	}

	public void setAttributionModifiedConfidence(final Date modified) {
		this.setProperty(NodeProperties.Generic.ATTRIBUTION_MODIFIED, modified.getTime());
	}

	public void setDescription(final SourceDescription description) {
		this.description = description;
		this.createRelationship(RelTypes.DESCRIPTION, description);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.source.SourceReference gedcomXSourceReference = (org.gedcomx.source.SourceReference) gedcomXObject;

		if (gedcomXSourceReference.getAttribution() != null) {
			this.setAttributionModifiedConfidence(gedcomXSourceReference.getAttribution().getModified());
			this.setAttributionChangeMessage(gedcomXSourceReference.getAttribution().getChangeMessage());
		}

	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.source.SourceReference gedcomXSourceReference = (org.gedcomx.source.SourceReference) gedcomXObject;

		this.descriptionURI = gedcomXSourceReference.getDescriptionRef();
		final SourceDescription source = this.getGraph().getSource(this.descriptionURI);
		if (source != null) {
			this.setDescription(source);
		} else {
			this.addNodeToResolveReferences();
		}
	}
}
