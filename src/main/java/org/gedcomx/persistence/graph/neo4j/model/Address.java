package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exceptions.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.AgentProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.neo4j.graphdb.Node;

@NodeType(NodeTypes.ACCOUNT)
public class Address extends NodeWrapper {

    protected Address() throws MissingFieldException {
        super();
    }

    protected Address(final Node node) throws UnknownNodeType,
            MissingFieldException {
        super(node);
    }

    protected Address(final org.gedcomx.agent.Address gedcomXAddress)
            throws MissingFieldException {
        super(gedcomXAddress);
    }

    @Override
    protected void deleteAllReferences() {
        return;
    }

    public Agent getAgent() {
        return (Agent) NodeWrapper.nodeWrapperOperations.getParentNode(this,
                RelationshipTypes.HAS_ADDRESS);
    }

    public String getCity() {
        return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
                AgentProperties.CITY);
    }

    public String getCountry() {
        return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
                AgentProperties.COUNTRY);
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
        return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
                AgentProperties.POSTAL_CODE);
    }

    public String getStateOrProvince() {
        return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
                AgentProperties.STATE_OR_PROVINCE);
    }

    public String getStreet() {
        return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
                AgentProperties.STREET);
    }

    public String getStreet2() {
        return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
                AgentProperties.STREET2);
    }

    public String getStreet3() {
        return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
                AgentProperties.STREET3);
    }

    public String getValue() {
        return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
                AgentProperties.VALUE);
    }

    @Override
    public void resolveReferences() {
        return;
    }

    public void setCity(final String city) {
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                AgentProperties.CITY, city);
    }

    public void setCountry(final String country) {
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                AgentProperties.COUNTRY, country);
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

    @Override
    protected void setGedcomXRelations(final Object gedcomXObject) {
        return;
    }

    public void setPostalCode(final String postalCode) {
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                AgentProperties.POSTAL_CODE, postalCode);
    }

    @Override
    protected void setRequiredProperties(final Object... properties) {
        return;
    }

    public void setStateOrProvince(final String stateOrProvince) {
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                AgentProperties.STATE_OR_PROVINCE, stateOrProvince);
    }

    public void setStreet(final String street) {
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                AgentProperties.STREET, street);
    }

    public void setStreet2(final String street2) {
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                AgentProperties.STREET2, street2);
    }

    public void setStreet3(final String street3) {
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                AgentProperties.STREET3, street3);
    }

    public void setValue(final String value) {
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                AgentProperties.VALUE, value);
    }

    @Override
    protected void validateUnderlyingNode() {
        return;
    }

}
