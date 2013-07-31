package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.SourceProperties;
import org.gedcomx.persistence.graph.neo4j.utils.Validation;
import org.neo4j.graphdb.Node;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

@NodeType(NodeTypes.SOURCE_DESCRIPTION)
public class SourceDescription extends NodeWrapper {

	protected SourceDescription(final Node node) {
		super(node);
	}

	@Inject
	protected SourceDescription(
			final @Assisted org.gedcomx.source.SourceDescription gedcomXSourceDescription) {
		super(gedcomXSourceDescription);
	}

	protected SourceDescription(final String citationValue) {
		super(new Object[] { citationValue });
	}

	public void addCitation(final SourceCitation sourceCitation) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_CITATION, sourceCitation);
	}

	public void addNote(final Note note) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_NOTE, note);
	}

	public void addSource(final SourceReference sourceReference) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_SOURCE_REFERENCE, sourceReference);
	}

	public void addTitle(final TextValue textValue) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_TITLE, textValue);
	}

	@Override
	protected void deleteAllReferences() {
		NodeWrapper.nodeWrapperOperations
				.deleteReferencedNodes(this.getNotes());
		NodeWrapper.nodeWrapperOperations.deleteReferencedNodes(this
				.getTitles());
		NodeWrapper.nodeWrapperOperations.deleteReferencedNodes(this
				.getCitations());
		NodeWrapper.nodeWrapperOperations.deleteReferencedNodes(this
				.getSources());
		NodeWrapper.nodeWrapperOperations.deleteReferencedNode(this
				.getAttribution());
		NodeWrapper.nodeWrapperOperations.deleteReferencedNode(this
				.getComponentOf());

		NodeWrapper.nodeWrapperOperations.deleteReferences(this,
				RelationshipTypes.HAS_CONCLUSION);
		NodeWrapper.nodeWrapperOperations.deleteReference(this,
				RelationshipTypes.MEDIATOR);
	}

	public URI getAbout() {
		return new URI((String) NodeWrapper.nodeWrapperOperations.getProperty(
				this, GenericProperties.ABOUT));
	}

	public Attribution getAttribution() {
		return NodeWrapper.nodeWrapperOperations.getNodeByRelationship(this,
				Attribution.class, RelationshipTypes.ATTRIBUTION);
	}

	public List<SourceCitation> getCitations() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				SourceCitation.class, RelationshipTypes.HAS_CITATION);
	}

	public SourceReference getComponentOf() {
		return NodeWrapper.nodeWrapperOperations.getNodeByRelationship(this,
				SourceReference.class, RelationshipTypes.COMPONENT_OF);
	}

	@Override
	public org.gedcomx.source.SourceDescription getGedcomX() {
		final org.gedcomx.source.SourceDescription gedcomXSourceDescription = new org.gedcomx.source.SourceDescription();

		gedcomXSourceDescription.setAbout(this.getAbout());
		gedcomXSourceDescription.setId(this.getId());
		gedcomXSourceDescription.setMediaType(this.getMediaType());

		final Attribution attr = this.getAttribution();
		if (attr != null) {
			gedcomXSourceDescription.setAttribution(attr.getGedcomX());
		}
		final SourceReference src = this.getComponentOf();
		if (src != null) {
			gedcomXSourceDescription.setComponentOf(src.getGedcomX());
		}

		gedcomXSourceDescription.setCitations(NodeWrapper.nodeWrapperOperations
				.getGedcomXList(org.gedcomx.source.SourceCitation.class,
						this.getCitations()));
		gedcomXSourceDescription
				.setNotes(NodeWrapper.nodeWrapperOperations.getGedcomXList(
						org.gedcomx.common.Note.class, this.getNotes()));
		gedcomXSourceDescription.setSources(NodeWrapper.nodeWrapperOperations
				.getGedcomXList(org.gedcomx.source.SourceReference.class,
						this.getSources()));
		gedcomXSourceDescription.setTitles(NodeWrapper.nodeWrapperOperations
				.getGedcomXList(org.gedcomx.common.TextValue.class,
						this.getTitles()));

		final Agent mediator = this.getMediator();
		if (mediator != null) {
			gedcomXSourceDescription.setMediator(mediator
					.getResourceReference());
		}

		return gedcomXSourceDescription;
	}

	public String getId() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				GenericProperties.ID);
	}

	public Agent getMediator() {
		return NodeWrapper.nodeWrapperOperations.getNodeByRelationship(this,
				Agent.class, RelationshipTypes.MEDIATOR);
	}

	public String getMediaType() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				ConclusionProperties.MEDIA_TYPE);
	}

	public List<Note> getNotes() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				Note.class, RelationshipTypes.HAS_NOTE);
	}

	@Override
	protected ResourceReference getResourceReference() {
		return new ResourceReference(new URI(this.getId()));
	}

	public List<SourceReference> getSources() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				SourceReference.class, RelationshipTypes.HAS_SOURCE_REFERENCE);
	}

	public List<TextValue> getTitles() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				TextValue.class, RelationshipTypes.HAS_TITLE);
	}

	@Override
	protected URI getURI() {
		return new URI(this.getId());
	}

	@Override
	public void resolveReferences() {
		NodeWrapper.nodeWrapperOperations
				.createReferenceRelationship(this, RelationshipTypes.MEDIATOR,
						SourceProperties.MEDIATOR_REFERENCE);
		NodeWrapper.nodeWrapperOperations.createReferenceRelationship(this,
				RelationshipTypes.HAS_CONCLUSION,
				SourceProperties.EXTRACTED_CONCLUSIONS_REFERENCE);
	}

	public void setAbout(final URI about) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.ABOUT, about);
	}

	public void setAttribution(final Attribution attribution) {
		NodeWrapper.nodeWrapperOperations.createRelationship(this,
				RelationshipTypes.ATTRIBUTION, attribution);
	}

	public void setComponentOf(final SourceReference componentOf) {
		NodeWrapper.nodeWrapperOperations.createRelationship(this,
				RelationshipTypes.COMPONENT_OF, componentOf);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.source.SourceDescription gedcomXSourceDescription = (org.gedcomx.source.SourceDescription) gedcomXObject;

		this.setId(gedcomXSourceDescription.getId());
		this.setAbout(gedcomXSourceDescription.getAbout());
		this.setMediaType(gedcomXSourceDescription.getMediaType());
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) {
		final org.gedcomx.source.SourceDescription gedcomXSourceDescription = (org.gedcomx.source.SourceDescription) gedcomXObject;

		this.setComponentOf(new SourceReference(gedcomXSourceDescription
				.getComponentOf()));
		this.setAttribution(new Attribution(gedcomXSourceDescription
				.getAttribution()));

		if (gedcomXSourceDescription.getNotes() != null) {
			for (final org.gedcomx.common.Note note : gedcomXSourceDescription
					.getNotes()) {
				this.addNote(new Note(note));
			}
		}
		if (gedcomXSourceDescription.getTitles() != null) {
			for (final org.gedcomx.common.TextValue title : gedcomXSourceDescription
					.getTitles()) {
				this.addTitle(new TextValue(title));
			}
		}
		if (gedcomXSourceDescription.getSources() != null) {
			for (final org.gedcomx.source.SourceReference source : gedcomXSourceDescription
					.getSources()) {
				this.addSource(new SourceReference(source));
			}
		}
		for (final org.gedcomx.source.SourceCitation sourceCitation : gedcomXSourceDescription
				.getCitations()) {
			this.addCitation(new SourceCitation(sourceCitation));
		}

		if (gedcomXSourceDescription.getMediator() != null) {
			NodeWrapper.nodeWrapperOperations.setProperty(this,
					SourceProperties.MEDIATOR_REFERENCE,
					gedcomXSourceDescription.getMediator());
		}
	}

	public void setId(final String id) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.ID, id);
	}

	public void setMediator(final Agent mediator) {
		NodeWrapper.nodeWrapperOperations.createReferenceRelationship(this,
				RelationshipTypes.MEDIATOR, mediator);
	}

	public void setMediaType(final String mediaType) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				ConclusionProperties.MEDIA_TYPE, mediaType);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_CITATION, new SourceCitation(
						(String) properties[0]));
	}

	@Override
	protected void validateUnderlyingNode() {
		if (Validation.nullOrEmpty(this.getCitations())) {
			throw new MissingRequiredRelationshipException(
					NodeWrapper.nodeWrapperOperations
							.getAnnotatedNodeType(this),
					this.getId(), RelationshipTypes.HAS_CITATION.toString());
		}
	}
}
