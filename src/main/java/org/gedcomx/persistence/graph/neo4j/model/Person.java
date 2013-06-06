package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exceptions.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.neo4j.graphdb.Node;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

@NodeType(NodeTypes.PERSON)
public class Person extends Conclusion {

    protected Person() throws MissingFieldException {
        super();
    }

    protected Person(final Node node) throws MissingFieldException,
            UnknownNodeType {
        super(node);
    }

    @Inject
    protected Person(final @Assisted org.gedcomx.conclusion.Person gedcomXPerson)
            throws MissingFieldException {
        super(gedcomXPerson);
    }

    public void addFact(final Fact fact) {
        NodeWrapper.nodeWrapperOperations.addRelationship(this,
                RelationshipTypes.HAS_FACT, fact);
    }

    public void addIdentifier(final Identifier identifier) {
        NodeWrapper.nodeWrapperOperations.addRelationship(this,
                RelationshipTypes.HAS_IDENTIFIER, identifier);
    }

    public void addName(final Name name) {
        NodeWrapper.nodeWrapperOperations.addRelationship(this,
                RelationshipTypes.HAS_NAME, name);
    }

    @Override
    protected void deleteAllConcreteReferences() {
        NodeWrapper.nodeWrapperOperations
                .deleteReferencedNode(this.getGender());

        NodeWrapper.nodeWrapperOperations
                .deleteReferencedNodes(this.getNames());
        NodeWrapper.nodeWrapperOperations
                .deleteReferencedNodes(this.getFacts());
    }

    public List<Fact> getFacts() {
        return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
                Fact.class, RelationshipTypes.HAS_FACT);
    }

    @Override
    public org.gedcomx.conclusion.Person getGedcomX() {
        final org.gedcomx.conclusion.Person gedcomXPerson = new org.gedcomx.conclusion.Person();

        this.getGedcomXConclusion(gedcomXPerson);

        gedcomXPerson.setLiving(this.getLiving());

        gedcomXPerson.setGender(this.getGender().getGedcomX());

        gedcomXPerson.setFacts(NodeWrapper.nodeWrapperOperations
                .getGedcomXList(org.gedcomx.conclusion.Fact.class,
                        this.getFacts()));
        gedcomXPerson.setIdentifiers(NodeWrapper.nodeWrapperOperations
                .getGedcomXList(org.gedcomx.conclusion.Identifier.class,
                        this.getIdentifiers()));
        gedcomXPerson.setNames(NodeWrapper.nodeWrapperOperations
                .getGedcomXList(org.gedcomx.conclusion.Name.class,
                        this.getNames()));

        return gedcomXPerson;
    }

    public Gender getGender() {
        return NodeWrapper.nodeWrapperOperations.getNodeByRelationship(this,
                Gender.class, RelationshipTypes.GENDER);
    }

    public List<Identifier> getIdentifiers() {
        return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
                Identifier.class, RelationshipTypes.HAS_IDENTIFIER);
    }

    public Boolean getLiving() {
        return (Boolean) NodeWrapper.nodeWrapperOperations.getProperty(this,
                ConclusionProperties.LIVING);
    }

    public List<Name> getNames() {
        return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
                Name.class, RelationshipTypes.HAS_NAME);
    }

    @Override
    protected void resolveConcreteReferences() {
        for (final Fact f : this.getFacts()) {
            f.resolveReferences();
        }
    }

    @Override
    protected void setGedcomXConcreteProperties(final Object gedcomXObject) {
        final org.gedcomx.conclusion.Person gedcomXPerson = (org.gedcomx.conclusion.Person) gedcomXObject;

        this.setLiving(gedcomXPerson.getLiving());
    }

    @Override
    protected void setGedcomXConcreteRelations(final Object gedcomXObject)
            throws MissingFieldException {
        final org.gedcomx.conclusion.Person gedcomXPerson = (org.gedcomx.conclusion.Person) gedcomXObject;

        if (gedcomXPerson.getGender() != null) {
            this.setGender(new Gender(gedcomXPerson.getGender()));
        }

        if (gedcomXPerson.getIdentifiers() != null) {
            for (final org.gedcomx.conclusion.Identifier identifier : gedcomXPerson
                    .getIdentifiers()) {
                this.addIdentifier(new Identifier(identifier));
            }
        }
        if (gedcomXPerson.getNames() != null) {
            for (final org.gedcomx.conclusion.Name name : gedcomXPerson
                    .getNames()) {
                this.addName(new Name(name));
            }
        }
        if (gedcomXPerson.getFacts() != null) {
            for (final org.gedcomx.conclusion.Fact fact : gedcomXPerson
                    .getFacts()) {
                this.addFact(new Fact(fact));
            }
        }
    }

    public void setGender(final Gender gender) {
        NodeWrapper.nodeWrapperOperations.createRelationship(this,
                RelationshipTypes.GENDER, gender);
    }

    public void setLiving(final Boolean living) {
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                ConclusionProperties.LIVING, living);
    }

    @Override
    protected void setRequiredProperties(final Object... properties)
            throws MissingFieldException {
        return;
    }

    @Override
    protected void validateUnderlyingNode() throws MissingFieldException {
        return;
    }
}
