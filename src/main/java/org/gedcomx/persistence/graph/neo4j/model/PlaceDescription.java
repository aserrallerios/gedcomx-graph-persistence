package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.neo4j.graphdb.Node;

@NodeType("PLACE_DESCRIPTION")
public class PlaceDescription extends Conclusion {

	protected PlaceDescription(final Node node) throws MissingFieldException, WrongNodeType {
		super(node);
	}

	protected PlaceDescription(final org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription) throws MissingFieldException {
		super(gedcomXPlaceDescription);
	}

	protected PlaceDescription(final String nameValue) throws MissingFieldException {
		super(new Object[] { nameValue });
	}

	public void addIdentifier(final Identifier identifier) {
		this.addRelationship(GENgraphRelTypes.HAS_IDENTIFIER, identifier);
	}

	public void addName(final TextValue name) {
		this.addRelationship(GENgraphRelTypes.HAS_NAME, name);
	}

	@Override
	protected void deleteAllConcreteReferences() {
		this.deleteReferencedNodes(this.getIdentifiers());
		this.deleteReferencedNodes(this.getNames());
	}

	public URI getAbout() {
		return new URI((String) this.getProperty(NodeProperties.Generic.ABOUT));
	}

	@Override
	protected org.gedcomx.conclusion.PlaceDescription getGedcomX() {
		final org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription = new org.gedcomx.conclusion.PlaceDescription();

		this.getGedcomXConclusion(gedcomXPlaceDescription);

		gedcomXPlaceDescription.setAbout(this.getAbout());
		gedcomXPlaceDescription.setLatitude(this.getLatitude());
		gedcomXPlaceDescription.setLongitude(this.getLongitude());
		gedcomXPlaceDescription.setSpatialDescription(this.getSpatialDescription());

		final org.gedcomx.conclusion.Date date = new org.gedcomx.conclusion.Date();
		date.setFormal(this.getTemporalDescriptionFormal());
		date.setOriginal(this.getTemporalDescriptionOriginal());
		gedcomXPlaceDescription.setTemporalDescription(date);

		gedcomXPlaceDescription.setNames(this.getGedcomXList(org.gedcomx.common.TextValue.class, this.getNames()));
		gedcomXPlaceDescription.setIdentifiers(this.getGedcomXList(org.gedcomx.conclusion.Identifier.class, this.getIdentifiers()));

		return gedcomXPlaceDescription;
	}

	public List<Identifier> getIdentifiers() {
		return this.getNodesByRelationship(Identifier.class, GENgraphRelTypes.HAS_IDENTIFIER);
	}

	public Double getLatitude() {
		return (Double) this.getProperty(NodeProperties.Conclusion.LATITUDE);

	}

	public Double getLongitude() {
		return (Double) this.getProperty(NodeProperties.Conclusion.LONGITUDE);

	}

	public List<TextValue> getNames() {
		return this.getNodesByRelationship(TextValue.class, GENgraphRelTypes.HAS_NAME);
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

	@Override
	protected void resolveReferences() {
		return;
	}

	public void setAbout(final URI about) {
		this.setProperty(NodeProperties.Generic.ABOUT, about.toString());
	}

	@Override
	protected void setGedcomXConcreteProperties(final Object gedcomXObject) {
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
	protected void setGedcomXConcreteRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription = (org.gedcomx.conclusion.PlaceDescription) gedcomXObject;

		for (final org.gedcomx.common.TextValue gedcomXName : gedcomXPlaceDescription.getNames()) {
			this.addName(new TextValue(gedcomXName));
		}
		for (final org.gedcomx.conclusion.Identifier gedcomXIdentifier : gedcomXPlaceDescription.getIdentifiers()) {
			this.addIdentifier(new Identifier(gedcomXIdentifier));
		}
	}

	public void setLatitude(final Double latitude) {
		this.setProperty(NodeProperties.Conclusion.LATITUDE, latitude);

	}

	public void setLongitude(final Double longitude) {
		this.setProperty(NodeProperties.Conclusion.LONGITUDE, longitude);

	}

	@Override
	protected void setRequiredProperties(final Object... properties) throws MissingFieldException {
		this.addName(new TextValue((String) properties[0]));
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

	@Override
	protected void validateUnderlyingNode() throws MissingFieldException {
		if (ValidationTools.nullOrEmpty(this.getNames())) {
			throw new MissingRequiredRelationshipException(PlaceDescription.class, this.getId(), GENgraphRelTypes.HAS_NAME);
		}
	}

}
