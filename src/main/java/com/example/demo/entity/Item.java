package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Entity
public class Item {
	@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private Integer itemid;
	
	    @Size(min = 1, max = 100, message="入力エラー：100文字以内")
		private String name;
	    
	    @Size(min = 1, max = 100, message="入力エラー：100文字以内")
		private String category;
		
	    @Size(max = 100, message="入力エラー: 0文字～100文字")
		private String item_info;
		
		@Min(0)
		@Max(1)
		@Column(name = "status", nullable=false)
		private Integer status = 0;
		
		@Min(0)
		@Max(1)
		@Column(name = "delete_flag", nullable=false)
		private Integer delete_flag = 0;
		
		@Column(name="created_at", insertable=false, updatable=false)
		private LocalDate created_at;
		
		@Size(min = 1, max = 50, message="入力エラー：50文字以内")
		private String created_by;
		
		@Column(name="updated_at", insertable=false, updatable=false)
		private LocalDate updated_at;
		
		@Size(min = 1, max = 50, message="入力エラー：50文字以内")
		private String updated_by;
		
		public Integer getItemId() {
			return itemid;
		}

		public void setItemId(Integer itemId) {
			this.itemid = itemId;
		}
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getItem_info() {
			return item_info;
		}

		public void setItem_info(String item_info) {
			this.item_info = item_info;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public Integer getDelete_flag() {
			return delete_flag;
		}

		public void setDelete_flag(Integer delete_flag) {
			this.delete_flag = delete_flag;
		}

		public LocalDate getCreated_at() {
			return created_at;
		}

		public void setCreated_at(LocalDate created_at) {
			this.created_at = created_at;
		}

		public String getCreated_by() {
			return created_by;
		}

		public void setCreated_by(String created_by) {
			this.created_by = created_by;
		}

		public LocalDate getUpdated_at() {
			return updated_at;
		}

		public void setUpdated_at(LocalDate updated_at) {
			this.updated_at = updated_at;
		}

		public String getUpdated_by() {
			return updated_by;
		}

		public void setUpdated_by(String updated_by) {
			this.updated_by = updated_by;
		}
		
}
