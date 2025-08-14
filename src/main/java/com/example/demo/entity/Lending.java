package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Lending {
	@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private Integer id;
		
		@NotNull
		private Integer itemid;
		
		@NotNull
		@Min(value = 0, message="入力エラー:0以上")
		private Integer employee_id;
		
		@Column(name="rental_datetime", insertable=false, updatable=false)
		private LocalDate rental_datetime;
		
		private String return_deadline;
		
		@Min(0)
		@Max(1)
		@Column(name = "allow_extension", nullable=false)
		private Integer allow_extension = 0;
		
		@Min(0)
		@Max(1)
		@Column(name = "status")
		private Integer status = 0;
		
		@Column(name="created_at", insertable=false, updatable=false)
		private LocalDate created_at;
		
		@Size(min = 1, max = 50, message="入力エラー：50文字以内")
		private String created_by;
		
		@Column(name="updated_at", insertable=false, updatable=false)
		private LocalDate updated_at;
		
		@Size(min = 1, max = 50, message="入力エラー：50文字以内")
		private String updated_by;

		public Integer getId() {
			return id;
		}
		
		public void setId(Integer id) {
			this.id = id;
		}
		
		public Integer getItemid() {
			return itemid;
		}

		public void setItemid(Integer itemid) {
			this.itemid = itemid;
		}

		public Integer getEmployee_id() {
			return employee_id;
		}

		public void setEmployee_id(Integer employee_id) {
			this.employee_id = employee_id;
		}

		public LocalDate getRental_datetime() {
			return rental_datetime;
		}

		public void setRental_datetime(LocalDate rental_datetime) {
			this.rental_datetime = rental_datetime;
		}

		public String getReturn_deadline() {
			return return_deadline;
		}

		public void setReturn_deadline(String return_deadline) {
			this.return_deadline = return_deadline;
		}

		public Integer getAllow_extension() {
			return allow_extension;
		}

		public void setAllow_extension(Integer allow_extension) {
			this.allow_extension = allow_extension;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
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
