package org.gedcomx.persistence.graph.neo4j.model.source;

import java.util.LinkedList;
import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.model.GENgraphNode;
import org.gedcomx.persistence.graph.neo4j.model.GENgraphTopLevelNode;
import org.gedcomx.persistence.graph.neo4j.model.common.Attribution;
import org.gedcomx.persistence.graph.neo4j.model.common.Note;
import org.gedcomx.persistence.graph.neo4j.model.common.TextValue;
import org.gedcomx.persistence.graph.neo4j.model.conclusion.Conclusion;
import org.gedcomx.persistence.graph.neo4j.model.contributor.Agent;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.utils.RelTypes;
import org.neo4j.graphdb.Node;

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

	public SourceDescription(final Node node) throws WrongNodeType {
		super(NodeTypes.SOURCE_DESCRIPTION, node);
	}

	public SourceDescription(final org.gedcomx.source.SourceDescription gedcomXSourceDescription) throws MissingFieldException {
		super(NodeTypes.SOURCE_DESCRIPTION, gedcomXSourceDescription);
	}

	public SourceDescription(final String citationValue) {
		super(NodeTypes.SOURCE_DESCRIPTION, new Object[] { citationValue });
	}

	public void addCitation(final SourceCitation sourceCitation) {
		this.sourceCitations.add(sourceCitation);
		this.createRelationship(RelTypes.HAS_CITATION, sourceCitation);
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

	public void addTitle(final TextValue textValue) {
		this.titles.add(textValue);
		this.createRelationship(RelTypes.HAS_TITLE, textValue);
	}

	@Override
	protected void deleteAllReferences() {
		this.deleteReferencedNodes(Note.class, RelTypes.HAS_NOTE);
		this.deleteReferencedNodes(TextValue.class, RelTypes.HAS_TITLE);
		this.deleteReference(Conclusion.class, RelTypes.CONCLUSION);
	}

	public URI getAbout(final URI about) {
		return new URI((String) this.getProperty(NodeProperties.Generic.ABOUT));
	}

	public List<SourceCitation> getCitations() {
		return this.sourceCitations;
	}

	public SourceReference getComponentOf() {
		return this.componentOf;
	}

	public List<Conclusion> getExtractedConclusions() {
		return this.extractedConclusions;
	}

	@Override
	protected org.gedcomx.source.SourceDescription getGedcomX() {
		final org.gedcomx.source.SourceDescription gedcomXSourceDescription = new org.gedcomx.source.SourceDescription();
		// TODO Auto-generated method stub
		return gedcomXSourceDescription;
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

	public List<SourceReference> getSources() {
		return this.sources;
	}

	public List<TextValue> getTitles() {
		return this.titles;
	}

	@Override
	protected void resolveReferences() {
		// TODO
	}

	public void setAbout(final URI about) {
		this.setProperty(NodeProperties.Generic.ABOUT, about.toString());
	}

	public void setAttribution(final Attribution attribution) {
		this.createRelationship(RelTypes.ATTRIBUTION, attribution);
	}

	public void setComponentOf(final SourceReference componentOf) {
		this.componentOf = componentOf;
		this.createRelationship(RelTypes.COMPONENT_OF, componentOf);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.source.SourceDescription gedcomXSourceDescription = (org.gedcomx.source.SourceDescription) gedcomXObject;

		this.setId(gedcomXSourceDescription.getId());
		this.setAbout(gedcomXSourceDescription.getAbout());
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.source.SourceDescription gedcomXSourceDescription = (org.gedcomx.source.SourceDescription) gedcomXObject;

		this.setComponentOf(new SourceReference(gedcomXSourceDescription.getComponentOf()));
		this.setAttribution(new Attribution(gedcomXSourceDescription.getAttribution()));

		for (final org.gedcomx.common.Note note : gedcomXSourceDescription.getNotes()) {
			this.addNote(new Note(note));
		}
		for (final org.gedcomx.common.TextValue title : gedcomXSourceDescription.getTitles()) {
			this.addTitle(new TextValue(title));
		}
		for (final org.gedcomx.source.SourceCitation sourceCitation : gedcomXSourceDescription.getCitations()) {
			this.addSourceCitation(new SourceCitation(sourceCitation));
		}
		for (final org.gedcomx.source.SourceReference source : gedcomXSourceDescription.getSources()) {
			this.addSource(new SourceReference(source));
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

	public void setId(final String id) {
		this.setProperty(NodeProperties.Generic.ID, id);
	}

	public void setMediator(final Agent mediator) {
		this.mediator = mediator;
		this.createRelationship(RelTypes.MEDIATOR, mediator);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.addRelationship(RelTypes.HAS_CITATION, new SourceCitation((String) properties[0]));
	}

	@Override
	protected void validateGedcomXObject(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.source.SourceDescription gedcomXSourceDescription = (org.gedcomx.source.SourceDescription) gedcomXObject;

		if ((gedcomXSourceDescription.getCitations() == null) || gedcomXSourceDescription.getCitations().isEmpty()) {
			throw new MissingRequiredRelationshipException(SourceDescription.class, gedcomXSourceDescription.getId(), RelTypes.HAS_CITATION);
		}
	}

	@Override
	protected void validateUnderlyingNode() throws WrongNodeType {
		if ((this.getCitations() == null) || this.getCitations().isEmpty()) {
			throw new WrongNodeType();
		}
		for (final SourceCitation citation : this.getCitations()) {
			if ((citation.getValue() == null) || citation.getValue().isEmpty()) {
				throw new WrongNodeType();
			}
		}
	}
}
