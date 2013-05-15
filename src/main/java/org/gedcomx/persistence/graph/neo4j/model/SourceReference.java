package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.exceptions.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.SourceProperties;
import org.gedcomx.persistence.graph.neo4j.utils.Validation;
import org.neo4j.graphdb.Node;

@NodeType(NodeTypes.SOURCE_REFERENCE)
public class SourceReference extends NodeWrapper {

    protected SourceReference(final Node node) throws MissingFieldException,
            UnknownNodeType {
        super(node);
    }

    protected SourceReference(
            final org.gedcomx.source.SourceReference gedcomXSourceReference)
            throws MissingFieldException {
        super(gedcomXSourceReference);
    }

    protected SourceReference(final SourceDescription description)
            throws MissingFieldException {
        super(new Object[] { description });
    }

    @Override
    protected void deleteAllReferences() {

    }

    public Attribution getAttribution() {
        return this.getNodeByRelationship(Attribution.class,
                RelationshipTypes.ATTRIBUTION);
    }

    public SourceDescription getDescription() {
        return this.getNodeByRelationship(SourceDescription.class,
                RelationshipTypes.DESCRIPTION);
    }

    @Override
    public org.gedcomx.source.SourceReference getGedcomX() {
        final org.gedcomx.source.SourceReference gedcomXSourceReference = new org.gedcomx.source.SourceReference();

        final Attribution attr = this.getAttribution();
        if (attr != null) {
            gedcomXSourceReference.setAttribution(attr.getGedcomX());
        }
        gedcomXSourceReference
                .setDescriptionRef(this.getDescription().getURI());

        return gedcomXSourceReference;
    }

    public NodeWrapper getParentNode() {
        return this.getParentNode(RelationshipTypes.HAS_SOURCE_REFERENCE);
    }

    @Override
    public void resolveReferences() {
        this.createReferenceRelationship(RelationshipTypes.DESCRIPTION,
                SourceProperties.SOURCE_DESCRIPTION_REFERENCE);
    }

    public void setAttribution(final Attribution attribution) {
        this.createRelationship(RelationshipTypes.ATTRIBUTION, attribution);
    }

    public void setDescription(final SourceDescription description) {
        this.createReferenceRelationship(RelationshipTypes.DESCRIPTION,
                description);
    }

    @Override
    protected void setGedcomXProperties(final Object gedcomXObject) {
        return;
    }

    @Override
    protected void setGedcomXRelations(final Object gedcomXObject)
            throws MissingFieldException {
        final org.gedcomx.source.SourceReference gedcomXSourceReference = (org.gedcomx.source.SourceReference) gedcomXObject;

        if (gedcomXSourceReference.getAttribution() != null) {
            this.setAttribution(new Attribution(gedcomXSourceReference
                    .getAttribution()));
        }

        this.setProperty(SourceProperties.SOURCE_DESCRIPTION_REFERENCE,
                gedcomXSourceReference.getDescriptionRef());
    }

    @Override
    protected void setRequiredProperties(final Object... properties) {
        this.setDescription((SourceDescription) properties[0]);
    }

    @Override
    protected void validateUnderlyingNode() throws MissingFieldException {
        if (Validation.nullOrEmpty(this.getDescription())) {
            throw new MissingRequiredRelationshipException(
                    this.getAnnotatedNodeType(),
                    RelationshipTypes.DESCRIPTION.toString());
        }
    }
}
