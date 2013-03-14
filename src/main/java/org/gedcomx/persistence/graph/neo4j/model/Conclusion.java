package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.neo4j.graphdb.Node;

public abstract class Conclusion extends NodeWrapper {

	public Conclusion() throws MissingFieldException {
		super();
	}

	protected Conclusion(final Node node) throws MissingFieldException, WrongNodeType {
		super(node);
	}

	public Conclusion(final org.gedcomx.conclusion.Conclusion gedcomXConclusion) throws MissingFieldException {
		super(gedcomXConclusion);
	}

	public void addNote(final Note note) {
		this.addRelationship(GENgraphRelTypes.HAS_NOTE, note);
	}

	public void addSourceReference(final SourceReference sourceReference) {
		this.addRelationship(GENgraphRelTypes.HAS_SOURCE_REFERENCE, sourceReference);
	}

	public Attribution getAttribution() {
		return this.getNodeByRelationship(Attribution.class, GENgraphRelTypes.ATTRIBUTION);
	}

	public URI getConfidence() {
		return new URI((String) this.getProperty(NodeProperties.Conclusion.CONFIDENCE));
	}

	public String getId() {
		return (String) this.getProperty(NodeProperties.Generic.ID);
	}

	public String getLang() {
		return (String) this.getProperty(NodeProperties.Generic.LANG);
	}

	public List<Note> getNotes() {
		return this.getNodesByRelationship(Note.class, GENgraphRelTypes.HAS_NOTE);
	}

	public List<SourceReference> getSourceReferences() {
		return this.getNodesByRelationship(SourceReference.class, GENgraphRelTypes.HAS_SOURCE_REFERENCE);
	}

	public void setAttribution(final Attribution attribution) {
		this.createRelationship(GENgraphRelTypes.ATTRIBUTION, attribution);
	}

	public void setConfidence(final URI confidence) {
		this.setProperty(NodeProperties.Conclusion.CONFIDENCE, confidence);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Conclusion gedcomXConclusion = (org.gedcomx.conclusion.Conclusion) gedcomXObject;
		this.setId(gedcomXConclusion.getId());
		this.setLang(gedcomXConclusion.getLang());
		this.setConfidence(gedcomXConclusion.getConfidence());
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

	}

	public void setId(final String id) {
		this.setProperty(NodeProperties.Generic.ID, id);
	}

	public void setLang(final String lang) {
		this.setProperty(NodeProperties.Generic.LANG, lang);
	}

}
