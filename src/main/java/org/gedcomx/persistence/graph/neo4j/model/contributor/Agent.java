package org.gedcomx.persistence.graph.neo4j.model.contributor;

import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAO;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.model.GENgraphNode;
import org.gedcomx.persistence.graph.neo4j.model.GENgraphTopLevelNode;
import org.gedcomx.persistence.graph.neo4j.model.common.Identifier;
import org.gedcomx.persistence.graph.neo4j.model.common.TextValue;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.utils.RelTypes;

public class Agent extends GENgraphNode implements GENgraphTopLevelNode {

	public Agent(final GENgraphDAO dao, final org.gedcomx.agent.Agent gedcomXAgent) throws MissingFieldException {
		super(dao, NodeTypes.AGENT, gedcomXAgent);
	}

	public void addAddress(final Address address) {
		this.addRelationship(RelTypes.HAS_ADDRESS, address);
	}

	public void addIdentifier(final Identifier identifier) {
		this.addRelationship(RelTypes.HAS_IDENTIFIER, identifier);
	}

	public void addName(final TextValue name) {
		this.addRelationship(RelTypes.HAS_NAME, name);
	}

	public void addOnlineAccount(final OnlineAccount onlineAccount) {
		this.addRelationship(RelTypes.HAS_ACCOUNT, onlineAccount);
	}

	@Override
	public void delete() {
		this.deleteReferences(Address.class, RelTypes.HAS_ADDRESS);
		this.deleteReferences(OnlineAccount.class, RelTypes.HAS_ACCOUNT);
		this.deleteReferences(Identifier.class, RelTypes.HAS_IDENTIFIER);
		this.deleteReferences(TextValue.class, RelTypes.HAS_NAME);
		this.deleteSelf();
	}

	public List<Address> getAddresses() {
		return this.getNodesByRelationship(Address.class, RelTypes.HAS_ADDRESS);
	}

	public List<ResourceReference> getEmails() {
		return this.getURIListProperties(NodeProperties.Agent.EMAILS);
	}

	@Override
	public org.gedcomx.agent.Agent getGedcomX() {
		final org.gedcomx.agent.Agent agent = new org.gedcomx.agent.Agent();

		agent.setEmails(this.getEmails());
		agent.setHomepage(this.getHomepage());
		agent.setId(this.getId());
		agent.setOpenid(this.getOpenid());
		agent.setPhones(this.getPhones());

		// extensible data?
		// links?

		return agent;
	}

	public ResourceReference getHomepage() {
		final String homepage = (String) this.getProperty(NodeProperties.Agent.HOMEPAGE);
		return new ResourceReference(new URI(homepage));
	}

	public String getId() {
		return (String) this.getProperty(NodeProperties.Generic.ID);
	}

	public List<Identifier> getIdentifiers() {
		return this.getNodesByRelationship(Identifier.class, RelTypes.HAS_IDENTIFIER);
	}

	public List<TextValue> getNames() {
		return this.getNodesByRelationship(TextValue.class, RelTypes.HAS_NAME);
	}

	public List<OnlineAccount> getOnlineAccounts() {
		return this.getNodesByRelationship(OnlineAccount.class, RelTypes.HAS_ACCOUNT);
	}

	public ResourceReference getOpenid() {
		final String openid = (String) this.getProperty(NodeProperties.Agent.OPENID);
		return new ResourceReference(new URI(openid));
	}

	public List<ResourceReference> getPhones() {
		return this.getURIListProperties(NodeProperties.Agent.PHONES);
	}

	public void setEmails(final List<ResourceReference> emails) {
		this.setURIListProperties(NodeProperties.Agent.EMAILS, emails);
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
	protected void setGedcomXRelations(final Object gedcomXObject) {
		final org.gedcomx.agent.Agent gedcomXAgent = (org.gedcomx.agent.Agent) gedcomXObject;

		for (final org.gedcomx.agent.Address gedcomXAddress : gedcomXAgent.getAddresses()) {
			this.addAddress(this.createNode(Address.class, gedcomXAddress));
		}
		for (final org.gedcomx.agent.OnlineAccount gedcomXOnlineAccount : gedcomXAgent.getAccounts()) {
			this.addOnlineAccount(this.createNode(OnlineAccount.class, gedcomXOnlineAccount));
		}
		for (final org.gedcomx.conclusion.Identifier gedcomXIdentifier : gedcomXAgent.getIdentifiers()) {
			this.addIdentifier(this.createNode(Identifier.class, gedcomXIdentifier));
		}
		for (final org.gedcomx.common.TextValue gedcomXName : gedcomXAgent.getNames()) {
			this.addName(this.createNode(TextValue.class, gedcomXName));
		}
	}

	public void setHomepage(final ResourceReference homepage) {
		this.setProperty(NodeProperties.Agent.HOMEPAGE, homepage.getResource().toString());
	}

	public void setId(final String id) {
		this.setProperty(NodeProperties.Generic.ID, id);
	}

	public void setOpenid(final ResourceReference openid) {
		this.setProperty(NodeProperties.Agent.HOMEPAGE, openid.getResource().toString());
	}

	public void setPhones(final List<ResourceReference> phones) {
		this.setURIListProperties(NodeProperties.Agent.PHONES, phones);
	}

}
