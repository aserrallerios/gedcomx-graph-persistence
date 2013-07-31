package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.types.ConfidenceLevel;
import org.neo4j.graphdb.Node;

public abstract class Conclusion extends NodeWrapper {

	protected Conclusion(final Node node) {
		super(node);
	}

	protected Conclusion(final Object... properties) {
		super(properties);
	}

	protected Conclusion(
			final org.gedcomx.conclusion.Conclusion gedcomXConclusion) {
		super(gedcomXConclusion);
	}

	public void addNote(final Note note) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_NOTE, note);
	}

	public void addSourceReference(final SourceReference sourceReference) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_SOURCE_REFERENCE, sourceReference);
	}

	protected abstract void deleteAllConcreteReferences();

	@Override
	protected void deleteAllReferences() {
		NodeWrapper.nodeWrapperOperations.deleteReferencedNode(this
				.getAttribution());
		NodeWrapper.nodeWrapperOperations
				.deleteReferencedNodes(this.getNotes());
		NodeWrapper.nodeWrapperOperations.deleteReferencedNodes(this
				.getSourceReferences());
		this.deleteAllConcreteReferences();
	}

	public Attribution getAttribution() {
		return NodeWrapper.nodeWrapperOperations.getNodeByRelationship(this,
				Attribution.class, RelationshipTypes.ATTRIBUTION);
	}

	@Deprecated
	public URI getConfidence() {
		return new URI((String) NodeWrapper.nodeWrapperOperations.getProperty(
				this, ConclusionProperties.CONFIDENCE));
	}

	protected org.gedcomx.conclusion.Conclusion getGedcomXConclusion(
			final org.gedcomx.conclusion.Conclusion gedcomXConclusion) {
		gedcomXConclusion.setId(this.getId());
		gedcomXConclusion.setLang(this.getLang());
		gedcomXConclusion.setConfidence(this.getConfidence());
		gedcomXConclusion.setKnownConfidenceLevel(this
				.getKnownConfidenceLevel());

		gedcomXConclusion.setAttribution(this.getAttribution().getGedcomX());
		gedcomXConclusion
				.setNotes(NodeWrapper.nodeWrapperOperations.getGedcomXList(
						org.gedcomx.common.Note.class, this.getNotes()));
		gedcomXConclusion.setSources(NodeWrapper.nodeWrapperOperations
				.getGedcomXList(org.gedcomx.source.SourceReference.class,
						this.getSourceReferences()));

		return gedcomXConclusion;
	}

	public String getId() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				GenericProperties.ID);
	}

	public ConfidenceLevel getKnownConfidenceLevel() {
		return ConfidenceLevel.fromQNameURI(this.getConfidence());
	}

	public String getLang() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				GenericProperties.LANG);
	}

	public List<Note> getNotes() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				Note.class, RelationshipTypes.HAS_NOTE);
	}

	@Override
	protected ResourceReference getResourceReference() {
		return new ResourceReference(new URI(this.getId()));
	}

	public List<SourceReference> getSourceReferences() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				SourceReference.class, RelationshipTypes.HAS_SOURCE_REFERENCE);
	}

	@Override
	protected URI getURI() {
		return new URI(this.getId());
	}

	protected abstract void resolveConcreteReferences();

	@Override
	public void resolveReferences() {
		final Attribution a = this.getAttribution();
		if (a != null) {
			this.getAttribution().resolveReferences();
		}
		for (final SourceReference s : this.getSourceReferences()) {
			s.resolveReferences();
		}
		this.resolveConcreteReferences();
	}

	public void setAttribution(final Attribution attribution) {
		NodeWrapper.nodeWrapperOperations.createRelationship(this,
				RelationshipTypes.ATTRIBUTION, attribution);
	}

	@Deprecated
	public void setConfidence(final URI confidence) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				ConclusionProperties.CONFIDENCE, confidence);
	}

	protected abstract void setGedcomXConcreteProperties(Object gedcomXObject);

	protected abstract void setGedcomXConcreteRelations(Object gedcomXObject);

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Conclusion gedcomXConclusion = (org.gedcomx.conclusion.Conclusion) gedcomXObject;
		this.setId(gedcomXConclusion.getId());
		this.setLang(gedcomXConclusion.getLang());
		this.setConfidence(gedcomXConclusion.getConfidence());

		this.setGedcomXConcreteProperties(gedcomXObject);
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Conclusion gedcomXConclusion = (org.gedcomx.conclusion.Conclusion) gedcomXObject;

		if (gedcomXConclusion.getNotes() != null) {
			for (final org.gedcomx.common.Note gedcomXNote : gedcomXConclusion
					.getNotes()) {
				this.addNote(new Note(gedcomXNote));
			}
		}
		if (gedcomXConclusion.getSources() != null) {
			for (final org.gedcomx.source.SourceReference gedcomXSourceReference : gedcomXConclusion
					.getSources()) {
				this.addSourceReference(new SourceReference(
						gedcomXSourceReference));
			}
		}

		if (gedcomXConclusion.getAttribution() != null) {
			this.setAttribution(new Attribution(gedcomXConclusion
					.getAttribution()));
		}

		this.setGedcomXConcreteRelations(gedcomXObject);
	}

	public void setId(final String id) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.ID, id);
	}

	public void setKnownConfidenceLevel(final ConfidenceLevel confidence) {
		this.setConfidence(confidence.toQNameURI());
	}

	public void setLang(final String lang) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.LANG, lang);
	}

}
