package org.gedcomx.persistence.graph.neo4j.model;

import java.util.Date;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.neo4j.graphdb.Node;

@NodeType(NodeTypes.ATTRIBUTION)
public class Attribution extends NodeWrapper {

	protected Attribution() {
		super();
	}

	protected Attribution(final Node node) {
		super(node);
	}

	protected Attribution(
			final org.gedcomx.common.Attribution gedcomXAttribution) {
		super(gedcomXAttribution);
	}

	@Override
	protected void deleteAllReferences() {
		NodeWrapper.nodeWrapperOperations.deleteReference(this,
				RelationshipTypes.CONTRIBUTOR);
	}

	public String getChangeMessage() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				GenericProperties.CHANGE_MESSAGE);
	}

	public Agent getContributor() {
		return NodeWrapper.nodeWrapperOperations.getNodeByRelationship(this,
				Agent.class, RelationshipTypes.CONTRIBUTOR);
	}

	@Override
	public org.gedcomx.common.Attribution getGedcomX() {
		final org.gedcomx.common.Attribution gedcomXAttribution = new org.gedcomx.common.Attribution();

		gedcomXAttribution.setChangeMessage(this.getChangeMessage());
		gedcomXAttribution.setModified(this.getModified());

		final Agent agent = this.getContributor();
		if (agent != null) {
			gedcomXAttribution.setContributor(agent.getResourceReference());
		}

		return gedcomXAttribution;
	}

	public Date getModified() {
		return new Date((Long) NodeWrapper.nodeWrapperOperations.getProperty(
				this, GenericProperties.MODIFIED));
	}

	public NodeWrapper getParentNode() {
		return NodeWrapper.nodeWrapperOperations.getParentNode(this,
				RelationshipTypes.ATTRIBUTION);
	}

	@Override
	public void resolveReferences() {
		NodeWrapper.nodeWrapperOperations.createReferenceRelationship(this,
				RelationshipTypes.CONTRIBUTOR,
				GenericProperties.CONTRIBUTOR_REFERENCE);
	}

	public void setChangeMessage(final String changeMessage) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.CHANGE_MESSAGE, changeMessage);
	}

	public void setContributor(final Agent agent) {
		NodeWrapper.nodeWrapperOperations.createReferenceRelationship(this,
				RelationshipTypes.CONTRIBUTOR, agent);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.common.Attribution gedcomXAttribution = (org.gedcomx.common.Attribution) gedcomXObject;

		this.setModified(gedcomXAttribution.getModified());
		this.setChangeMessage(gedcomXAttribution.getChangeMessage());
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) {
		final org.gedcomx.common.Attribution gedcomXAttribution = (org.gedcomx.common.Attribution) gedcomXObject;

		if (gedcomXAttribution.getContributor() != null) {
			NodeWrapper.nodeWrapperOperations.setProperty(this,
					GenericProperties.CONTRIBUTOR_REFERENCE,
					gedcomXAttribution.getContributor());
		}
	}

	public void setModified(final Date modified) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.MODIFIED, modified.getTime());
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
