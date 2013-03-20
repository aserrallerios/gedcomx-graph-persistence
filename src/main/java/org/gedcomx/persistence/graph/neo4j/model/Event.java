package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.types.EventType;
import org.neo4j.graphdb.Node;

@NodeType("EVENT")
public class Event extends Conclusion {

	public Event() throws MissingFieldException {
		super();
	}

	public Event(final Node node) throws MissingFieldException, UnknownNodeType {
		super(node);
	}

	public Event(final org.gedcomx.conclusion.Event gedcomXEvent) throws MissingFieldException {
		super(gedcomXEvent);
	}

	public void addRole(final EventRole role) {
		this.addRelationship(GENgraphRelTypes.HAS_ROLE, role);
	}

	@Override
	protected void deleteAllConcreteReferences() {
		this.deleteReferencedNode(this.getPlaceReference());
		this.deleteReferencedNodes(this.getRoles());
	}

	public String getDateFormal() {
		return (String) this.getProperty(ConclusionProperties.DATE_FORMAL);
	}

	public String getDateOriginal() {
		return (String) this.getProperty(ConclusionProperties.DATE_ORIGINAL);
	}

	@Override
	protected org.gedcomx.conclusion.Event getGedcomX() {
		final org.gedcomx.conclusion.Event gedcomXEvent = new org.gedcomx.conclusion.Event();

		this.getGedcomXConclusion(gedcomXEvent);

		final org.gedcomx.conclusion.Date date = new org.gedcomx.conclusion.Date();
		date.setFormal(this.getDateFormal());
		date.setOriginal(this.getDateOriginal());
		gedcomXEvent.setDate(date);

		gedcomXEvent.setKnownType(this.getKnownType());
		gedcomXEvent.setPlace(this.getPlaceReference().getGedcomX());
		gedcomXEvent.setRoles(this.getGedcomXList(org.gedcomx.conclusion.EventRole.class, this.getRoles()));

		return gedcomXEvent;
	}

	public EventType getKnownType() {
		return EventType.fromQNameURI(this.getType());
	}

	public PlaceReference getPlaceReference() {
		return this.getNodeByRelationship(PlaceReference.class, GENgraphRelTypes.PLACE);
	}

	public List<EventRole> getRoles() {
		return this.getNodesByRelationship(EventRole.class, GENgraphRelTypes.HAS_ROLE);
	}

	public URI getType() {
		return new URI((String) this.getProperty(GenericProperties.TYPE));
	}

	@Override
	protected void resolveReferences() {
		return;
	}

	public void setDateFormal(final String value) {
		this.setProperty(ConclusionProperties.DATE_FORMAL, value);
	}

	public void setDateOriginal(final String value) {
		this.setProperty(ConclusionProperties.DATE_ORIGINAL, value);
	}

	@Override
	protected void setGedcomXConcreteProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Event gedcomXEvent = (org.gedcomx.conclusion.Event) gedcomXObject;
		this.setType(gedcomXEvent.getType());

		if (gedcomXEvent.getDate() != null) {
			this.setDateFormal(gedcomXEvent.getDate().getFormal());
			this.setDateOriginal(gedcomXEvent.getDate().getOriginal());
		}

	}

	@Override
	protected void setGedcomXConcreteRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.Event gedcomXEvent = (org.gedcomx.conclusion.Event) gedcomXObject;

		if (gedcomXEvent.getPlace() != null) {
			this.setPlaceReference(new PlaceReference(gedcomXEvent.getPlace()));
		}
		for (final org.gedcomx.conclusion.EventRole role : gedcomXEvent.getRoles()) {
			this.addRole(new EventRole(role));
		}
	}

	public void setKnownType(final EventType type) {
		this.setType(type.toQNameURI());
	}

	public void setPlaceReference(final PlaceReference placeReference) {
		this.createRelationship(GENgraphRelTypes.PLACE, placeReference);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) throws MissingFieldException {
		return;
	}

	public void setType(final URI type) {
		this.setProperty(GenericProperties.TYPE, type.toString());
	}

	@Override
	protected void validateUnderlyingNode() throws MissingFieldException {
		return;
	}
}
