package org.gedcomx.persistence.graph.neo4j.model;

import java.util.ArrayList;
import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exceptions.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.utils.Validation;
import org.gedcomx.types.NamePartQualifierType;
import org.gedcomx.types.NamePartType;
import org.neo4j.graphdb.Node;

@NodeType(NodeTypes.NAME_PART)
public class NamePart extends NodeWrapper {

    protected NamePart(final Node node) throws MissingFieldException,
            UnknownNodeType {
        super(node);
    }

    protected NamePart(final org.gedcomx.conclusion.NamePart gedcomXNamePart)
            throws MissingFieldException {
        super(gedcomXNamePart);
    }

    protected NamePart(final String value) throws MissingFieldException {
        super(value);
    }

    public void addQualifier(final Qualifier qualifier) {
        this.addRelationship(RelationshipTypes.HAS_QUALIFIER, qualifier);
    }

    @Override
    protected void deleteAllReferences() {
        return;
    }

    @Override
    public org.gedcomx.conclusion.NamePart getGedcomX() {
        final org.gedcomx.conclusion.NamePart gedcomXNamePart = new org.gedcomx.conclusion.NamePart();

        gedcomXNamePart.setType(this.getType());
        gedcomXNamePart.setKnownType(this.getKnownType());
        gedcomXNamePart.setValue(this.getValue());
        gedcomXNamePart.setQualifiers(this.getGedcomXList(
                org.gedcomx.common.Qualifier.class, this.getQualifiers()));

        return gedcomXNamePart;
    }

    public List<NamePartQualifierType> getKnownQualifiers() {
        final List<ResourceReference> references = this
                .getURIListProperties(ConclusionProperties.QUALIFIERS);

        final List<NamePartQualifierType> result = new ArrayList<NamePartQualifierType>();
        for (final ResourceReference ref : references) {
            result.add(NamePartQualifierType.fromQNameURI(ref.getResource()));
        }
        return result;
    }

    public NamePartType getKnownType() {
        return NamePartType.fromQNameURI(this.getType());
    }

    public NameForm getNameForm() {
        return (NameForm) this.getParentNode(RelationshipTypes.HAS_NAME_PART);
    }

    public List<Qualifier> getQualifiers() {
        return this.getNodesByRelationship(Qualifier.class,
                RelationshipTypes.HAS_QUALIFIER);
    }

    @Deprecated
    public URI getType() {
        return new URI((String) this.getProperty(GenericProperties.TYPE));
    }

    public String getValue() {
        return (String) this.getProperty(GenericProperties.VALUE);
    }

    @Override
    public void resolveReferences() {
        return;
    }

    @Override
    protected void setGedcomXProperties(final Object gedcomXObject) {
        final org.gedcomx.conclusion.NamePart gedcomXNamePart = (org.gedcomx.conclusion.NamePart) gedcomXObject;

        this.setType(gedcomXNamePart.getType());
        this.setValue(gedcomXNamePart.getValue());

    }

    @Override
    protected void setGedcomXRelations(final Object gedcomXObject)
            throws MissingFieldException {
        final org.gedcomx.conclusion.NamePart gedcomXNamePart = (org.gedcomx.conclusion.NamePart) gedcomXObject;

        if (gedcomXNamePart.getQualifiers() != null) {
            for (final org.gedcomx.common.Qualifier q : gedcomXNamePart
                    .getQualifiers()) {
                this.addQualifier(new Qualifier(q));
            }
        }

        return;
    }

    public void setKnownQualifiers(final List<NamePartQualifierType> qualifiers) {
        this.setProperty(ConclusionProperties.QUALIFIERS, qualifiers);
    }

    public void setKnownType(final NamePartType type) {
        this.setType(type.toQNameURI());
    }

    @Override
    protected void setRequiredProperties(final Object... properties)
            throws MissingFieldException {
        this.setValue((String) properties[0]);
    }

    @Deprecated
    public void setType(final URI type) {
        this.setProperty(GenericProperties.TYPE, type);
    }

    public void setValue(final String value) {
        this.setProperty(GenericProperties.VALUE, value);
    }

    @Override
    protected void validateUnderlyingNode() throws MissingFieldException {
        if (Validation.nullOrEmpty(this.getValue())) {
            throw new MissingRequiredPropertyException(
                    this.getAnnotatedNodeType(),
                    GenericProperties.VALUE.toString());
        }
    }

}
