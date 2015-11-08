package com.ZYKJ.tuannisuoai.data;

import java.io.Serializable;
import java.util.Date;

/**
 * 轮播的实现
 * 
 * @author bin
 * 
 */
public class Carousels implements Serializable {
	private String tbid;

	private String title;

	private String imgurl;

	private Date createtime;

	private String linkurl;

	public String getTbid() {
		return tbid;
	}

	public void setTbid(String tbid) {
		this.tbid = tbid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getLinkurl() {
		return linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
}