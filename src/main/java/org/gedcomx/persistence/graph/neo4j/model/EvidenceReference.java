package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.utils.Validation;
import org.neo4j.graphdb.Node;

public class EvidenceReference extends NodeWrapper {
	// TODO: reference is supposed to poit to a Subject. Can we replace the
	// ResourceReference to a Subject's "lazy" reference?

	protected EvidenceReference(final Node Node) {
		super(Node);
	}

	protected EvidenceReference(
			final org.gedcomx.common.EvidenceReference gedcomXEvidenceReference) {
		super(gedcomXEvidenceReference);
	}

	protected EvidenceReference(final ResourceReference reference) {
		super(new Object[] { reference });
	}

	@Override
	protected void deleteAllReferences() {
		this.getAttribution().delete();
	}

	public Attribution getAttribution() {
		return NodeWrapper.nodeWrapperOperations.getNodeByRelationship(this,
				Attribution.class, RelationshipTypes.ATTRIBUTION);
	}

	@Override
	public org.gedcomx.common.EvidenceReference getGedcomX() {
		final org.gedcomx.common.EvidenceReference gedcomXEvidenceReference = new org.gedcomx.common.EvidenceReference();

		gedcomXEvidenceReference.setAttribution(this.getAttribution()
				.getGedcomX());
		gedcomXEvidenceReference.setResource(this.getReference().getResource());
		gedcomXEvidenceReference.setResourceId(this.getReference()
				.getResourceId());

		return gedcomXEvidenceReference;
	}

	@Override
	public NodeWrapper getParentNode() {
		return NodeWrapper.nodeWrapperOperations.getParentNode(this,
				RelationshipTypes.HAS_EVIDENCE);
	}

	public ResourceReference getReference() {
		return new ResourceReference(new URI(
				(String) NodeWrapper.nodeWrapperOperations.getProperty(this,
						GenericProperties.EVIDENCE_REFERENCE)));
	}

	@Override
	public void resolveReferences() {
		return;
	}

	public Attribution setAttribution() {
		return this.setAttribution(new Attribution());
	}

	private Attribution setAttribution(final Attribution attribution) {
		NodeWrapper.nodeWrapperOperations.createRelationship(this,
				RelationshipTypes.ATTRIBUTION, attribution);
		return attribution;
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.common.EvidenceReference gedcomXEvidenceReference = (org.gedcomx.common.EvidenceReference) gedcomXObject;
		this.setReference(new ResourceReference(gedcomXEvidenceReference
				.getResource(), gedcomXEvidenceReference.getResourceId()));
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) {
		final org.gedcomx.common.EvidenceReference gedcomXEvidenceReference = (org.gedcomx.common.EvidenceReference) gedcomXObject;
		if (gedcomXEvidenceReference.getAttribution() != null) {
			this.setAttribution(new Attribution(gedcomXEvidenceReference
					.getAttribution()));
		}
	}

	public void setReference(final ResourceReference reference) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.EVIDENCE_REFERENCE, reference);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.setReference((ResourceReference) properties[0]);

	}

	@Override
	protected void validateUnderlyingNode() {
		if (Validation.nullOrEmpty(this.getReference())) {
			throw new MissingRequiredPropertyException(
					NodeWrapper.nodeWrapperOperations
							.getAnnotatedNodeType(this),
					GenericProperties.EVIDENCE_REFERENCE.toString());
		}
	}
}
