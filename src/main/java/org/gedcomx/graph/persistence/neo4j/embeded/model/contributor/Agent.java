package org.gedcomx.graph.persistence.neo4j.embeded.model.contributor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredPropertyException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.common.Identifier;
import org.gedcomx.graph.persistence.neo4j.embeded.model.common.TextValue;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.RelTypes;

public class Agent extends GENgraphNode {

	private final List<Address> addresses;
	private final List<OnlineAccount> onlineAccounts;
	private final List<Identifier> identifiers;
	private final List<TextValue> names;

	public Agent(final org.gedcomx.contributor.Agent gedcomXAgent) throws MissingRequiredPropertyException {
		super(NodeTypes.AGENT, gedcomXAgent);
		this.addresses = new LinkedList<>();
		this.onlineAccounts = new LinkedList<>();
		this.identifiers = new LinkedList<>();
		this.names = new LinkedList<>();

		for (final org.gedcomx.contributor.Address gedcomXAddress : gedcomXAgent.getAddresses()) {
			this.addAddress(new Address(gedcomXAddress));
		}
		for (final org.gedcomx.contributor.OnlineAccount gedcomXOnlineAccount : gedcomXAgent.getAccounts()) {
			this.addOnlineAccount(new OnlineAccount(gedcomXOnlineAccount));
		}
		for (final org.gedcomx.conclusion.Identifier gedcomXIdentifier : gedcomXAgent.getIdentifiers()) {
			this.addIdentifier(new Identifier(gedcomXIdentifier));
		}
		for (final org.gedcomx.common.TextValue gedcomXName : gedcomXAgent.getNames()) {
			this.addName(new TextValue(gedcomXName));
		}
	}

	public void addAddress(final Address address) {
		this.addresses.add(address);
		this.createRelationship(RelTypes.HAS_ADDRESS, address);
	}

	public void addIdentifier(final Identifier identifier) {
		this.identifiers.add(identifier);
		this.createRelationship(RelTypes.HAS_IDENTIFIER, identifier);
	}

	public void addName(final TextValue name) {
		this.names.add(name);
		this.createRelationship(RelTypes.HAS_NAME, name);
	}

	public void addOnlineAccount(final OnlineAccount onlineAccount) {
		this.onlineAccounts.add(onlineAccount);
		this.createRelationship(RelTypes.HAS_ACCOUNT, onlineAccount);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingRequiredPropertyException {
		return;
	}

	public Collection<Address> getAddresses() {
		return this.addresses;
	}

	public List<ResourceReference> getEmails() {
		return this.getURIListProperties(NodeProperties.Agent.EMAILS);
	}

	public ResourceReference getHomepage() {
		final String homepage = (String) this.getProperty(NodeProperties.Agent.HOMEPAGE);
		return new ResourceReference(new URI(homepage));
	}

	public String getId() {
		return (String) this.getProperty(NodeProperties.Generic.ID);
	}

	public Collection<Identifier> getIdentifiers() {
		return this.identifiers;
	}

	public Collection<TextValue> getNames() {
		return this.names;
	}

	public Collection<OnlineAccount> getOnlineAccounts() {
		return this.onlineAccounts;
	}

	public ResourceReference getOpenid() {
		final String openid = (String) this.getProperty(NodeProperties.Agent.OPENID);
		return new ResourceReference(new URI(openid));
	}

	public List<ResourceReference> getPhones() {
		return this.getURIListProperties(NodeProperties.Agent.PHONES);
	}

	public void setEmails(final List<ResourceReference> emails) {
		this.setURIListProperties(emails, NodeProperties.Agent.EMAILS);
	}

	public void setHomepage(final ResourceReference homepage) {
		this.setProperty(NodeProperties.Agent.HOMEPAGE, homepage.getResource().toString());
	}

	public void setId(final String id) {
		this.setProperty(NodeProperties.Generic.ID, id);
	}

	@Override
	protected void setInitialProperties(final Object gedcomXObject) {
		final org.gedcomx.contributor.Agent gedcomXAgent = (org.gedcomx.contributor.Agent) gedcomXObject;
		this.setId(gedcomXAgent.getId());
		this.setHomepage(gedcomXAgent.getHomepage());
		this.setOpenid(gedcomXAgent.getOpenid());

		this.setEmails(gedcomXAgent.getEmails());
		this.setPhones(gedcomXAgent.getPhones());
	}

	public void setOpenid(final ResourceReference openid) {
		this.setProperty(NodeProperties.Agent.HOMEPAGE, openid.getResource().toString());
	}

	public void setPhones(final List<ResourceReference> phones) {
		this.setURIListProperties(phones, NodeProperties.Agent.PHONES);
	}

}
