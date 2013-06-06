package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

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
import org.gedcomx.types.RelationshipType;
import org.neo4j.graphdb.Node;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

@NodeType(NodeTypes.RELATIONSHIP)
public class Relationship extends Conclusion {

    protected Relationship(final Node node) throws MissingFieldException,
            UnknownNodeType {
        super(node);
    }

    @Inject
    protected Relationship(
            final @Assisted org.gedcomx.conclusion.Relationship gedcomXRelationship)
            throws MissingFieldException {
        super(gedcomXRelationship);
    }

    protected Relationship(final Person p1, final Person p2)
            throws MissingFieldException {
        super(p1, p2);
    }

    public void addFact(final Fact fact) {
        NodeWrapper.nodeWrapperOperations.addRelationship(this,
                RelationshipTypes.HAS_FACT, fact);
    }

    public void addIdentifier(final Identifier identifier) {
        NodeWrapper.nodeWrapperOperations.addRelationship(this,
                RelationshipTypes.HAS_IDENTIFIER, identifier);
    }

    @Override
    protected void deleteAllConcreteReferences() {
        NodeWrapper.nodeWrapperOperations.deleteReferencedNodes(this
                .getIdentifiers());
        NodeWrapper.nodeWrapperOperations
                .deleteReferencedNodes(this.getFacts());

        NodeWrapper.nodeWrapperOperations.deleteReference(this,
                RelationshipTypes.PERSON1);
        NodeWrapper.nodeWrapperOperations.deleteReference(this,
                RelationshipTypes.PERSON2);
    }

    public List<Fact> getFacts() {
        return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
                Fact.class, RelationshipTypes.HAS_FACT);
    }

    @Override
    public org.gedcomx.conclusion.Relationship getGedcomX() {
        final org.gedcomx.conclusion.Relationship gedcomXRelationship = new org.gedcomx.conclusion.Relationship();

        this.getGedcomXConclusion(gedcomXRelationship);

        gedcomXRelationship.setFacts(NodeWrapper.nodeWrapperOperations
                .getGedcomXList(org.gedcomx.conclusion.Fact.class,
                        this.getFacts()));
        gedcomXRelationship.setIdentifiers(NodeWrapper.nodeWrapperOperations
                .getGedcomXList(org.gedcomx.conclusion.Identifier.class,
                        this.getIdentifiers()));

        gedcomXRelationship.setKnownType(this.getKnownType());
        gedcomXRelationship.setType(this.getType());

        gedcomXRelationship
                .setPerson1(this.getPerson1().getResourceReference());
        gedcomXRelationship
                .setPerson2(this.getPerson2().getResourceReference());

        return gedcomXRelationship;
    }

    public List<Identifier> getIdentifiers() {
        return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
                Identifier.class, RelationshipTypes.HAS_IDENTIFIER);
    }

    public RelationshipType getKnownType() {
        return RelationshipType.fromQNameURI(this.getType());
    }

    public Person getPerson1() {
        return NodeWrapper.nodeWrapperOperations.getNodeByRelationship(this,
                Person.class, RelationshipTypes.PERSON1);
    }

    public Person getPerson2() {
        return NodeWrapper.nodeWrapperOperations.getNodeByRelationship(this,
                Person.class, RelationshipTypes.PERSON2);
    }

    @Deprecated
    public URI getType() {
        return new URI((String) NodeWrapper.nodeWrapperOperations.getProperty(
                this, GenericProperties.TYPE));
    }

    @Override
    protected void resolveConcreteReferences() {
        NodeWrapper.nodeWrapperOperations.createReferenceRelationship(this,
                RelationshipTypes.PERSON1,
                ConclusionProperties.PERSON1_REFERENCE);
        NodeWrapper.nodeWrapperOperations.createReferenceRelationship(this,
                RelationshipTypes.PERSON2,
                ConclusionProperties.PERSON2_REFERENCE);
        for (final Fact f : this.getFacts()) {
            f.resolveReferences();
        }
    }

    @Override
    protected void setGedcomXConcreteProperties(final Object gedcomXObject) {
        final org.gedcomx.conclusion.Relationship gedcomXRelationship = (org.gedcomx.conclusion.Relationship) gedcomXObject;

        this.setType(gedcomXRelationship.getType());
    }

    @Override
    protected void setGedcomXConcreteRelations(final Object gedcomXObject)
            throws MissingFieldException {
        final org.gedcomx.conclusion.Relationship gedcomXRelationship = (org.gedcomx.conclusion.Relationship) gedcomXObject;

        if (gedcomXRelationship.getFacts() != null) {
            for (final org.gedcomx.conclusion.Fact fact : gedcomXRelationship
                    .getFacts()) {
                this.addFact(new Fact(fact));
            }
        }
        if (gedcomXRelationship.getIdentifiers() != null) {
            for (final org.gedcomx.conclusion.Identifier identifier : gedcomXRelationship
                    .getIdentifiers()) {
                this.addIdentifier(new Identifier(identifier));
            }
        }

        NodeWrapper.nodeWrapperOperations.setProperty(this,
                ConclusionProperties.PERSON1_REFERENCE,
                gedcomXRelationship.getPerson1());
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                ConclusionProperties.PERSON2_REFERENCE,
                gedcomXRelationship.getPerson2());
    }

    public void setKnownType(final RelationshipType type) {
        this.setType(type.toQNameURI());
    }

    public void setPerson1(final Person person1) {
        NodeWrapper.nodeWrapperOperations.createReferenceRelationship(this,
                RelationshipTypes.PERSON1, person1);
    }

    public void setPerson2(final Person person2) {
        NodeWrapper.nodeWrapperOperations.createReferenceRelationship(this,
                RelationshipTypes.PERSON2, person2);
    }

    @Override
    protected void setRequiredProperties(final Object... properties)
            throws MissingFieldException {
        this.setPerson1((Person) properties[0]);
        this.setPerson2((Person) properties[1]);
    }

    @Deprecated
    public void setType(final URI type) {
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                GenericProperties.TYPE, type);
    }

    @Override
    protected void validateUnderlyingNode() throws MissingFieldException {
        if (Validation.nullOrEmpty(this.getPerson1())) {
            throw new MissingRequiredRelationshipException(
                    NodeWrapper.nodeWrapperOperations
                            .getAnnotatedNodeType(this),
                    this.getId(), RelationshipTypes.PERSON1.toString());
        }
        if (Validation.nullOrEmpty(this.getPerson2())) {
            throw new MissingRequiredRelationshipException(
                    NodeWrapper.nodeWrapperOperations
                            .getAnnotatedNodeType(this),
                    this.getId(), RelationshipTypes.PERSON2.toString());
        }
    }

}
