package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.common.Note;
import org.gedcomx.graph.persistence.neo4j.embeded.model.source.SourceReference;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.RelTypes;

public class Conclusion extends GENgraphNode {

	private ConclusionSubnode subnode;
	private final List<SourceReference> sourceReferences;
	private final List<Note> notes;

	public Conclusion(final GENgraph graf, final org.gedcomx.conclusion.Conclusion gedcomXConclusion) throws MissingFieldException {
		super(graf, NodeTypes.CONCLUSION, gedcomXConclusion);

		this.sourceReferences = new LinkedList<>();
		this.notes = new LinkedList<>();

		if (gedcomXConclusion instanceof org.gedcomx.conclusion.Document) {
			this.subnode = new Document(graf, (org.gedcomx.conclusion.Document) gedcomXConclusion);
		}
		if (gedcomXConclusion instanceof org.gedcomx.conclusion.PlaceDescription) {
			this.subnode = new PlaceDescription(graf, (org.gedcomx.conclusion.PlaceDescription) gedcomXConclusion);
		}
		if (gedcomXConclusion instanceof org.gedcomx.conclusion.Name) {
			this.subnode = new Name(graf, (org.gedcomx.conclusion.Name) gedcomXConclusion);
		}
		if (gedcomXConclusion instanceof org.gedcomx.conclusion.Fact) {
			this.subnode = new Fact(graf, (org.gedcomx.conclusion.Fact) gedcomXConclusion);
		}
		if (gedcomXConclusion instanceof org.gedcomx.conclusion.Event) {
			this.subnode = new Event(graf, (org.gedcomx.conclusion.Event) gedcomXConclusion);
		}
		if (gedcomXConclusion instanceof org.gedcomx.conclusion.EventRole) {
			this.subnode = new EventRole(graf, (org.gedcomx.conclusion.EventRole) gedcomXConclusion);
		}
		if (gedcomXConclusion instanceof org.gedcomx.conclusion.Gender) {
			this.subnode = new Gender(graf, (org.gedcomx.conclusion.Gender) gedcomXConclusion);
		}
		if (gedcomXConclusion instanceof org.gedcomx.conclusion.Person) {
			this.subnode = new Person(graf, (org.gedcomx.conclusion.Person) gedcomXConclusion);
		}
		if (gedcomXConclusion instanceof org.gedcomx.conclusion.Relationship) {
			this.subnode = new Relationship(graf, (org.gedcomx.conclusion.Relationship) gedcomXConclusion);
		} else {
			// TODO
		}
		this.createRelationship(RelTypes.IS_A, this.subnode);

		for (final org.gedcomx.common.Note gedcomXNote : gedcomXConclusion.getNotes()) {
			this.addNote(new Note(graf, gedcomXNote));
		}
		for (final org.gedcomx.source.SourceReference gedcomXSourceReference : gedcomXConclusion.getSources()) {
			this.addSourceReference(new SourceReference(graf, gedcomXSourceReference));
		}
	}

	public void addNote(final Note note) {
		this.notes.add(note);
		this.createRelationship(RelTypes.HAS_NOTE, note);
	}

	public void addSourceReference(final SourceReference sourceReference) {
		this.sourceReferences.add(sourceReference);
		this.createRelationship(RelTypes.HAS_SOURCE_REFERENCE, sourceReference);
	}

	public String getAttributionChangeMessage() {
		return (String) this.getProperty(NodeProperties.Generic.ATTRIBUTION_CHANGE_MESSAGE);

	}

	public Date getAttributionModifiedConfidence() {
		return new Date((Long) this.getProperty(NodeProperties.Generic.ATTRIBUTION_MODIFIED));
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
		return this.notes;
	}

	public List<SourceReference> getSourceReferences() {
		return this.sourceReferences;
	}

	public ConclusionSubnode getSubnode() {
		return this.subnode;
	}

	public void setAttributionChangeMessage(final String changeMessage) {
		this.setProperty(NodeProperties.Generic.ATTRIBUTION_CHANGE_MESSAGE, changeMessage);

	}

	public void setAttributionModifiedConfidence(final Date modified) {
		this.setProperty(NodeProperties.Generic.ATTRIBUTION_MODIFIED, modified.getTime());
	}

	public void setConfidence(final URI confidence) {
		this.setProperty(NodeProperties.Conclusion.CONFIDENCE, confidence);
	}

	public void setId(final String id) {
		this.setProperty(NodeProperties.Generic.ID, id);
	}

	@Override
	protected void setInitialProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Conclusion gedcomXConclusion = (org.gedcomx.conclusion.Conclusion) gedcomXObject;
		this.setId(gedcomXConclusion.getId());
		this.setLang(gedcomXConclusion.getLang());
		this.setConfidence(gedcomXConclusion.getConfidence());
		if (gedcomXConclusion.getAttribution() != null) {
			this.setAttributionModifiedConfidence(gedcomXConclusion.getAttribution().getModified());
			this.setAttributionChangeMessage(gedcomXConclusion.getAttribution().getChangeMessage());
		}
	}

	public void setLang(final String lang) {
		this.setProperty(NodeProperties.Generic.LANG, lang);
	}

	public void setSubnode(final ConclusionSubnode subnode) {
		this.subnode = subnode;
	}

}
