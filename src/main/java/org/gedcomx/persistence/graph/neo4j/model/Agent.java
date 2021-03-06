package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.AgentProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.neo4j.graphdb.Node;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

@NodeType(NodeTypes.AGENT)
public class Agent extends NodeWrapper {

	protected Agent() {
		super();
	}

	protected Agent(final Node node) {
		super(node);
	}

	@Inject
	protected Agent(final @Assisted org.gedcomx.agent.Agent gedcomXAgent) {
		super(gedcomXAgent);
	}

	public Address addAddress() {
		return this.addAddress(new Address());
	}

	private Address addAddress(final Address address) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_ADDRESS, address);
		return address;
	}

	private Identifier addIdentifier(final Identifier identifier) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_IDENTIFIER, identifier);
		return identifier;
	}

	public Identifier addIdentifier(final URI uri) {
		return this.addIdentifier(new Identifier(uri));
	}

	public TextValue addName(final String name) {
		return this.addName(new TextValue(name));
	}

	private TextValue addName(final TextValue name) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_NAME, name);
		return name;
	}

	private OnlineAccount addOnlineAccount(final OnlineAccount onlineAccount) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_ACCOUNT, onlineAccount);
		return onlineAccount;
	}

	public OnlineAccount addOnlineAccount(final String accountName,
			final ResourceReference serviceHomepage) {
		return this.addOnlineAccount(new OnlineAccount(accountName,
				serviceHomepage));
	}

	@Override
	protected void deleteAllReferences() {
		NodeWrapper.nodeWrapperOperations.deleteReferencedNodes(this
				.getAddresses());
		NodeWrapper.nodeWrapperOperations.deleteReferencedNodes(this
				.getOnlineAccounts());
		NodeWrapper.nodeWrapperOperations.deleteReferencedNodes(this
				.getIdentifiers());
		NodeWrapper.nodeWrapperOperations
				.deleteReferencedNodes(this.getNames());
	}

	public List<Address> getAddresses() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				Address.class, RelationshipTypes.HAS_ADDRESS);
	}

	public List<ResourceReference> getEmails() {
		return NodeWrapper.nodeWrapperOperations.getURIListProperties(this,
				AgentProperties.EMAILS);
	}

	@Override
	public org.gedcomx.agent.Agent getGedcomX() {
		final org.gedcomx.agent.Agent gedcomXAgent = new org.gedcomx.agent.Agent();

		gedcomXAgent.setEmails(this.getEmails());
		gedcomXAgent.setHomepage(this.getHomepage());
		gedcomXAgent.setId(this.getId());
		gedcomXAgent.setOpenid(this.getOpenid());
		gedcomXAgent.setPhones(this.getPhones());

		gedcomXAgent.setAddresses(NodeWrapper.nodeWrapperOperations
				.getGedcomXList(org.gedcomx.agent.Address.class,
						this.getAddresses()));
		gedcomXAgent.setAccounts(NodeWrapper.nodeWrapperOperations
				.getGedcomXList(org.gedcomx.agent.OnlineAccount.class,
						this.getOnlineAccounts()));
		gedcomXAgent.setNames(NodeWrapper.nodeWrapperOperations.getGedcomXList(
				org.gedcomx.common.TextValue.class, this.getNames()));
		gedcomXAgent.setIdentifiers(NodeWrapper.nodeWrapperOperations
				.getGedcomXList(org.gedcomx.conclusion.Identifier.class,
						this.getIdentifiers()));

		return gedcomXAgent;
	}

	public ResourceReference getHomepage() {
		final String homepage = (String) NodeWrapper.nodeWrapperOperations
				.getProperty(this, AgentProperties.HOMEPAGE);
		return new ResourceReference(new URI(homepage));
	}

	public String getId() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				GenericProperties.ID);
	}

	public List<Identifier> getIdentifiers() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				Identifier.class, RelationshipTypes.HAS_IDENTIFIER);
	}

	public List<TextValue> getNames() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				TextValue.class, RelationshipTypes.HAS_NAME);
	}

	public List<OnlineAccount> getOnlineAccounts() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				OnlineAccount.class, RelationshipTypes.HAS_ACCOUNT);
	}

	public ResourceReference getOpenid() {
		return new ResourceReference(new URI(
				(String) NodeWrapper.nodeWrapperOperations.getProperty(this,
						AgentProperties.OPENID)));
	}

	@Override
	public NodeWrapper getParentNode() {
		return null;
	}

	public List<ResourceReference> getPhones() {
		return NodeWrapper.nodeWrapperOperations.getURIListProperties(this,
				AgentProperties.PHONES);
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
		NodeWrapper.nodeWrapperOperations.setURIListProperties(this,
				AgentProperties.EMAILS, emails);
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
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				AgentProperties.HOMEPAGE, homepage);
	}

	public void setId(final String id) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.ID, id);
	}

	public void setOpenid(final ResourceReference openid) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				AgentProperties.HOMEPAGE, openid);
	}

	public void setPhones(final List<ResourceReference> phones) {
		NodeWrapper.nodeWrapperOperations.setURIListProperties(this,
				AgentProperties.PHONES, phones);
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
