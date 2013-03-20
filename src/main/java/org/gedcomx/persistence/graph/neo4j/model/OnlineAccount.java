package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.neo4j.graphdb.Node;

@NodeType("ACCOUNT")
public class OnlineAccount extends NodeWrapper {

	protected OnlineAccount(final Node node) throws UnknownNodeType, MissingFieldException {
		super(node);
	}

	protected OnlineAccount(final org.gedcomx.agent.OnlineAccount gedcomXOnlineAccount) throws MissingFieldException {
		super(gedcomXOnlineAccount);
	}

	protected OnlineAccount(final String accountName, final ResourceReference serviceHomepage) throws MissingFieldException {
		super(accountName, serviceHomepage);
	}

	@Override
	protected void deleteAllReferences() {
		return;
	}

	public String getAccountName() {
		return (String) this.getProperty(AgentProperties.ACCOUNT_NAME);
	}

	public Agent getAgent() {
		return (Agent) this.getParentNode(GENgraphRelTypes.HAS_ACCOUNT);
	}

	@Override
	protected org.gedcomx.agent.OnlineAccount getGedcomX() {
		final org.gedcomx.agent.OnlineAccount gedcomXOnlineAccount = new org.gedcomx.agent.OnlineAccount();

		gedcomXOnlineAccount.setServiceHomepage(this.getServiceHomepage());
		gedcomXOnlineAccount.setAccountName(this.getAccountName());

		return gedcomXOnlineAccount;
	}

	public ResourceReference getServiceHomepage() {
		final String serviceHomepage = (String) this.getProperty(AgentProperties.SERVICE_HOMEPAGE);
		return new ResourceReference(new URI(serviceHomepage));
	}

	@Override
	protected void resolveReferences() {
		return;
	}

	public void setAccountName(final String accountName) {
		this.setProperty(AgentProperties.ACCOUNT_NAME, accountName);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.agent.OnlineAccount gedcomXOnlineAccount = (org.gedcomx.agent.OnlineAccount) gedcomXObject;
		this.setAccountName(gedcomXOnlineAccount.getAccountName());
		this.setServiceHomepage(gedcomXOnlineAccount.getServiceHomepage());

	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) {
		return;
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.setAccountName((String) properties[0]);
		this.setServiceHomepage((ResourceReference) properties[1]);
	}

	public void setServiceHomepage(final ResourceReference serviceHomepage) {
		this.setProperty(AgentProperties.SERVICE_HOMEPAGE, serviceHomepage.getResource().toString());
	}

	@Override
	protected void validateUnderlyingNode() throws MissingRequiredPropertyException {
		if (ValidationTools.nullOrEmpty(this.getAccountName())) {
			throw new MissingRequiredPropertyException(OnlineAccount.class, AgentProperties.ACCOUNT_NAME);
		}
		if (ValidationTools.nullOrEmpty(this.getServiceHomepage())) {
			throw new MissingRequiredPropertyException(OnlineAccount.class, AgentProperties.SERVICE_HOMEPAGE);
		}
	}
}