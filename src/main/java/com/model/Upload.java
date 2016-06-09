package com.model;

// default package 
// Generated Mar 12, 2016 3:09:14 PM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * RoomType generated by hbm2java
 */
@Entity
@Table(name = "UPLOAD", schema = "sl_learn_zk_upload_1")
public class Upload implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int uploadID;
	private String fileName;
	private int fileSize;
	private int partSize;
	private int partAmount;
	private int groupSize;
	private int groupAmount;
	private int lastGroup;
	private int uploadStatus;

	/**
	 * 
	 */
	public Upload() {
		// TODO Auto-generated constructor stub
	}

	public Upload(int uploadID, String fileName, int fileSize, int partSize, int partAmount, int groupSize,
			int groupAmount, int lastGroup, int uploadStatus) {
		super();
		this.uploadID = uploadID;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.partSize = partSize;
		this.partAmount = partAmount;
		this.groupSize = groupSize;
		this.groupAmount = groupAmount;
		this.lastGroup = lastGroup;
		this.uploadStatus = uploadStatus;
	}

	@Id
	@SequenceGenerator(name = "SEQ_UPLOAD", sequenceName = "SEQ_UPLOAD")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_UPLOAD")
	@Column(name = "upload_id", unique = true, nullable = false)
	public int getUploadID() {
		return uploadID;
	}

	public void setUploadID(int uploadID) {
		this.uploadID = uploadID;
	}

	@Column(name = "file_name", length = 100)
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "file_size")
	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	@Column(name = "part_size")
	public int getPartSize() {
		return partSize;
	}

	public void setPartSize(int partSize) {
		this.partSize = partSize;
	}

	@Column(name = "part_amount")
	public int getPartAmount() {
		return partAmount;
	}

	public void setPartAmount(int partAmount) {
		this.partAmount = partAmount;
	}

	@Column(name = "group_size")
	public int getGroupSize() {
		return groupSize;
	}

	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}

	@Column(name = "group_amount")
	public int getGroupAmount() {
		return groupAmount;
	}

	public void setGroupAmount(int groupAmount) {
		this.groupAmount = groupAmount;
	}

	@Column(name = "last_group")
	public int getLastGroup() {
		return lastGroup;
	}

	public void setLastGroup(int lastGroup) {
		this.lastGroup = lastGroup;
	}

	@Column(name = "upload_status")
	public int getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(int uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

}
