package org.gedcomx.persistence.graph.neo4j.model.contributor;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.model.GENgraph;
import org.gedcomx.persistence.graph.neo4j.model.GENgraphNode;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;

public class OnlineAccount extends GENgraphNode {

	protected OnlineAccount(final GENgraph graf, final org.gedcomx.agent.OnlineAccount gedcomXOnlineAccount) throws MissingFieldException {
		super(graf, NodeTypes.ACCOUNT, gedcomXOnlineAccount);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.agent.OnlineAccount gedcomXOnlineAccount = (org.gedcomx.agent.OnlineAccount) gedcomXObject;
		if ((gedcomXOnlineAccount.getAccountName() == null) || gedcomXOnlineAccount.getAccountName().isEmpty()) {
			throw new MissingRequiredPropertyException(OnlineAccount.class, NodeProperties.Agent.ACCOUNT_NAME);
		}
		if ((gedcomXOnlineAccount.getServiceHomepage() == null) || (gedcomXOnlineAccount.getServiceHomepage().getResource() == null)) {
			throw new MissingRequiredPropertyException(OnlineAccount.class, NodeProperties.Agent.SERVICE_HOMEPAGE);
		}
	}

	public String getAccountName() {
		return (String) this.getProperty(NodeProperties.Agent.ACCOUNT_NAME);
	}

	public ResourceReference getServiceHomepage() {
		final String serviceHomepage = (String) this.getProperty(NodeProperties.Agent.SERVICE_HOMEPAGE);
		return new ResourceReference(new URI(serviceHomepage));
	}

	public void setAccountName(final String accountName) {
		this.setProperty(NodeProperties.Agent.ACCOUNT_NAME, accountName);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.agent.OnlineAccount gedcomXOnlineAccount = (org.gedcomx.agent.OnlineAccount) gedcomXObject;
		this.setAccountName(gedcomXOnlineAccount.getAccountName());
		this.setServiceHomepage(gedcomXOnlineAccount.getServiceHomepage());

	}

	public void setServiceHomepage(final ResourceReference serviceHomepage) {
		this.setProperty(NodeProperties.Agent.SERVICE_HOMEPAGE, serviceHomepage.getResource().toString());
	}
}
