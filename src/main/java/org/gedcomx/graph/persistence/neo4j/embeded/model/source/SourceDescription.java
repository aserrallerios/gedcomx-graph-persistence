package org.gedcomx.graph.persistence.neo4j.embeded.model.source;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredRelationshipException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphTopLevelNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.common.Note;
import org.gedcomx.graph.persistence.neo4j.embeded.model.common.TextValue;
import org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion.Conclusion;
import org.gedcomx.graph.persistence.neo4j.embeded.model.contributor.Agent;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.RelTypes;

public class SourceDescription extends GENgraphNode implements GENgraphTopLevelNode {

	private final List<Note> notes = new LinkedList<>();
	private final List<SourceCitation> sourceCitations = new LinkedList<>();
	private final List<TextValue> titles = new LinkedList<>();
	private final List<SourceReference> sources = new LinkedList<>();
	private final List<Conclusion> extractedConclusions = new LinkedList<>();
	private final List<URI> extractedConclusionsURI = new LinkedList<>();

	private SourceReference componentOf;
	private Agent mediator;
	private URI mediatorURI;

	public SourceDescription(final GENgraph graph, final org.gedcomx.source.SourceDescription gedcomXSourceDescription)
			throws MissingFieldException {
		super(graph, NodeTypes.SOURCE_DESCRIPTION, gedcomXSourceDescription);
	}

	public void addExtractedConclusion(final Conclusion conclusion) {
		this.extractedConclusions.add(conclusion);
		this.createRelationship(RelTypes.HAS_CONCLUSION, conclusion);
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
			throw new MissingRequiredRelationshipException(SourceDescription.class, gedcomXSourceDescription.getId(), RelTypes.HAS_CITATION);
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

	@Override
	protected void resolveReferences() {
		if ((this.mediator == null) && (this.mediatorURI != null)) {
			final Agent mediator = this.getGraph().getAgent(this.mediatorURI);
			if (mediator != null) {
				this.setMediator(mediator);
			}
		}
		for (final URI conclusionsReferences : this.extractedConclusionsURI) {
			final Conclusion conclusion = this.getGraph().getConclusion(conclusionsReferences);
			if (conclusion != null) {
				if (this.extractedConclusions.indexOf(conclusion) == -1) {
					this.addExtractedConclusion(conclusion);
				}
			}
		}
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

	@Override
	protected void setRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.source.SourceDescription gedcomXSourceDescription = (org.gedcomx.source.SourceDescription) gedcomXObject;

		this.setComponentOf(new SourceReference(this.getGraph(), gedcomXSourceDescription.getComponentOf()));

		for (final org.gedcomx.common.Note note : gedcomXSourceDescription.getNotes()) {
			this.addNote(new Note(this.getGraph(), note));
		}
		for (final org.gedcomx.common.TextValue title : gedcomXSourceDescription.getTitles()) {
			this.addTitle(new TextValue(this.getGraph(), title));
		}
		for (final org.gedcomx.source.SourceCitation sourceCitation : gedcomXSourceDescription.getCitations()) {
			this.addSourceCitation(new SourceCitation(this.getGraph(), sourceCitation));
		}
		for (final org.gedcomx.source.SourceReference source : gedcomXSourceDescription.getSources()) {
			this.addSource(new SourceReference(this.getGraph(), source));
		}

		if (gedcomXSourceDescription.getMediator() != null) {
			this.mediatorURI = gedcomXSourceDescription.getMediator().getResource();
			final Agent mediator = this.getGraph().getAgent(this.mediatorURI);
			if (mediator != null) {
				this.setMediator(mediator);
			} else {
				this.addNodeToResolveReferences();
			}
		}
		for (final ResourceReference conclusionReference : gedcomXSourceDescription.getExtractedConclusions()) {
			this.extractedConclusionsURI.add(conclusionReference.getResource());
			final Conclusion conclusion = this.getGraph().getConclusion(conclusionReference.getResource());
			if (conclusion != null) {
				this.addExtractedConclusion(conclusion);
			} else {
				this.addNodeToResolveReferences();
			}
		}
		return;
	}

}
