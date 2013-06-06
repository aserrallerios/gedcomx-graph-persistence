package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.exceptions.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.utils.Validation;
import org.neo4j.graphdb.Node;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

@NodeType(NodeTypes.PLACE_DESCRIPTION)
public class PlaceDescription extends Conclusion {

    protected PlaceDescription(final Node node) throws MissingFieldException,
            UnknownNodeType {
        super(node);
    }

    @Inject
    protected PlaceDescription(
            final @Assisted org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription)
            throws MissingFieldException {
        super(gedcomXPlaceDescription);
    }

    protected PlaceDescription(final String nameValue)
            throws MissingFieldException {
        super(new Object[] { nameValue });
    }

    public void addIdentifier(final Identifier identifier) {
        NodeWrapper.nodeWrapperOperations.addRelationship(this,
                RelationshipTypes.HAS_IDENTIFIER, identifier);
    }

    public void addMedia(final SourceReference media) {
        NodeWrapper.nodeWrapperOperations.addRelationship(this,
                RelationshipTypes.HAS_MEDIA, media);
    }

    public void addName(final TextValue name) {
        NodeWrapper.nodeWrapperOperations.addRelationship(this,
                RelationshipTypes.HAS_NAME, name);
    }

    @Override
    protected void deleteAllConcreteReferences() {
        NodeWrapper.nodeWrapperOperations.deleteReferencedNodes(this
                .getIdentifiers());
        NodeWrapper.nodeWrapperOperations
                .deleteReferencedNodes(this.getNames());
        NodeWrapper.nodeWrapperOperations
                .deleteReferencedNodes(this.getMedia());
    }

    public URI getAbout() {
        return new URI((String) NodeWrapper.nodeWrapperOperations.getProperty(
                this, GenericProperties.ABOUT));
    }

    public Boolean getExtracted() {
        return (Boolean) NodeWrapper.nodeWrapperOperations.getProperty(this,
                ConclusionProperties.EXTRACTED);
    }

    @Override
    public org.gedcomx.conclusion.PlaceDescription getGedcomX() {
        final org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription = new org.gedcomx.conclusion.PlaceDescription();

        this.getGedcomXConclusion(gedcomXPlaceDescription);

        gedcomXPlaceDescription.setExtracted(this.getExtracted());
        gedcomXPlaceDescription.setLatitude(this.getLatitude());
        gedcomXPlaceDescription.setLongitude(this.getLongitude());
        gedcomXPlaceDescription.setSpatialDescription(this
                .getSpatialDescription());

        final org.gedcomx.conclusion.Date date = new org.gedcomx.conclusion.Date();
        date.setFormal(this.getTemporalDescriptionFormal());
        date.setOriginal(this.getTemporalDescriptionOriginal());
        gedcomXPlaceDescription.setTemporalDescription(date);

        gedcomXPlaceDescription.setNames(NodeWrapper.nodeWrapperOperations
                .getGedcomXList(org.gedcomx.common.TextValue.class,
                        this.getNames()));
        gedcomXPlaceDescription.setMedia(NodeWrapper.nodeWrapperOperations
                .getGedcomXList(org.gedcomx.source.SourceReference.class,
                        this.getMedia()));
        gedcomXPlaceDescription
                .setIdentifiers(NodeWrapper.nodeWrapperOperations
                        .getGedcomXList(
                                org.gedcomx.conclusion.Identifier.class,
                                this.getIdentifiers()));

        return gedcomXPlaceDescription;
    }

