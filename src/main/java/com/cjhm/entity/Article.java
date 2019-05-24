package com.cjhm.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="T_ARTICLE")
public class Article {

	@Id
	@GeneratedValue
	private Long articleId;
	
	@Column
	private String title;

	@Column
	private String content;
	
	@Column
	private String username;
	
	@Column
	@CreatedDate
	private LocalDateTime createDate;
}
