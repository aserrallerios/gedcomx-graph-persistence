package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.Agent.AgentProperties;
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
		return (Agent) this.getParentNode(RelTypes.HAS_ACCOUNT);
	}

	@Override
	protected org.gedcomx.agent.OnlineAccount getGedcomX() {
		final org.gedcomx.agent.OnlineAccount gedcomXOnlineAccount = new org.gedcomx.agent.OnlineAccount();

		gedcomXOnlineAccount.setServiceHomepage(this.getServiceHomepage());
		gedcomXOnlineAccount.setAccountName(this.getAccountName());

		return gedcomXOnlineAccount;
	}

	public ResourceReference getServiceHomepage() {
		return new ResourceReference(new URI((String) this.getProperty(AgentProperties.SERVICE_HOMEPAGE)));
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
		this.setProperty(AgentProperties.SERVICE_HOMEPAGE, serviceHomepage);
	}

	@Override
	protected void validateUnderlyingNode() throws MissingRequiredPropertyException {
		if (ValidationTools.nullOrEmpty(this.getAccountName())) {
			throw new MissingRequiredPropertyException(this.getAnnotatedNodeType(), AgentProperties.ACCOUNT_NAME.toString());
		}
		if (ValidationTools.nullOrEmpty(this.getServiceHomepage())) {
			throw new MissingRequiredPropertyException(this.getAnnotatedNodeType(), AgentProperties.SERVICE_HOMEPAGE.toString());
		}
	}
}
