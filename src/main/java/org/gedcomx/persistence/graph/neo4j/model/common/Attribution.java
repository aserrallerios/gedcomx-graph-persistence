package org.gedcomx.persistence.graph.neo4j.model.common;

import java.util.Date;

import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.model.GENgraphNode;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.utils.RelTypes;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;

public class Attribution extends GENgraphNode {

	protected Attribution() {
		super(NodeTypes.ATTRIBUTION);
	}

	protected Attribution(final Node node) throws WrongNodeType {
		super(NodeTypes.ATTRIBUTION, node);
	}

	public Attribution(final org.gedcomx.common.Attribution gedcomXAttribution) throws MissingFieldException {
		super(NodeTypes.ATTRIBUTION, gedcomXAttribution);
	}

	@Override
	protected void deleteAllReferences() {
		this.deleteReference(RelTypes.CONTRIBUTOR);
	}

	public String getChangeMessage() {
		return (String) this.getProperty(NodeProperties.Generic.CHANGE_MESSAGE);
	}

	@Override
	protected org.gedcomx.common.Attribution getGedcomX() {
		final org.gedcomx.common.Attribution gedcomXAttribution = new org.gedcomx.common.Attribution();

		gedcomXAttribution.setChangeMessage(this.getChangeMessage());
		gedcomXAttribution.setModified(this.getModified());
		// gedcomXAttribution.setContributor(this.getContributor());
		// TODO

		return gedcomXAttribution;
	}

	public Date getModified() {
		return new Date((Long) this.getProperty(NodeProperties.Generic.MODIFIED));
	}

	public GENgraphNode getParentNode() {
		// TODO
		return this.getNodeByRelationship(GENgraphNode.class, RelTypes.HAS_NAME, Direction.INCOMING);
	}

	@Override
	protected void resolveReferences() {
		// TODO
	}

	public void setChangeMessage(final String changeMessage) {
		this.setProperty(NodeProperties.Generic.CHANGE_MESSAGE, changeMessage);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.common.Attribution gedcomXAttribution = (org.gedcomx.common.Attribution) gedcomXObject;

		this.setModified(gedcomXAttribution.getModified());
		this.setChangeMessage(gedcomXAttribution.getChangeMessage());

	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) {

		// this.setContributor(gedcomXAttribution.getContributor());
		// TODO
	}

	public void setModified(final Date modified) {
		this.setProperty(NodeProperties.Generic.MODIFIED, modified.getTime());
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		return;
	}

	@Override
	protected void validateGedcomXObject(final Object gedcomXObject) throws MissingFieldException {
		return;
	}

	@Override
	protected void validateUnderlyingNode() {
		return;
	}

}
