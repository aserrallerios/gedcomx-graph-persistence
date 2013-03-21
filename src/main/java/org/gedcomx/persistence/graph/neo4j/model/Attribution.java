package org.gedcomx.persistence.graph.neo4j.model;

import java.util.Date;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.neo4j.graphdb.Node;

@NodeType("ATTRIBUTION")
public class Attribution extends NodeWrapper {

	public Attribution() throws MissingFieldException {
		super();
	}

	protected Attribution(final Node node) throws UnknownNodeType, MissingFieldException {
		super(node);
	}

	public Attribution(final org.gedcomx.common.Attribution gedcomXAttribution) throws MissingFieldException {
		super(gedcomXAttribution);
	}

	@Override
	protected void deleteAllReferences() {
		this.deleteReference(WrapperRelTypes.CONTRIBUTOR);
	}

	public String getChangeMessage() {
		return (String) this.getProperty(GenericProperties.CHANGE_MESSAGE);
	}

	@Override
	public org.gedcomx.common.Attribution getGedcomX() {
		final org.gedcomx.common.Attribution gedcomXAttribution = new org.gedcomx.common.Attribution();

		gedcomXAttribution.setChangeMessage(this.getChangeMessage());
		gedcomXAttribution.setModified(this.getModified());
		// gedcomXAttribution.setContributor(this.getContributor());
		// TODO

		return gedcomXAttribution;
	}

	public Date getModified() {
		return new Date((Long) this.getProperty(GenericProperties.MODIFIED));
	}

	public NodeWrapper getParentNode() {
		return super.getParentNode(WrapperRelTypes.ATTRIBUTION);
	}

	@Override
	protected void resolveReferences() {
		// TODO
	}

	public void setChangeMessage(final String changeMessage) {
		this.setProperty(GenericProperties.CHANGE_MESSAGE, changeMessage);
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
		this.setProperty(GenericProperties.MODIFIED, modified.getTime());
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
