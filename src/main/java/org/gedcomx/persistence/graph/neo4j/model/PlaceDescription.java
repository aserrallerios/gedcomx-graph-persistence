package org.gedcomx.persistence.graph.neo4j.model;

import java.util.LinkedList;
import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;

public class PlaceDescription extends Conclusion {

	private final List<TextValue> names = new LinkedList<>();
	private final List<Identifier> identifiers = new LinkedList<>();

	protected PlaceDescription(final GENgraph graph, final org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription)
			throws MissingFieldException {
		super(graph, NodeTypes.PLACE_DESCRIPTION, gedcomXPlaceDescription);
	}

	public void addIdentifier(final Identifier identifier) {
		this.identifiers.add(identifier);
		this.createRelationship(GENgraphRelTypes.HAS_IDENTIFIER, identifier);
	}

	public void addName(final TextValue name) {
		this.names.add(name);
		this.createRelationship(GENgraphRelTypes.HAS_NAME, name);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription = (org.gedcomx.conclusion.PlaceDescription) gedcomXObject;

		if ((gedcomXPlaceDescription.getNames() == null) || gedcomXPlaceDescription.getNames().isEmpty()) {
			throw new MissingRequiredRelationshipException(PlaceDescription.class, gedcomXPlaceDescription.getId(), GENgraphRelTypes.HAS_NAME);
		}
	}

	public URI getAbout() {
		return new URI((String) this.getProperty(NodeProperties.Generic.ABOUT));
	}

	public List<Identifier> getIdentifiers() {
		return this.identifiers;
	}

	public Double getLatitude() {
		return (Double) this.getProperty(NodeProperties.Conclusion.LATITUDE);

	}

	public Double getLongitude() {
		return (Double) this.getProperty(NodeProperties.Conclusion.LONGITUDE);

	}

	public List<TextValue> getNames() {
		return this.names;
	}

	public ResourceReference getSpatialDescription() {
		return new ResourceReference(new URI((String) this.getProperty(NodeProperties.Conclusion.SPATIAL_DESCRIPTION)));
	}

	public String getTemporalDescriptionFormal() {
		return (String) this.getProperty(NodeProperties.Conclusion.TEMPORAL_DESCRIPTION_FORMAL);

	}

	public String getTemporalDescriptionOriginal() {
		return (String) this.getProperty(NodeProperties.Conclusion.TEMPORAL_DESCRIPTION_ORIGINAL);

	}

	public URI getType() {
		return new URI((String) this.getProperty(NodeProperties.Generic.TYPE));
	}

	public void setAbout(final URI about) {
		this.setProperty(NodeProperties.Generic.ABOUT, about.toString());
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription = (org.gedcomx.conclusion.PlaceDescription) gedcomXObject;

		this.setAbout(gedcomXPlaceDescription.getAbout());
		this.setType(gedcomXPlaceDescription.getType());
		this.setLatitude(gedcomXPlaceDescription.getLatitude());
		this.setLongitude(gedcomXPlaceDescription.getLatitude());
		this.setSpatialDescription(gedcomXPlaceDescription.getSpatialDescription());

		if (gedcomXPlaceDescription.getTemporalDescription() != null) {
			this.setTemporalDescriptionOriginal(gedcomXPlaceDescription.getTemporalDescription().getOriginal());
			this.setTemporalDescriptionFormal(gedcomXPlaceDescription.getTemporalDescription().getFormal());
		}

	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription = (org.gedcomx.conclusion.PlaceDescription) gedcomXObject;

		for (final org.gedcomx.common.TextValue gedcomXName : gedcomXPlaceDescription.getNames()) {
			this.addName(new TextValue(this.getGraph(), gedcomXName));
		}
		for (final org.gedcomx.conclusion.Identifier gedcomXIdentifier : gedcomXPlaceDescription.getIdentifiers()) {
			this.addIdentifier(new Identifier(this.getGraph(), gedcomXIdentifier));
		}
	}

	public void setLatitude(final Double latitude) {
		this.setProperty(NodeProperties.Conclusion.LATITUDE, latitude);

	}

	public void setLongitude(final Double longitude) {
		this.setProperty(NodeProperties.Conclusion.LONGITUDE, longitude);

	}

	public void setSpatialDescription(final ResourceReference spatialDescription) {
		this.setProperty(NodeProperties.Conclusion.SPATIAL_DESCRIPTION, spatialDescription.getResource().toString());
	}

	public void setTemporalDescriptionFormal(final String formal) {
		this.setProperty(NodeProperties.Conclusion.TEMPORAL_DESCRIPTION_FORMAL, formal);

	}

	public void setTemporalDescriptionOriginal(final String original) {
		this.setProperty(NodeProperties.Conclusion.TEMPORAL_DESCRIPTION_ORIGINAL, original);

	}

	public void setType(final URI type) {
		this.setProperty(NodeProperties.Generic.TYPE, type.toString());
	}

}
