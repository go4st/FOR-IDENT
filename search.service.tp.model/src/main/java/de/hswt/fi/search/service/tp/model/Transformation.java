package de.hswt.fi.search.service.tp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
public class Transformation implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String rule;

	private String reaction;

	private String reactionId;

	private String reactionLink;

	private String compoundId;

	private String transformationProductId;

	@Transient
	private Compound compound;

	@Transient
	private Compound transformationProduct;

	public Transformation() {
	}

	/**
	 * Copy constructor without id and compounds
	 *
	 * @param transformation
	 *            instance to copy from
	 */
	public Transformation(Transformation transformation) {
		id = transformation.id;
		rule = transformation.rule;
		reaction = transformation.reaction;
		reactionId = transformation.reactionId;
		reactionLink = transformation.reactionLink;
		compoundId = transformation.compoundId;
		transformationProductId = transformation.transformationProductId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getReaction() {
		return reaction;
	}

	public void setReaction(String reaction) {
		this.reaction = reaction;
	}

	public String getReactionId() {
		return reactionId;
	}

	public void setReactionId(String reactionId) {
		this.reactionId = reactionId;
	}

	public String getReactionLink() {
		return reactionLink;
	}

	public void setReactionLink(String reactionLink) {
		this.reactionLink = reactionLink;
	}

	public String getCompoundId() {
		return compoundId;
	}

	public void setCompoundId(String compoundId) {
		this.compoundId = compoundId;
	}

	public String getTransformationProductId() {
		return transformationProductId;
	}

	public void setTransformationProductId(String transformationProductId) {
		this.transformationProductId = transformationProductId;
	}

	public Compound getCompound() {
		return compound;
	}

	public void setCompound(Compound compound) {
		this.compound = compound;
	}

	public Compound getTransformationProduct() {
		return transformationProduct;
	}

	public void setTransformationProduct(Compound transformationProduct) {
		this.transformationProduct = transformationProduct;
	}

}
