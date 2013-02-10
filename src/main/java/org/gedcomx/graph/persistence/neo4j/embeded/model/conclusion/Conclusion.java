package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredPropertyException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;

public class Conclusion extends GENgraphNode {

	private ConclusionSubnode subnode;

	public Conclusion(final org.gedcomx.conclusion.Conclusion gedcomXConclusion) throws MissingRequiredPropertyException {
		super(NodeTypes.CONCLUSION, gedcomXConclusion);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingRequiredPropertyException {
		// TODO Auto-generated method stub

	}

	public ConclusionSubnode getSubnode() {
		return this.subnode;
	}

	@Override
	protected void setInitialProperties(final Object gedcomXObject) {
		// TODO Auto-generated method stub

	}

	public void setSubnode(final ConclusionSubnode subnode) {
		this.subnode = subnode;
	}

}
