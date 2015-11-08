package com.ZYKJ.tuannisuoai.data;

import java.io.Serializable;

public class GoodsType implements Serializable {
	private Long tbid;

	private Long parentId;

	private String typeName;

	private Byte isLeaf;

	public Long getTbid() {
		return tbid;
	}

	public void setTbid(Long tbid) {
		this.tbid = tbid;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Byte getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Byte isLeaf) {
		this.isLeaf = isLeaf;
	}
}