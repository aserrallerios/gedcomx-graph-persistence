package org.gedcomx.persistence.graph.neo4j.model.contributor;

import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.model.GENgraphNode;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.utils.RelTypes;
import org.neo4j.graphdb.Direction;

public class Address extends GENgraphNode {

	public Address() {
		super(NodeTypes.ADDRESS);
	}

	protected Address(final org.gedcomx.agent.Address gedcomXAddress) throws MissingFieldException {
		super(NodeTypes.ADDRESS, gedcomXAddress);
	}

	public Agent getAgent() {
		return this.getNodeByRelationship(Agent.class, RelTypes.HAS_ADDRESS, Direction.INCOMING);
	}

	public String getCity() {
		return (String) this.getProperty(NodeProperties.Agent.CITY);
	}

	public String getCountry() {
		return (String) this.getProperty(NodeProperties.Agent.COUNTRY);
	}

	@Override
	public org.gedcomx.agent.Address getGedcomX() {
		final org.gedcomx.agent.Address gedcomXAddress = new org.gedcomx.agent.Address();

		gedcomXAddress.setCity(this.getCity());
		gedcomXAddress.setCountry(this.getCountry());
		gedcomXAddress.setPostalCode(this.getPostalCode());
		gedcomXAddress.setStateOrProvince(this.getStateOrProvince());
		gedcomXAddress.setStreet(this.getStreet());
		gedcomXAddress.setStreet2(this.getStreet2());
		gedcomXAddress.setStreet3(this.getStreet3());
		gedcomXAddress.setValue(this.getValue());

		return gedcomXAddress;
	}

	public String getPostalCode() {
		return (String) this.getProperty(NodeProperties.Agent.POSTAL_CODE);
	}

	public String getStateOrProvince() {
		return (String) this.getProperty(NodeProperties.Agent.STATE_OR_PROVINCE);
	}

	public String getStreet() {
		return (String) this.getProperty(NodeProperties.Agent.STREET);
	}

	public String getStreet2() {
		return (String) this.getProperty(NodeProperties.Agent.STREET2);
	}

	public String getStreet3() {
		return (String) this.getProperty(NodeProperties.Agent.STREET3);
	}

	public String getValue() {
		return (String) this.getProperty(NodeProperties.Agent.VALUE);
	}

	public void setCity(final String city) {
		this.setProperty(NodeProperties.Agent.CITY, city);
	}

	public void setCountry(final String country) {
		this.setProperty(NodeProperties.Agent.COUNTRY, country);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.agent.Address gedcomXAddress = (org.gedcomx.agent.Address) gedcomXObject;

		this.setValue(gedcomXAddress.getValue());
		this.setCity(gedcomXAddress.getCity());
		this.setCountry(gedcomXAddress.getCountry());
		this.setPostalCode(gedcomXAddress.getPostalCode());
		this.setStateOrProvince(gedcomXAddress.getStateOrProvince());
		this.setStreet(gedcomXAddress.getStreet());
		this.setStreet2(gedcomXAddress.getStreet2());
		this.setStreet3(gedcomXAddress.getStreet3());
	}

	public void setPostalCode(final String postalCode) {
		this.setProperty(NodeProperties.Agent.POSTAL_CODE, postalCode);
	}

	public void setStateOrProvince(final String stateOrProvince) {
		this.setProperty(NodeProperties.Agent.STATE_OR_PROVINCE, stateOrProvince);
	}

	public void setStreet(final String street) {
		this.setProperty(NodeProperties.Agent.STREET, street);
	}

	public void setStreet2(final String street2) {
		this.setProperty(NodeProperties.Agent.STREET2, street2);
	}

	public void setStreet3(final String street3) {
		this.setProperty(NodeProperties.Agent.STREET3, street3);
	}

	public void setValue(final String value) {
		this.setProperty(NodeProperties.Agent.VALUE, value);
	}

}
