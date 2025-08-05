package com.example.demo.entity;

import java.sql.Timestamp;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Lending {
	@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private Integer id;
	
		private Integer itemid;
		
		private Integer employee_id;
		
		private Timestamp rental_datetime;
		
		private String return_deadline;
		
		private Integer allow_extension;
		
		private Integer status;
		
		private Timestamp created_at;
		
		private String created_by;
		
		private Timestamp updated_at;
		
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

		public Timestamp getRental_datetime() {
			return rental_datetime;
		}

		public void setRental_datetime(Timestamp rental_datetime) {
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
