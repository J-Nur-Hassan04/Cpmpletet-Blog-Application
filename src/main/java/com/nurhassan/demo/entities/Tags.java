package com.nurhassan.demo.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Tags")
public class Tags {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "tag_seq")
	private int id;
	private Date createdAt;
	private Date updatedAt;
	private String name;
	
	@ManyToMany(mappedBy = "tags")
	List<Posts> post = new ArrayList<>();

	public Tags(int id, Date createdAt, Date updatedAt, String name, List<Posts> post) {
		super();
		this.id = id;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.name = name;
		this.post = post;
	}

	public Tags() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Posts> getPost() {
		return post;
	}

	public void setPost(List<Posts> post) {
		this.post = post;
	}
}
