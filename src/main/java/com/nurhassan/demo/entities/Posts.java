package com.nurhassan.demo.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Posts")
public class Posts {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "Posts_seq")
	private int id;
	private String title;
	private String excerpt;
	@Column(columnDefinition = "TEXT")
	private String content;
	private String author;
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date publishedAt;
	private boolean isPublished;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	
	@ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST})
	private List<Tags> tags = new ArrayList<>();
	
	@OneToMany(mappedBy = "posts",cascade = CascadeType.ALL)
	private List<Comments> comments = new ArrayList<>();

	User user;
	
	public Posts(int id, String title, String excerpt, String content, String author, Date publishedAt,
			boolean isPublished, Date createdAt, Date updatedAt, List<Tags> tags, List<Comments> comments) {
		super();
		this.id = id;
		this.title = title;
		this.excerpt = excerpt;
		this.content = content;
		this.author = author;
		this.publishedAt = publishedAt;
		this.isPublished = isPublished;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.tags = tags;
		this.comments = comments;
	}
	
	public Posts() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTagsInString(){
		String tagStr = "";
		for(Tags tag : tags)
		{
			tagStr+=tag.getName()+",";
		}
		return tagStr;	
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getExcerpt() {
		return excerpt;
	}
	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getPublishedAt() {
		return publishedAt;
	}
	public void setPublishedAt(Date publishedAt) {
		this.publishedAt = publishedAt;
	}
	public boolean isPublished() {
		return isPublished;
	}
	public void setPublished(boolean isPublished) {
		this.isPublished = isPublished;
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
	public List<Tags> getTags() {
		return tags;
	}
	public void setTags(List<Tags> tags) {
		this.tags = tags;
	}
	public List<Comments> getComments() {
		return comments;
	}
	public void setComments(List<Comments> comments) {
		this.comments = comments;
	}

	
}
