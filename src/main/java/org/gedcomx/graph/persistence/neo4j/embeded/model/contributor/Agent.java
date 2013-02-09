package org.gedcomx.graph.persistence.neo4j.embeded.model.contributor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.dao.impl.GENgraphNodeUtils;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.common.Identifier;
import org.gedcomx.graph.persistence.neo4j.embeded.model.common.TextValue;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.RelTypes;
import org.neo4j.graphdb.Node;

public class Agent extends GENgraphNode {

	private final List<Address> addresses;
	private final List<Account> onlineAccounts;
	private final List<Identifier> identifiers;
	private final List<TextValue> names;

	public Agent(final Node underlyingNode, final org.gedcomx.contributor.Agent gedcomXAgent) {
		super(underlyingNode, NodeTypes.AGENT);
		this.addresses = new LinkedList<>();
		this.onlineAccounts = new LinkedList<>();
		this.identifiers = new LinkedList<>();
		this.names = new LinkedList<>();
		this.setAgentProperties(gedcomXAgent);
	}

	public void addAddress(final Address address) {
		this.addresses.add(address);
		GENgraphNodeUtils.createRelationship(this.underlyingNode, RelTypes.HAS_ADDRESS, address.getUnderlyingNode());
	}

	public void addIdentifier(final Identifier identifier) {
		this.identifiers.add(identifier);
		GENgraphNodeUtils.createRelationship(this.underlyingNode, RelTypes.HAS_IDENTIFIER, identifier.getUnderlyingNode());
	}

	public void addName(final TextValue name) {
		this.names.add(name);
		GENgraphNodeUtils.createRelationship(this.underlyingNode, RelTypes.HAS_NAME, name.getUnderlyingNode());
	}

	public void addOnlineAccount(final Account onlineAccount) {
		this.onlineAccounts.add(onlineAccount);
		GENgraphNodeUtils.createRelationship(this.underlyingNode, RelTypes.HAS_ACCOUNT, onlineAccount.getUnderlyingNode());
	}

	public Collection<Address> getAddresses() {
		return this.addresses;
	}

	public List<ResourceReference> getEmails() {
		return this.getURIListProperties(NodeProperties.Agent.EMAILS);
	}

	public ResourceReference getHomepage() {
		final String homepage = (String) GENgraphNodeUtils.getNodeProperty(this.underlyingNode, NodeProperties.Agent.HOMEPAGE);
		return new ResourceReference(new URI(homepage));
	}

	public String getId() {
		return (String) GENgraphNodeUtils.getNodeProperty(this.underlyingNode, NodeProperties.Generic.ID);
	}

	public Collection<Identifier> getIdentifiers() {
		return this.identifiers;
	}

	public Collection<TextValue> getNames() {
		return this.names;
	}

	public Collection<Account> getOnlineAccounts() {
		return this.onlineAccounts;
	}

	public ResourceReference getOpenid() {
		final String openid = (String) GENgraphNodeUtils.getNodeProperty(this.underlyingNode, NodeProperties.Agent.OPENID);
		return new ResourceReference(new URI(openid));
	}

	public List<ResourceReference> getPhones() {
		return this.getURIListProperties(NodeProperties.Agent.PHONES);
	}

	private void setAgentProperties(final org.gedcomx.contributor.Agent gedcomXAgent) {
		this.setId(gedcomXAgent.getId());
		this.setHomepage(gedcomXAgent.getHomepage());
		this.setOpenid(gedcomXAgent.getOpenid());

		this.setEmails(gedcomXAgent.getEmails());
		this.setPhones(gedcomXAgent.getPhones());
	}

	public void setEmails(final List<ResourceReference> emails) {
		this.setURIListProperties(emails, NodeProperties.Agent.EMAILS);
	}

	public void setHomepage(final ResourceReference homepage) {
		GENgraphNodeUtils.addNodeProperty(this.underlyingNode, NodeProperties.Agent.HOMEPAGE, homepage.getResource().toString());
	}

	public void setId(final String id) {
		GENgraphNodeUtils.addNodeProperty(this.underlyingNode, NodeProperties.Generic.ID, id);
	}

	public void setOpenid(final ResourceReference openid) {
		GENgraphNodeUtils.addNodeProperty(this.underlyingNode, NodeProperties.Agent.HOMEPAGE, openid.getResource().toString());
	}

	public void setPhones(final List<ResourceReference> phones) {
		this.setURIListProperties(phones, NodeProperties.Agent.PHONES);
	}

}
