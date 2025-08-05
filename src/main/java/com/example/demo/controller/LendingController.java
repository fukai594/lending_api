package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Constants.Constants;
import com.example.demo.entity.ErrorResponse;
import com.example.demo.entity.Item;
import com.example.demo.entity.Lending;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.LendingRepository;

@RestController
@RequestMapping("/lending")
public class LendingController {
	private LendingRepository lendingRepository;
	private ItemRepository itemRepository;
	
	public LendingController(LendingRepository LendingRepository, ItemRepository ItemRepository) {
		this.lendingRepository = LendingRepository;
		this.itemRepository = ItemRepository;
	}
	@GetMapping
	public List<Lending> getAll(){
		return this.lendingRepository.findAll();
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Object> getById(@PathVariable("id") int id) {
		Optional<Lending> lending = this.lendingRepository.findById(id);//指定したIdが存在するか確認
		if(lending.isEmpty()) {
			ErrorResponse errorResponse = generateErrorResponse(Constants.NOT_FOUND_ITEM_ID);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(lending);
	}
	@PostMapping
	public ResponseEntity<Object> save(@RequestBody Lending lending) {
		Optional<Item> item = this.itemRepository.findById(lending.getItemid());
		if(item.isEmpty()) {
			ErrorResponse errorResponse = generateErrorResponse(Constants.NOT_FOUND_ITEM_ID);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		Integer status = item.map(Item::getStatus).orElse(0);
		if(status == 1) {//itemのステータスが貸出中の場合はエラーを発生させる
			ErrorResponse errorResponse = generateErrorResponse(Constants.LENDING_ITEM);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		updateItemStatus(item, 1);//itemのステータスを1:貸出中に更新
		this.lendingRepository.save(lending);
		return ResponseEntity.status(HttpStatus.OK).body(lending);
	}
	@PutMapping("{id}")
	public ResponseEntity<Object> update(@PathVariable("id") int id, @RequestBody Lending lending){
		Optional<Lending> selectedLending = this.lendingRepository.findById(id);//存在しないidであればエラーを返す
		if(selectedLending.isEmpty()) {
			ErrorResponse errorResponse = generateErrorResponse(Constants.NOT_FOUND_ITEM_ID);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		Integer itemid = selectedLending.map(Lending::getItemid).orElse(0);//アイテムIDを更新する際に存在確認のために必要
		Optional<Item> item = this.itemRepository.findById(lending.getItemid());//存在しないアイテムIDの場合はエラーを返す
		if(item.isEmpty()) {
			ErrorResponse errorResponse = generateErrorResponse(Constants.NOT_FOUND_ITEM_ID);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}	
		Integer status = selectedLending.map(Lending::getStatus).orElse(0);//ステータスが1:返却済みであれば備品テーブルのステータスを0:貸出可に更新
		if(status == 1) {
			updateItemStatus(item, 0);//itemのステータスを0:貸出可に更新
		}
		this.lendingRepository.save(lending);
		return ResponseEntity.status(HttpStatus.OK).body(lending);
	}
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") int id) {
		this.lendingRepository.deleteById(id);
	}
	private ErrorResponse generateErrorResponse(String errorMessage) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage(errorMessage);
		return errorResponse;
	}
	private void updateItemStatus(Optional<Item> item, Integer status) {
		item.ifPresent(i -> {
			i.setStatus(status);//itemのステータス更新
			this.itemRepository.save(i);
		});
	}
}
