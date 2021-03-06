package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.SourceProperties;
import org.gedcomx.persistence.graph.neo4j.utils.Validation;
import org.neo4j.graphdb.Node;

@NodeType(NodeTypes.SOURCE_REFERENCE)
public class SourceReference extends NodeWrapper {

	protected SourceReference(final Node node) {
		super(node);
	}

	protected SourceReference(
			final org.gedcomx.source.SourceReference gedcomXSourceReference) {
		super(gedcomXSourceReference);
	}

	protected SourceReference(final String citation) {
		super(new Object[] { citation });
	}

	@Override
	protected void deleteAllReferences() {

	}

	public Attribution getAttribution() {
		return NodeWrapper.nodeWrapperOperations.getNodeByRelationship(this,
				Attribution.class, RelationshipTypes.ATTRIBUTION);
	}

	public SourceDescription getDescription() {
		return NodeWrapper.nodeWrapperOperations.getNodeByRelationship(this,
				SourceDescription.class, RelationshipTypes.DESCRIPTION);
	}

	@Override
	public org.gedcomx.source.SourceReference getGedcomX() {
		final org.gedcomx.source.SourceReference gedcomXSourceReference = new org.gedcomx.source.SourceReference();

		final Attribution attr = this.getAttribution();
		if (attr != null) {
			gedcomXSourceReference.setAttribution(attr.getGedcomX());
		}
		gedcomXSourceReference
				.setDescriptionRef(this.getDescription().getURI());

		return gedcomXSourceReference;
	}

	@Override
	public NodeWrapper getParentNode() {
		return NodeWrapper.nodeWrapperOperations.getParentNode(this,
				RelationshipTypes.HAS_SOURCE_REFERENCE);
	}

	@Override
	public void resolveReferences() {
		NodeWrapper.nodeWrapperOperations.createReferenceRelationship(this,
				RelationshipTypes.DESCRIPTION,
				SourceProperties.SOURCE_DESCRIPTION_REFERENCE);
	}

	public Attribution setAttribution() {
		return this.setAttribution(new Attribution());
	}

	private Attribution setAttribution(final Attribution attribution) {
		NodeWrapper.nodeWrapperOperations.createRelationship(this,
				RelationshipTypes.ATTRIBUTION, attribution);
		return attribution;
	}

	public void setDescription(final SourceDescription description) {
		NodeWrapper.nodeWrapperOperations.createReferenceRelationship(this,
				RelationshipTypes.DESCRIPTION, description);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		return;
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) {
		final org.gedcomx.source.SourceReference gedcomXSourceReference = (org.gedcomx.source.SourceReference) gedcomXObject;

		if (gedcomXSourceReference.getAttribution() != null) {
			this.setAttribution(new Attribution(gedcomXSourceReference
					.getAttribution()));
		}

		NodeWrapper.nodeWrapperOperations.setProperty(this,
				SourceProperties.SOURCE_DESCRIPTION_REFERENCE,
				gedcomXSourceReference.getDescriptionRef());
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.setDescription(new SourceDescription((String) properties[0]));
	}

	@Override
	protected void validateUnderlyingNode() {
		if (Validation.nullOrEmpty(this.getDescription())) {
			throw new MissingRequiredRelationshipException(
					NodeWrapper.nodeWrapperOperations
							.getAnnotatedNodeType(this),
					RelationshipTypes.DESCRIPTION.toString());
		}
	}
}