    public List<Identifier> getIdentifiers() {
        return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
                Identifier.class, RelationshipTypes.HAS_IDENTIFIER);
    }

    public Double getLatitude() {
        return (Double) NodeWrapper.nodeWrapperOperations.getProperty(this,
                ConclusionProperties.LATITUDE);

    }

    public Double getLongitude() {
        return (Double) NodeWrapper.nodeWrapperOperations.getProperty(this,
                ConclusionProperties.LONGITUDE);
    }

    public List<SourceReference> getMedia() {
        return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
                SourceReference.class, RelationshipTypes.HAS_MEDIA);
    }

    public List<TextValue> getNames() {
        return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
                TextValue.class, RelationshipTypes.HAS_NAME);
    }

    public ResourceReference getSpatialDescription() {
        return new ResourceReference(new URI(
                (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
                        ConclusionProperties.SPATIAL_DESCRIPTION)));
    }

    public String getTemporalDescriptionFormal() {
        return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
                ConclusionProperties.TEMPORAL_DESCRIPTION_FORMAL);

    }

    public String getTemporalDescriptionOriginal() {
        return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
                ConclusionProperties.TEMPORAL_DESCRIPTION_ORIGINAL);

    }

    public URI getType() {
        return new URI((String) NodeWrapper.nodeWrapperOperations.getProperty(
                this, GenericProperties.TYPE));
    }

    @Override
    protected void resolveConcreteReferences() {
        return;
    }

    public void setAbout(final URI about) {
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                GenericProperties.ABOUT, about);
    }

    public void setExtracted(final Boolean extracted) {
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                ConclusionProperties.EXTRACTED, extracted);
    }

    @Override
    protected void setGedcomXConcreteProperties(final Object gedcomXObject) {
        final org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription = (org.gedcomx.conclusion.PlaceDescription) gedcomXObject;

        this.setExtracted(gedcomXPlaceDescription.getExtracted());
        this.setType(gedcomXPlaceDescription.getType());
        this.setLatitude(gedcomXPlaceDescription.getLatitude());
        this.setLongitude(gedcomXPlaceDescription.getLatitude());
        this.setSpatialDescription(gedcomXPlaceDescription
                .getSpatialDescription());

        if (gedcomXPlaceDescription.getTemporalDescription() != null) {
            this.setTemporalDescriptionOriginal(gedcomXPlaceDescription
                    .getTemporalDescription().getOriginal());
            this.setTemporalDescriptionFormal(gedcomXPlaceDescription
                    .getTemporalDescription().getFormal());
        }

    }

    @Override
    protected void setGedcomXConcreteRelations(final Object gedcomXObject)
            throws MissingFieldException {
        final org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription = (org.gedcomx.conclusion.PlaceDescription) gedcomXObject;

        for (final org.gedcomx.common.TextValue gedcomXName : gedcomXPlaceDescription
                .getNames()) {
            this.addName(new TextValue(gedcomXName));
        }
        if (gedcomXPlaceDescription.getIdentifiers() != null) {
            for (final org.gedcomx.conclusion.Identifier gedcomXIdentifier : gedcomXPlaceDescription
                    .getIdentifiers()) {
                this.addIdentifier(new Identifier(gedcomXIdentifier));
            }
        }
        if (gedcomXPlaceDescription.getMedia() != null) {
            for (final org.gedcomx.source.SourceReference gedcomXsourceRef : gedcomXPlaceDescription
                    .getMedia()) {
                this.addSourceReference(new SourceReference(gedcomXsourceRef));
            }
        }
    }

    public void setLatitude(final Double latitude) {
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                ConclusionProperties.LATITUDE, latitude);

    }

    public void setLongitude(final Double longitude) {
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                ConclusionProperties.LONGITUDE, longitude);

    }

    @Override
    protected void setRequiredProperties(final Object... properties)
            throws MissingFieldException {
        this.addName(new TextValue((String) properties[0]));
    }

    public void setSpatialDescription(final ResourceReference spatialDescription) {
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                ConclusionProperties.SPATIAL_DESCRIPTION, spatialDescription);
    }

    public void setTemporalDescriptionFormal(final String formal) {
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                ConclusionProperties.TEMPORAL_DESCRIPTION_FORMAL, formal);

    }

    public void setTemporalDescriptionOriginal(final String original) {
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                ConclusionProperties.TEMPORAL_DESCRIPTION_ORIGINAL, original);

    }

    public void setType(final URI type) {
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                GenericProperties.TYPE, type);
    }

    @Override
    protected void validateUnderlyingNode() throws MissingFieldException {
        if (Validation.nullOrEmpty(this.getNames())) {
            throw new MissingRequiredRelationshipException(
                    NodeWrapper.nodeWrapperOperations
                            .getAnnotatedNodeType(this),
                    this.getId(), RelationshipTypes.HAS_NAME.toString());
        }
    }

}
