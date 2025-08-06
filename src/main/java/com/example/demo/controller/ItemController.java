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
import com.example.demo.repository.ItemRepository;

@RestController
@RequestMapping("/item")
public class ItemController {
	
	private ItemRepository itemRepository;
	
	public ItemController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}
	@GetMapping
	public List<Item> getAll(){
		return this.itemRepository.findAll();
	}
	@GetMapping("{itemid}")
	public ResponseEntity<Object> getById(@PathVariable("itemid") int itemid) {
		Optional<Item> item = this.itemRepository.findById(itemid);
		if(item.isEmpty()) {
			ErrorResponse errorResponse = generateErrorResponse(Constants.NOT_FOUND_ITEM);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(item);
	}
	@PostMapping
	public Item save(@RequestBody Item item) {
		return this.itemRepository.save(item);
	}
	@PutMapping("{itemid}")
	public ResponseEntity<Object> update(@PathVariable("itemid") int itemid, @RequestBody Item item) {
		Optional<Item> selectedItem = this.itemRepository.findById(itemid);
		if(selectedItem.isEmpty()) {
			ErrorResponse errorResponse = generateErrorResponse(Constants.NOT_FOUND_ITEM);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		item.setItemId(itemid);
		return ResponseEntity.status(HttpStatus.OK).body(itemRepository.save(item));
	}
	@DeleteMapping("{itemid}")
	public ResponseEntity<Object> delete(@PathVariable("itemid") int itemid) {
		//貸出中の備品は廃棄登録不可の処理
		Optional<Item> item = this.itemRepository.findById(itemid);
		if(item.map(Item::getStatus).orElse(0) != 1) {
			ErrorResponse errorResponse = generateErrorResponse(Constants.VALIDATED_DELETE);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		this.itemRepository.deleteById(itemid);
		return  ResponseEntity.status(HttpStatus.OK).build();
	}
	
	private ErrorResponse generateErrorResponse(String errorMessage) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage(errorMessage);
		return errorResponse;
	}
}