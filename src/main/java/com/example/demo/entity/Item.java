package com.example.demo.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Item {
	@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private Integer itemid;
		
		private String name;
		
		private String category;
		
		private String item_info;
		
		private Integer status;
		
		private Integer delete_flag;
		
		private Timestamp created_at;
		
		private String created_by;
		
		private Timestamp updated_at;
		
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

		public Timestamp getCreated_at() {
			return created_at;
		}

		public void setCreated_at(Timestamp created_at) {
			this.created_at = created_at;
		}

		public String getCreated_by() {
			return created_by;
		}

		public void setCreated_by(String created_by) {
			this.created_by = created_by;
		}

		public Timestamp getUpdated_at() {
			return updated_at;
		}

		public void setUpdated_at(Timestamp updated_at) {
			this.updated_at = updated_at;
		}

		public String getUpdated_by() {
			return updated_by;
		}

		public void setUpdated_by(String updated_by) {
			this.updated_by = updated_by;
		}
		
}
