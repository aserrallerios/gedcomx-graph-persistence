package org.gedcomx.graph.persistence.neo4j.embeded.model.source;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.gedcomx.common.URI;
import org.gedcomx.conclusion.Conclusion;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredRelationshipException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.common.Note;
import org.gedcomx.graph.persistence.neo4j.embeded.model.common.TextValue;
import org.gedcomx.graph.persistence.neo4j.embeded.model.contributor.Agent;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.RelTypes;

public class SourceDescription extends GENgraphNode {

	private final List<Note> notes;
	private final List<SourceCitation> sourceCitations;
	private final List<TextValue> titles;
	private final List<SourceReference> sources;
	private final List<Conclusion> extractedConclusions;

	private SourceReference componentOf;
	private Agent mediator;

	public SourceDescription(final GENgraph graph, final org.gedcomx.source.SourceDescription gedcomXSourceDescription)
			throws MissingFieldException {
		super(graph, NodeTypes.SOURCE_DESCRIPTION, gedcomXSourceDescription);

		this.notes = new LinkedList<>();
		this.sourceCitations = new LinkedList<>();
		this.titles = new LinkedList<>();
		this.sources = new LinkedList<>();
		this.extractedConclusions = new LinkedList<>();

		this.setComponentOf(new SourceReference(graph, gedcomXSourceDescription.getComponentOf()));

		// TODO
		// this.setMediator(new Agent(graph,
		// gedcomXSourceDescription.getMediator()));

		for (final org.gedcomx.common.Note note : gedcomXSourceDescription.getNotes()) {
			this.addNote(new Note(graph, note));
		}
		for (final org.gedcomx.common.TextValue title : gedcomXSourceDescription.getTitles()) {
			this.addTitle(new TextValue(graph, title));
		}
		for (final org.gedcomx.source.SourceCitation sourceCitation : gedcomXSourceDescription.getCitations()) {
			this.addSourceCitation(new SourceCitation(graph, sourceCitation));
		}
		for (final org.gedcomx.source.SourceReference source : gedcomXSourceDescription.getSources()) {
			this.addSource(new SourceReference(graph, source));
		}
	}

	public void addNote(final Note note) {
		this.notes.add(note);
		this.createRelationship(RelTypes.HAS_NOTE, note);
	}

	public void addSource(final SourceReference sourceReference) {
		this.sources.add(sourceReference);
		this.createRelationship(RelTypes.HAS_SOURCE_REFERENCE, sourceReference);
	}

	public void addSourceCitation(final SourceCitation sourceCitation) {
		this.sourceCitations.add(sourceCitation);
		this.createRelationship(RelTypes.HAS_CITATION, sourceCitation);
	}

	public void addTitle(final TextValue textValue) {
		this.titles.add(textValue);
		this.createRelationship(RelTypes.HAS_TITLE, textValue);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.source.SourceDescription gedcomXSourceDescription = (org.gedcomx.source.SourceDescription) gedcomXObject;

		if ((gedcomXSourceDescription.getCitations() == null) || gedcomXSourceDescription.getCitations().isEmpty()) {
			throw new MissingRequiredRelationshipException(SourceDescription.class, RelTypes.HAS_CITATION);
		}
	}

	public URI getAbout(final URI about) {
		return new URI((String) this.getProperty(NodeProperties.Generic.ABOUT));
	}

	public SourceReference getComponentOf() {
		return this.componentOf;
	}

	public List<Conclusion> getExtractedConclusions() {
		return this.extractedConclusions;
	}

	public String getId() {
		return (String) this.getProperty(NodeProperties.Generic.ID);
	}

	public Agent getMediator() {
		return this.mediator;
	}

	public List<Note> getNotes() {
		return this.notes;
	}

	public List<SourceCitation> getSourceCitations() {
		return this.sourceCitations;
	}

	public List<SourceReference> getSources() {
		return this.sources;
	}

	public List<TextValue> getTitles() {
		return this.titles;
	}

	public void setAbout(final URI about) {
		this.setProperty(NodeProperties.Generic.ABOUT, about.toString());
	}

	public void setAttributionChangeMessage(final String changeMessage) {
		this.setProperty(NodeProperties.Generic.ATTRIBUTION_CHANGE_MESSAGE, changeMessage);

	}

	public void setAttributionModifiedConfidence(final Date modified) {
		this.setProperty(NodeProperties.Generic.ATTRIBUTION_MODIFIED, modified.getTime());
	}

	public void setComponentOf(final SourceReference componentOf) {
		this.componentOf = componentOf;
		this.createRelationship(RelTypes.COMPONENT_OF, componentOf);
	}

	public void setId(final String id) {
		this.setProperty(NodeProperties.Generic.ID, id);
	}

	@Override
	protected void setInitialProperties(final Object gedcomXObject) {
		final org.gedcomx.source.SourceDescription gedcomXSourceDescription = (org.gedcomx.source.SourceDescription) gedcomXObject;

		this.setId(gedcomXSourceDescription.getId());
		this.setAbout(gedcomXSourceDescription.getAbout());

		if (gedcomXSourceDescription.getAttribution() != null) {
			this.setAttributionModifiedConfidence(gedcomXSourceDescription.getAttribution().getModified());
			this.setAttributionChangeMessage(gedcomXSourceDescription.getAttribution().getChangeMessage());
		}
	}

	public void setMediator(final Agent mediator) {
		this.mediator = mediator;
		this.createRelationship(RelTypes.MEDIATOR, mediator);
	}

}
