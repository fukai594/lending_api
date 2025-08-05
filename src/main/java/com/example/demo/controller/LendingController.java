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
		Optional<Lending> lending = this.lendingRepository.findById(id);
		if(lending.isEmpty()) {
			ErrorResponse errorResponse = generateErrorResponse();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(lending);
	}
	//備品テーブルのステータスを1:貸出中に更新
	@PostMapping
	public ResponseEntity<Object> save(@RequestBody Lending lending) {
		Optional<Item> item = this.itemRepository.findById(lending.getItemid());//指定したアイテムIDが備品テーブルに存在しているかチェック
		if(item.isEmpty()) {
			ErrorResponse errorResponse = generateErrorResponse();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		this.lendingRepository.save(lending);
		return ResponseEntity.status(HttpStatus.OK).body(lending);
	}
	//返却された場合返却済みにステータスを更新。
	//ステータス以外の貸出管理情報を更新する場合の対応。
	//リクエストボディのステータスが1:返却済みであれば備品テーブルのステータスを0:貸出可に更新
	@PutMapping("{id}")
	public ResponseEntity<Object> update(@PathVariable("id") int id, @RequestBody Lending lending){
		Optional<Lending> selectedLending = this.lendingRepository.findById(id);
		if(selectedLending.isEmpty()) {
			ErrorResponse errorResponse = generateErrorResponse();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		this.lendingRepository.save(lending);
		return ResponseEntity.status(HttpStatus.OK).body(lending);
	}
	//外部キー制約があって削除できない場合の対応
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") int id) {
		this.lendingRepository.deleteById(id);
	}
	private ErrorResponse generateErrorResponse() {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage("指定したアイテムIDが存在しません");
		return errorResponse;
	}
}
