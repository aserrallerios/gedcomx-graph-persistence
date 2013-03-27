package org.gedcomx.persistence.graph.neo4j.model;

import java.util.ArrayList;
import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.SourceProperties;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.neo4j.graphdb.Node;

@NodeType("SOURCE_DESCRIPTION")
public class SourceDescription extends NodeWrapper {

	public SourceDescription(final Node node) throws UnknownNodeType, MissingFieldException {
		super(node);
	}

	public SourceDescription(final org.gedcomx.source.SourceDescription gedcomXSourceDescription) throws MissingFieldException {
		super(gedcomXSourceDescription);
	}

	public SourceDescription(final String citationValue) throws MissingFieldException {
		super(new Object[] { citationValue });
	}

	public void addCitation(final SourceCitation sourceCitation) {
		this.addRelationship(RelationshipTypes.HAS_CITATION, sourceCitation);
	}

	public void addExtractedConclusion(final Conclusion conclusion) {
		this.addRelationship(RelationshipTypes.HAS_CONCLUSION, conclusion);
	}

	public void addNote(final Note note) {
		this.addRelationship(RelationshipTypes.HAS_NOTE, note);
	}

	public void addSource(final SourceReference sourceReference) {
		this.addRelationship(RelationshipTypes.HAS_SOURCE_REFERENCE, sourceReference);
	}

	public void addTitle(final TextValue textValue) {
		this.addRelationship(RelationshipTypes.HAS_TITLE, textValue);
	}

	@Override
	protected void deleteAllReferences() {
		this.deleteReferencedNodes(this.getNotes());
		this.deleteReferencedNodes(this.getTitles());
		this.deleteReferencedNodes(this.getCitations());
		this.deleteReferencedNodes(this.getSources());
		this.deleteReferencedNode(this.getAttribution());
		this.deleteReferencedNode(this.getComponentOf());

		this.deleteReferences(RelationshipTypes.HAS_CONCLUSION);
		this.deleteReference(RelationshipTypes.MEDIATOR);
	}

	public URI getAbout() {
		return new URI((String) this.getProperty(GenericProperties.ABOUT));
	}

	public Attribution getAttribution() {
		return this.getNodeByRelationship(Attribution.class, RelationshipTypes.ATTRIBUTION);
	}

	public List<SourceCitation> getCitations() {
		return this.getNodesByRelationship(SourceCitation.class, RelationshipTypes.HAS_CITATION);
	}

	public SourceReference getComponentOf() {
		return this.getNodeByRelationship(SourceReference.class, RelationshipTypes.COMPONENT_OF);
	}

	public List<Conclusion> getExtractedConclusions() {
		return this.getNodesByRelationship(Conclusion.class, RelationshipTypes.HAS_CONCLUSION);
	}

	@Override
	protected org.gedcomx.source.SourceDescription getGedcomX() {
		final org.gedcomx.source.SourceDescription gedcomXSourceDescription = new org.gedcomx.source.SourceDescription();

		gedcomXSourceDescription.setAbout(this.getAbout());
		gedcomXSourceDescription.setId(this.getId());

		final Attribution attr = this.getAttribution();
		if (attr != null) {
			gedcomXSourceDescription.setAttribution(attr.getGedcomX());
		}
		final SourceReference src = this.getComponentOf();
		if (src != null) {
			gedcomXSourceDescription.setComponentOf(src.getGedcomX());
		}

		gedcomXSourceDescription.setCitations(this.getGedcomXList(org.gedcomx.source.SourceCitation.class, this.getCitations()));
		gedcomXSourceDescription.setNotes(this.getGedcomXList(org.gedcomx.common.Note.class, this.getNotes()));
		gedcomXSourceDescription.setSources(this.getGedcomXList(org.gedcomx.source.SourceReference.class, this.getSources()));
		gedcomXSourceDescription.setTitles(this.getGedcomXList(org.gedcomx.common.TextValue.class, this.getTitles()));

		final Agent mediator = this.getMediator();
		if (mediator != null) {
			gedcomXSourceDescription.setMediator(mediator.getResourceReference());
		}

		final List<Conclusion> conclusions = this.getExtractedConclusions();
		if (conclusions != null) {
			final List<ResourceReference> references = new ArrayList<>();
			for (final Conclusion conclusion : conclusions) {
				references.add(conclusion.getResourceReference());
			}
			gedcomXSourceDescription.setExtractedConclusions(references);
		}
		return gedcomXSourceDescription;
	}

	public String getId() {
		return (String) this.getProperty(GenericProperties.ID);
	}

	public Agent getMediator() {
		return this.getNodeByRelationship(Agent.class, RelationshipTypes.MEDIATOR);
	}

	public List<Note> getNotes() {
		return this.getNodesByRelationship(Note.class, RelationshipTypes.HAS_NOTE);
	}

	@Override
	protected ResourceReference getResourceReference() {
		return new ResourceReference(new URI(this.getId()));
	}

	public List<SourceReference> getSources() {
		return this.getNodesByRelationship(SourceReference.class, RelationshipTypes.HAS_SOURCE_REFERENCE);
	}

	public List<TextValue> getTitles() {
		return this.getNodesByRelationship(TextValue.class, RelationshipTypes.HAS_TITLE);
	}

	@Override
	protected URI getURI() {
		return new URI(this.getId());
	}

	@Override
	public void resolveReferences() {
		this.createReferenceRelationship(RelationshipTypes.MEDIATOR, SourceProperties.MEDIATOR_REFERENCE);
		this.createReferenceRelationship(RelationshipTypes.HAS_CONCLUSION, SourceProperties.EXTRACTED_CONCLUSIONS_REFERENCE);
	}

	public void setAbout(final URI about) {
		this.setProperty(GenericProperties.ABOUT, about);
	}

	public void setAttribution(final Attribution attribution) {
		this.createRelationship(RelationshipTypes.ATTRIBUTION, attribution);
	}

	public void setComponentOf(final SourceReference componentOf) {
		this.createRelationship(RelationshipTypes.COMPONENT_OF, componentOf);
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
			this.setProperty(SourceProperties.MEDIATOR_REFERENCE, gedcomXSourceDescription.getMediator());
		}
		if (gedcomXSourceDescription.getExtractedConclusions() != null) {
			this.setURIListProperties(SourceProperties.EXTRACTED_CONCLUSIONS_REFERENCE, gedcomXSourceDescription.getExtractedConclusions());
		}
	}

	public void setId(final String id) {
		this.setProperty(GenericProperties.ID, id);
	}

	public void setMediator(final Agent mediator) {
		this.createReferenceRelationship(RelationshipTypes.MEDIATOR, mediator);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) throws MissingFieldException {
		this.addRelationship(RelationshipTypes.HAS_CITATION, new SourceCitation((String) properties[0]));
	}

	@Override
	protected void validateUnderlyingNode() throws MissingRequiredRelationshipException {
		if (ValidationTools.nullOrEmpty(this.getCitations())) {
			throw new MissingRequiredRelationshipException(this.getAnnotatedNodeType(), this.getId(), RelationshipTypes.HAS_CITATION.toString());
		}
	}
}
