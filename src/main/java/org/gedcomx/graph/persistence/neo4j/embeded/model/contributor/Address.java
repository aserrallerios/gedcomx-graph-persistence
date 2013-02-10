package org.gedcomx.graph.persistence.neo4j.embeded.model.contributor;

import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredPropertyException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;

public class Address extends GENgraphNode {

	protected Address(final org.gedcomx.contributor.Address gedcomXAddress) throws MissingRequiredPropertyException {
		super(NodeTypes.ADDRESS, gedcomXAddress);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingRequiredPropertyException {
		return;
	}

	public String getCity() {
		return (String) this.getProperty(NodeProperties.Agent.CITY);
	}

	public String getCountry() {
		return (String) this.getProperty(NodeProperties.Agent.COUNTRY);
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
	protected void setInitialProperties(final Object gedcomXObject) {
		final org.gedcomx.contributor.Address gedcomXAddress = (org.gedcomx.contributor.Address) gedcomXObject;
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
