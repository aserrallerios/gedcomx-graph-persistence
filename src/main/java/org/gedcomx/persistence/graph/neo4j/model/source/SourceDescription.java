package org.gedcomx.persistence.graph.neo4j.model.source;

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
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.neo4j.graphdb.Node;

public class SourceDescription extends GENgraphNode implements GENgraphTopLevelNode {

	public SourceDescription(final Node node) throws WrongNodeType, MissingFieldException {
		super(NodeTypes.SOURCE_DESCRIPTION, node);
	}

	public SourceDescription(final org.gedcomx.source.SourceDescription gedcomXSourceDescription) throws MissingFieldException {
		super(NodeTypes.SOURCE_DESCRIPTION, gedcomXSourceDescription);
	}

	public SourceDescription(final String citationValue) throws MissingFieldException {
		super(NodeTypes.SOURCE_DESCRIPTION, new Object[] { citationValue });
	}

	public void addCitation(final SourceCitation sourceCitation) {
		this.addRelationship(RelTypes.HAS_CITATION, sourceCitation);
	}

	public void addExtractedConclusion(final Conclusion conclusion) {
		this.addRelationship(RelTypes.HAS_CONCLUSION, conclusion);
	}

	public void addNote(final Note note) {
		this.addRelationship(RelTypes.HAS_NOTE, note);
	}

	public void addSource(final SourceReference sourceReference) {
		this.addRelationship(RelTypes.HAS_SOURCE_REFERENCE, sourceReference);
	}

	public void addTitle(final TextValue textValue) {
		this.addRelationship(RelTypes.HAS_TITLE, textValue);
	}

	@Override
	protected void deleteAllReferences() {
		this.deleteReferencedNodes(Note.class, RelTypes.HAS_NOTE);
		this.deleteReferencedNodes(TextValue.class, RelTypes.HAS_TITLE);
		this.deleteReferencedNodes(SourceCitation.class, RelTypes.HAS_CITATION);
		this.deleteReferencedNodes(SourceReference.class, RelTypes.HAS_SOURCE_REFERENCE);
		this.deleteReferencedNode(Attribution.class, RelTypes.ATTRIBUTION);
		this.deleteReferencedNode(SourceReference.class, RelTypes.COMPONENT_OF);

		this.deleteReferences(RelTypes.HAS_CONCLUSION);
		this.deleteReference(RelTypes.MEDIATOR);
	}

	public URI getAbout(final URI about) {
		return new URI((String) this.getProperty(NodeProperties.Generic.ABOUT));
	}

	public Attribution getAttribution() {
		return this.getNodeByRelationship(Attribution.class, RelTypes.ATTRIBUTION);
	}

	public List<SourceCitation> getCitations() {
		return this.getNodesByRelationship(SourceCitation.class, RelTypes.HAS_CITATION);
	}

	public SourceReference getComponentOf() {
		return this.getNodeByRelationship(SourceReference.class, RelTypes.COMPONENT_OF);
	}

	public List<Conclusion> getExtractedConclusions() {
		return this.getNodesByRelationship(Conclusion.class, RelTypes.HAS_CONCLUSION);
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
		return this.getNodeByRelationship(Agent.class, RelTypes.MEDIATOR);
	}

	public List<Note> getNotes() {
		return this.getNodesByRelationship(Note.class, RelTypes.HAS_NOTE);
	}

	public List<SourceReference> getSources() {
		return this.getNodesByRelationship(SourceReference.class, RelTypes.HAS_SOURCE_REFERENCE);
	}

	public List<TextValue> getTitles() {
		return this.getNodesByRelationship(TextValue.class, RelTypes.HAS_TITLE);
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
			this.addCitation(new SourceCitation(sourceCitation));
		}
		for (final org.gedcomx.source.SourceReference source : gedcomXSourceDescription.getSources()) {
			this.addSource(new SourceReference(source));
		}

		if (gedcomXSourceDescription.getMediator() != null) {
			this.setMediator(new Agent());
		}
		for (final ResourceReference conclusionReference : gedcomXSourceDescription.getExtractedConclusions()) {
			this.addExtractedConclusion(new Conclusion());
		}
		// TODO
		return;
	}

	public void setId(final String id) {
		this.setProperty(NodeProperties.Generic.ID, id);
	}

	public void setMediator(final Agent mediator) {
		this.createRelationship(RelTypes.MEDIATOR, mediator);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) throws MissingFieldException {
		this.addRelationship(RelTypes.HAS_CITATION, new SourceCitation((String) properties[0]));
	}

	@Override
	protected void validateUnderlyingNode() throws MissingRequiredRelationshipException {
		if (ValidationTools.nullOrEmpty(this.getCitations())) {
			throw new MissingRequiredRelationshipException(SourceDescription.class, this.getId(), RelTypes.HAS_CITATION);
		}
	}
}
