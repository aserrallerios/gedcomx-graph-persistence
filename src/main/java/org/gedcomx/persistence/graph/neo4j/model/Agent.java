package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.neo4j.graphdb.Node;

@NodeType("AGENT")
public class Agent extends NodeWrapper {

	public enum AgentProperties implements NodeProperties {
		HOMEPAGE, OPENID, EMAILS, PHONES, STREET, STREET2, STREET3, VALUE, STATE_OR_PROVINCE, CITY, COUNTRY, POSTAL_CODE, ACCOUNT_NAME, SERVICE_HOMEPAGE, IDENTIFIER_TYPE;

		private final boolean indexed;
		private final IndexNodeNames indexName;

		private AgentProperties() {
			this.indexed = false;
			this.indexName = null;
		}

		private AgentProperties(final boolean indexed, final IndexNodeNames indexName) {
			this.indexed = indexed;
			this.indexName = indexName;
		}

		@Override
		public IndexNodeNames getIndexName() {
			return this.indexName;
		}

		@Override
		public boolean isIndexed() {
			return this.indexed;
		}

	}

	public Agent() throws MissingFieldException {
		super();
	}

	protected Agent(final Node node) throws UnknownNodeType, MissingFieldException {
		super(node);
	}

	public Agent(final org.gedcomx.agent.Agent gedcomXAgent) throws MissingFieldException {
		super(gedcomXAgent);
	}

	public void addAddress(final Address address) {
		this.addRelationship(WrapperRelTypes.HAS_ADDRESS, address);
	}

	public void addIdentifier(final Identifier identifier) {
		this.addRelationship(WrapperRelTypes.HAS_IDENTIFIER, identifier);
	}

	public void addName(final TextValue name) {
		this.addRelationship(WrapperRelTypes.HAS_NAME, name);
	}

	public void addOnlineAccount(final OnlineAccount onlineAccount) {
		this.addRelationship(WrapperRelTypes.HAS_ACCOUNT, onlineAccount);
	}

	@Override
	public void deleteAllReferences() {
		this.deleteReferencedNodes(this.getAddresses());
		this.deleteReferencedNodes(this.getOnlineAccounts());
		this.deleteReferencedNodes(this.getIdentifiers());
		this.deleteReferencedNodes(this.getNames());
	}

	public List<Address> getAddresses() {
		return this.getNodesByRelationship(Address.class, WrapperRelTypes.HAS_ADDRESS);
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

		gedcomXAgent.setAddresses(this.getGedcomXList(org.gedcomx.agent.Address.class, this.getAddresses()));
		gedcomXAgent.setAccounts(this.getGedcomXList(org.gedcomx.agent.OnlineAccount.class, this.getOnlineAccounts()));
		gedcomXAgent.setNames(this.getGedcomXList(org.gedcomx.common.TextValue.class, this.getNames()));
		gedcomXAgent.setIdentifiers(this.getGedcomXList(org.gedcomx.conclusion.Identifier.class, this.getIdentifiers()));

		return gedcomXAgent;
	}

	public ResourceReference getHomepage() {
		final String homepage = (String) this.getProperty(AgentProperties.HOMEPAGE);
		return new ResourceReference(new URI(homepage));
	}

	public String getId() {
		return (String) this.getProperty(GenericProperties.ID);
	}

	public List<Identifier> getIdentifiers() {
		return this.getNodesByRelationship(Identifier.class, WrapperRelTypes.HAS_IDENTIFIER);
	}

	public List<TextValue> getNames() {
		return this.getNodesByRelationship(TextValue.class, WrapperRelTypes.HAS_NAME);
	}

	public List<OnlineAccount> getOnlineAccounts() {
		return this.getNodesByRelationship(OnlineAccount.class, WrapperRelTypes.HAS_ACCOUNT);
	}

	public ResourceReference getOpenid() {
		final String openid = (String) this.getProperty(AgentProperties.OPENID);
		return new ResourceReference(new URI(openid));
	}

	public List<ResourceReference> getPhones() {
		return this.getURIListProperties(AgentProperties.PHONES);
	}

	@Override
	protected void resolveReferences() {
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
	protected void setGedcomXRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.agent.Agent gedcomXAgent = (org.gedcomx.agent.Agent) gedcomXObject;

		for (final org.gedcomx.agent.Address gedcomXAddress : gedcomXAgent.getAddresses()) {
			this.addAddress(new Address(gedcomXAddress));
		}
		for (final org.gedcomx.agent.OnlineAccount gedcomXOnlineAccount : gedcomXAgent.getAccounts()) {
			this.addOnlineAccount(new OnlineAccount(gedcomXOnlineAccount));
		}
		for (final org.gedcomx.conclusion.Identifier gedcomXIdentifier : gedcomXAgent.getIdentifiers()) {
			this.addIdentifier(new Identifier(gedcomXIdentifier));
		}
		for (final org.gedcomx.common.TextValue gedcomXName : gedcomXAgent.getNames()) {
			this.addName(new TextValue(gedcomXName));
		}
	}

	public void setHomepage(final ResourceReference homepage) {
		this.setProperty(AgentProperties.HOMEPAGE, homepage.getResource().toString());
	}

	public void setId(final String id) {
		this.setProperty(GenericProperties.ID, id);
	}

	public void setOpenid(final ResourceReference openid) {
		this.setProperty(AgentProperties.HOMEPAGE, openid.getResource().toString());
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
