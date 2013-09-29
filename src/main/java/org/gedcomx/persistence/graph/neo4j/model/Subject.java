package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.neo4j.graphdb.Node;

public abstract class Subject extends Conclusion {

	protected Subject(final Node node) {
		super(node);
	}

	protected Subject(final Object... properties) {
		super(properties);
	}

	protected Subject(final org.gedcomx.conclusion.Subject gedcomXSubject) {
		super(gedcomXSubject);
	}

	private EvidenceReference addEvidence(final EvidenceReference evidence) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_EVIDENCE, evidence);
		return evidence;
	}

	public EvidenceReference addEvidence(final ResourceReference reference) {
		return this.addEvidence(new EvidenceReference(reference));
	}

	private Identifier addIdentifier(final Identifier identifier) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_IDENTIFIER, identifier);
		return identifier;
	}

	public Identifier addIdentifier(final URI uri) {
		return this.addIdentifier(new Identifier(uri));
	}

	private SourceReference addMedia(final SourceReference media) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_MEDIA, media);
		return media;
	}

	public SourceReference addMedia(final String citation) {
		return this.addMedia(new SourceReference(citation));
	}

	@Override
	protected void deleteAllReferences() {
		NodeWrapper.nodeWrapperOperations
				.deleteReferencedNodes(this.getMedia());
		NodeWrapper.nodeWrapperOperations.deleteReferencedNodes(this
				.getIdentifiers());
		super.deleteAllReferences();
	}

	public List<EvidenceReference> getEvidences() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				EvidenceReference.class, RelationshipTypes.HAS_EVIDENCE);
	}

	public Boolean getExtracted() {
		return (Boolean) NodeWrapper.nodeWrapperOperations.getProperty(this,
				ConclusionProperties.EXTRACTED);
	}

	@Override
	protected org.gedcomx.conclusion.Conclusion getGedcomXConclusion(
			final org.gedcomx.conclusion.Conclusion gedcomXConclusion) {
		final org.gedcomx.conclusion.Subject gedcomXSubject = (org.gedcomx.conclusion.Subject) gedcomXConclusion;

		gedcomXSubject.setExtracted(this.getExtracted());

		gedcomXSubject.setMedia(NodeWrapper.nodeWrapperOperations
				.getGedcomXList(org.gedcomx.source.SourceReference.class,
						this.getMedia()));
		gedcomXSubject.setIdentifiers(NodeWrapper.nodeWrapperOperations
				.getGedcomXList(org.gedcomx.conclusion.Identifier.class,
						this.getIdentifiers()));
		gedcomXSubject.setEvidence(NodeWrapper.nodeWrapperOperations
				.getGedcomXList(org.gedcomx.common.EvidenceReference.class,
						this.getEvidences()));

		super.getGedcomXConclusion(gedcomXSubject);

		return gedcomXSubject;
	}

	public List<Identifier> getIdentifiers() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				Identifier.class, RelationshipTypes.HAS_IDENTIFIER);
	}

	public List<SourceReference> getMedia() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				SourceReference.class, RelationshipTypes.HAS_MEDIA);
	}

	public void setExtracted(final Boolean extracted) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				ConclusionProperties.EXTRACTED, extracted);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Subject gedcomXSubject = (org.gedcomx.conclusion.Subject) gedcomXObject;
		this.setExtracted(gedcomXSubject.getExtracted());
		super.setGedcomXProperties(gedcomXObject);
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Subject gedcomXSubject = (org.gedcomx.conclusion.Subject) gedcomXObject;
		if (gedcomXSubject.getMedia() != null) {
			for (final org.gedcomx.source.SourceReference gedcomXsourceRef : gedcomXSubject
					.getMedia()) {
				this.addMedia(new SourceReference(gedcomXsourceRef));
			}
		}
		if (gedcomXSubject.getIdentifiers() != null) {
			for (final org.gedcomx.conclusion.Identifier gedcomXIdentifier : gedcomXSubject
					.getIdentifiers()) {
				this.addIdentifier(new Identifier(gedcomXIdentifier));
			}
		}
		if (gedcomXSubject.getEvidence() != null) {
			for (final org.gedcomx.common.EvidenceReference gedcomXEvidence : gedcomXSubject
					.getEvidence()) {
				this.addEvidence(new EvidenceReference(gedcomXEvidence));
			}
		}
		super.setGedcomXRelations(gedcomXObject);
	}

}
