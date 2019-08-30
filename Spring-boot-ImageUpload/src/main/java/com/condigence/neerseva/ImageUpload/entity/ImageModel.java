package com.condigence.neerseva.ImageUpload.entity;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Table(name = "ImageTable")
@Entity
public class ImageModel {

	@Id
	@Column(name = "id")
	@GeneratedValue
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "type")
	private String type;

	@Lob
	@Column(name = "pic")
	private byte[] pic;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the pic
	 */
	public byte[] getPic() {
		return pic;
	}

	/**
	 * @param pic the pic to set
	 */
	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	@Override
	public String toString() {
		return "ImageModel [id=" + id + ", name=" + name + ", type=" + type + ", pic=" + Arrays.toString(pic) + "]";
	}

}
