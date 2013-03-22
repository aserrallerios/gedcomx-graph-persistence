package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.SourceDescription.SourceProperties;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.neo4j.graphdb.Node;

@NodeType("SOURCE_REFERENCE")
public class SourceReference extends NodeWrapper {

	public SourceReference(final Node node) throws MissingFieldException, UnknownNodeType {
		super(node);
	}

	public SourceReference(final org.gedcomx.source.SourceReference gedcomXSourceReference) throws MissingFieldException {
		super(gedcomXSourceReference);
	}

	public SourceReference(final SourceDescription description) throws MissingFieldException {
		super(new Object[] { description });
	}

	@Override
	protected void deleteAllReferences() {

	}

	public Attribution getAttribution() {
		return this.getNodeByRelationship(Attribution.class, RelTypes.ATTRIBUTION);
	}

	public SourceDescription getDescription() {
		return this.getNodeByRelationship(SourceDescription.class, RelTypes.DESCRIPTION);
	}

	@Override
	protected org.gedcomx.source.SourceReference getGedcomX() {
		final org.gedcomx.source.SourceReference gedcomXSourceReference = new org.gedcomx.source.SourceReference();

		final Attribution attr = this.getAttribution();
		if (attr != null) {
			gedcomXSourceReference.setAttribution(attr.getGedcomX());
		}
		gedcomXSourceReference.setDescriptionRef(this.getDescription().getURI());

		return gedcomXSourceReference;
	}

	public NodeWrapper getParentNode() {
		return this.getParentNode(RelTypes.HAS_SOURCE_REFERENCE);
	}

	@Override
	protected void resolveReferences() {
		this.createReferenceRelationship(RelTypes.DESCRIPTION, SourceProperties.SOURCE_DESCRIPTION_REFERENCE);
	}

	public void setAttribution(final Attribution attribution) {
		this.createRelationship(RelTypes.ATTRIBUTION, attribution);
	}

	public void setDescription(final SourceDescription description) {
		this.createReferenceRelationship(RelTypes.DESCRIPTION, description);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		return;
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.source.SourceReference gedcomXSourceReference = (org.gedcomx.source.SourceReference) gedcomXObject;

		this.setAttribution(new Attribution(gedcomXSourceReference.getAttribution()));

		this.setProperty(SourceProperties.SOURCE_DESCRIPTION_REFERENCE, gedcomXSourceReference.getDescriptionRef());
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.setDescription((SourceDescription) properties[0]);
	}

	@Override
	protected void validateUnderlyingNode() throws MissingFieldException {
		if (ValidationTools.nullOrEmpty(this.getDescription())) {
			throw new MissingRequiredRelationshipException(SourceReference.class, RelTypes.DESCRIPTION.toString());
		}
	}
}
