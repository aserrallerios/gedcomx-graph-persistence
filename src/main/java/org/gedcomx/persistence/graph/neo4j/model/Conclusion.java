package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.types.ConfidenceLevel;
import org.neo4j.graphdb.Node;

public abstract class Conclusion extends NodeWrapper {

	public enum ConclusionProperties implements NodeProperties {

		ID, CONFIDENCE(true, IndexNodeNames.TYPES), TEXT(true, IndexNodeNames.OTHER), LATITUDE, LONGITUDE, TEMPORAL_DESCRIPTION_ORIGINAL, SPATIAL_DESCRIPTION, TEMPORAL_DESCRIPTION_FORMAL, ORIGINAL, DATE_ORIGINAL, DATE_FORMAL, PREFERRED, FULL_TEXT, QUALIFIERS, DETAILS, LIVING, PERSON_REFERENCE;

		private final boolean indexed;
		private final IndexNodeNames indexName;

		private ConclusionProperties() {
			this.indexed = false;
			this.indexName = null;
		}

		private ConclusionProperties(final boolean indexed, final IndexNodeNames indexName) {
			this.indexed = indexed;
			this.indexName = indexName;
		}

		@Override
		public IndexNodeNames getIndexName() {
			return this.indexName;
		}

		@Override
		public boolean isIndexed() {
			return this.indexed;
		}

	}

	protected Conclusion(final Node node) throws MissingFieldException, UnknownNodeType {
		super(node);
	}

	public Conclusion(final Object... properties) throws MissingFieldException {
		super(properties);
	}

	public Conclusion(final org.gedcomx.conclusion.Conclusion gedcomXConclusion) throws MissingFieldException {
		super(gedcomXConclusion);
	}

	public void addNote(final Note note) {
		this.addRelationship(WrapperRelTypes.HAS_NOTE, note);
	}

	public void addSourceReference(final SourceReference sourceReference) {
		this.addRelationship(WrapperRelTypes.HAS_SOURCE_REFERENCE, sourceReference);
	}

	protected abstract void deleteAllConcreteReferences();

	@Override
	protected void deleteAllReferences() {
		this.deleteReferencedNode(this.getAttribution());
		this.deleteReferencedNodes(this.getNotes());
		this.deleteReferencedNodes(this.getSourceReferences());
		this.deleteAllConcreteReferences();
	}

	public Attribution getAttribution() {
		return this.getNodeByRelationship(Attribution.class, WrapperRelTypes.ATTRIBUTION);
	}

	public URI getConfidence() {
		return new URI((String) this.getProperty(ConclusionProperties.CONFIDENCE));
	}

	protected org.gedcomx.conclusion.Conclusion getGedcomXConclusion(final org.gedcomx.conclusion.Conclusion gedcomXConclusion) {
		gedcomXConclusion.setId(this.getId());
		gedcomXConclusion.setLang(this.getLang());
		gedcomXConclusion.setConfidence(this.getConfidence());
		gedcomXConclusion.setKnownConfidenceLevel(this.getKnownConfidenceLevel());

		gedcomXConclusion.setAttribution(this.getAttribution().getGedcomX());
		gedcomXConclusion.setNotes(this.getGedcomXList(org.gedcomx.common.Note.class, this.getNotes()));
		gedcomXConclusion.setSources(this.getGedcomXList(org.gedcomx.source.SourceReference.class, this.getSourceReferences()));

		return gedcomXConclusion;
	}

	public String getId() {
		return (String) this.getProperty(GenericProperties.ID);
	}

	public ConfidenceLevel getKnownConfidenceLevel() {
		return ConfidenceLevel.fromQNameURI(this.getConfidence());
	}

	public String getLang() {
		return (String) this.getProperty(GenericProperties.LANG);
	}

	public List<Note> getNotes() {
		return this.getNodesByRelationship(Note.class, WrapperRelTypes.HAS_NOTE);
	}

	public List<SourceReference> getSourceReferences() {
		return this.getNodesByRelationship(SourceReference.class, WrapperRelTypes.HAS_SOURCE_REFERENCE);
	}

	public void setAttribution(final Attribution attribution) {
		this.createRelationship(WrapperRelTypes.ATTRIBUTION, attribution);
	}

	public void setConfidence(final URI confidence) {
		this.setProperty(ConclusionProperties.CONFIDENCE, confidence);
	}

	protected abstract void setGedcomXConcreteProperties(Object gedcomXObject);

	protected abstract void setGedcomXConcreteRelations(Object gedcomXObject) throws MissingFieldException;

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Conclusion gedcomXConclusion = (org.gedcomx.conclusion.Conclusion) gedcomXObject;
		this.setId(gedcomXConclusion.getId());
		this.setLang(gedcomXConclusion.getLang());
		this.setConfidence(gedcomXConclusion.getConfidence());

		this.setGedcomXConcreteProperties(gedcomXObject);
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.Conclusion gedcomXConclusion = (org.gedcomx.conclusion.Conclusion) gedcomXObject;

		for (final org.gedcomx.common.Note gedcomXNote : gedcomXConclusion.getNotes()) {
			this.addNote(new Note(gedcomXNote));
		}
		for (final org.gedcomx.source.SourceReference gedcomXSourceReference : gedcomXConclusion.getSources()) {
			this.addSourceReference(new SourceReference(gedcomXSourceReference));
		}

		this.setAttribution(new Attribution(gedcomXConclusion.getAttribution()));
		this.setGedcomXConcreteRelations(gedcomXObject);
	}

	public void setId(final String id) {
		this.setProperty(GenericProperties.ID, id);
	}

	public void setKnownConfidenceLevel(final ConfidenceLevel confidence) {
		this.setConfidence(confidence.toQNameURI());
	}

	public void setLang(final String lang) {
		this.setProperty(GenericProperties.LANG, lang);
	}

}
