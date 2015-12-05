package com.ZYKJ.tuannisuoai.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Bitmap;

public class Comment implements Serializable{
	/**
	 * 商品评价
	 */
	private static final long serialVersionUID = 1L;
	private String goodid;
	private float stars;
	private String content;
	private ArrayList<Bitmap> images;
	private ArrayList<File> files;
	private String imageName;
	public String getGoodid() {
		return goodid;
	}
	public void setGoodid(String goodid) {
		this.goodid = goodid;
	}
	public float getStars() {
		return stars;
	}
	public void setStars(float stars) {
		this.stars = stars;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public ArrayList<Bitmap> getImages() {
		return images;
	}
	public void setImages(ArrayList<Bitmap> images) {
		this.images = images;
	}
	public ArrayList<File> getFiles() {
		return files;
	}
	public void setFiles(ArrayList<File> files) {
		this.files = files;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
}
