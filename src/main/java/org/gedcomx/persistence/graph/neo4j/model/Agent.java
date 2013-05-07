package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exceptions.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.AgentProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.neo4j.graphdb.Node;

@NodeType(NodeTypes.AGENT)
public class Agent extends NodeWrapper {

    public Agent() throws MissingFieldException {
        super();
    }

    protected Agent(final Node node) throws UnknownNodeType,
            MissingFieldException {
        super(node);
    }

    public Agent(final org.gedcomx.agent.Agent gedcomXAgent)
            throws MissingFieldException {
        super(gedcomXAgent);
    }

    public void addAddress(final Address address) {
        this.addRelationship(RelationshipTypes.HAS_ADDRESS, address);
    }

    public void addIdentifier(final Identifier identifier) {
        this.addRelationship(RelationshipTypes.HAS_IDENTIFIER, identifier);
    }

    public void addName(final TextValue name) {
        this.addRelationship(RelationshipTypes.HAS_NAME, name);
    }

    public void addOnlineAccount(final OnlineAccount onlineAccount) {
        this.addRelationship(RelationshipTypes.HAS_ACCOUNT, onlineAccount);
    }

    @Override
    public void deleteAllReferences() {
        this.deleteReferencedNodes(this.getAddresses());
        this.deleteReferencedNodes(this.getOnlineAccounts());
        this.deleteReferencedNodes(this.getIdentifiers());
        this.deleteReferencedNodes(this.getNames());
    }

    public List<Address> getAddresses() {
        return this.getNodesByRelationship(Address.class,
                RelationshipTypes.HAS_ADDRESS);
    }

    public List<ResourceReference> getEmails() {
        return this.getURIListProperties(AgentProperties.EMAILS);
    }

    @Override
    public org.gedcomx.agent.Agent getGedcomX() {
        final org.gedcomx.agent.Agent gedcomXAgent = new org.gedcomx.agent.Agent();

        gedcomXAgent.setEmails(this.getEmails());
        gedcomXAgent.setHomepage(this.getHomepage());
        gedcomXAgent.setId(this.getId());
        gedcomXAgent.setOpenid(this.getOpenid());
        gedcomXAgent.setPhones(this.getPhones());

        gedcomXAgent.setAddresses(this.getGedcomXList(
                org.gedcomx.agent.Address.class, this.getAddresses()));
        gedcomXAgent
                .setAccounts(this.getGedcomXList(
                        org.gedcomx.agent.OnlineAccount.class,
                        this.getOnlineAccounts()));
        gedcomXAgent.setNames(this.getGedcomXList(
                org.gedcomx.common.TextValue.class, this.getNames()));
        gedcomXAgent
                .setIdentifiers(this.getGedcomXList(
                        org.gedcomx.conclusion.Identifier.class,
                        this.getIdentifiers()));

        return gedcomXAgent;
    }

    public ResourceReference getHomepage() {
        final String homepage = (String) this
                .getProperty(AgentProperties.HOMEPAGE);
        return new ResourceReference(new URI(homepage));
    }

    public String getId() {
        return (String) this.getProperty(GenericProperties.ID);
    }

    public List<Identifier> getIdentifiers() {
        return this.getNodesByRelationship(Identifier.class,
                RelationshipTypes.HAS_IDENTIFIER);
    }

    public List<TextValue> getNames() {
        return this.getNodesByRelationship(TextValue.class,
                RelationshipTypes.HAS_NAME);
    }

    public List<OnlineAccount> getOnlineAccounts() {
        return this.getNodesByRelationship(OnlineAccount.class,
                RelationshipTypes.HAS_ACCOUNT);
    }

    public ResourceReference getOpenid() {
        return new ResourceReference(new URI(
                (String) this.getProperty(AgentProperties.OPENID)));
    }

    public List<ResourceReference> getPhones() {
        return this.getURIListProperties(AgentProperties.PHONES);
    }

    @Override
    protected ResourceReference getResourceReference() {
        return new ResourceReference(new URI(this.getId()));
    }

    @Override
    protected URI getURI() {
        return new URI(this.getId());
    }

    @Override
    public void resolveReferences() {
        return;
    }

    public void setEmails(final List<ResourceReference> emails) {
        this.setURIListProperties(AgentProperties.EMAILS, emails);
    }

    @Override
    protected void setGedcomXProperties(final Object gedcomXObject) {
        final org.gedcomx.agent.Agent gedcomXAgent = (org.gedcomx.agent.Agent) gedcomXObject;
        this.setId(gedcomXAgent.getId());
        this.setHomepage(gedcomXAgent.getHomepage());
        this.setOpenid(gedcomXAgent.getOpenid());

        this.setEmails(gedcomXAgent.getEmails());
        this.setPhones(gedcomXAgent.getPhones());
    }

    @Override
    protected void setGedcomXRelations(final Object gedcomXObject)
            throws MissingFieldException {
        final org.gedcomx.agent.Agent gedcomXAgent = (org.gedcomx.agent.Agent) gedcomXObject;

        if (gedcomXAgent.getAddresses() != null) {
            for (final org.gedcomx.agent.Address gedcomXAddress : gedcomXAgent
                    .getAddresses()) {
                this.addAddress(new Address(gedcomXAddress));
            }
        }
        if (gedcomXAgent.getAccounts() != null) {
            for (final org.gedcomx.agent.OnlineAccount gedcomXOnlineAccount : gedcomXAgent
                    .getAccounts()) {
                this.addOnlineAccount(new OnlineAccount(gedcomXOnlineAccount));
            }
        }
        if (gedcomXAgent.getIdentifiers() != null) {
            for (final org.gedcomx.conclusion.Identifier gedcomXIdentifier : gedcomXAgent
                    .getIdentifiers()) {
                this.addIdentifier(new Identifier(gedcomXIdentifier));
            }
        }
        if (gedcomXAgent.getNames() != null) {
            for (final org.gedcomx.common.TextValue gedcomXName : gedcomXAgent
                    .getNames()) {
                this.addName(new TextValue(gedcomXName));
            }
        }
    }

    public void setHomepage(final ResourceReference homepage) {
        this.setProperty(AgentProperties.HOMEPAGE, homepage);
    }

    public void setId(final String id) {
        this.setProperty(GenericProperties.ID, id);
    }

    public void setOpenid(final ResourceReference openid) {
        this.setProperty(AgentProperties.HOMEPAGE, openid);
    }

    public void setPhones(final List<ResourceReference> phones) {
        this.setURIListProperties(AgentProperties.PHONES, phones);
    }

    @Override
    protected void setRequiredProperties(final Object... properties) {
        return;
    }

    @Override
    protected void validateUnderlyingNode() {
        return;
    }
}
