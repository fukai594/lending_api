package com.example.demo.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
			ErrorResponse errorResponse = generateErrorResponse(Constants.NOT_FOUND_LENDING);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(lending);
	}
	private boolean isTomorrowOrLater(LocalDate date) {//明日以降の日にちであるかの確認
		LocalDate today = LocalDate.now();
		if(date.isAfter(today)) {
			return true;
		}
		return false;
	}
	@PostMapping
	public ResponseEntity<Object> save(
			@RequestBody @Validated Lending lending,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
            // エラーメッセージを収集
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }
		//ステータスがnullの時は0をセットする
		if(lending.getStatus() == null) {
			lending.setStatus(0);
		}
		//ステータスのバリデーション
		if(lending.getStatus() != 0 && lending.getStatus() != null) {//statusのバリデーション
			ErrorResponse errorResponse = generateErrorResponse(Constants.VALIDATED_STATUS_POST);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		//返却期限のバリデーション
		String deadline = lending.getReturn_deadline();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		try {//受け取った文字列をlocalDate型に変換
			LocalDate date = LocalDate.parse(deadline, formatter);			
			if(!isTomorrowOrLater(date)) {
				ErrorResponse errorResponse = generateErrorResponse(Constants.VALIDATED_RETURN_DEADLINE);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
			}
		}catch(Exception e) {
			ErrorResponse errorResponse = generateErrorResponse(Constants.VALIDATED_INPUT);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		//指定したアイテムIDが貸出可能かの確認
		Optional<Item> item = this.itemRepository.findById(lending.getItemid());//存在チェック
		if(item.isEmpty()) {
			ErrorResponse errorResponse = generateErrorResponse(Constants.NOT_FOUND_ITEM);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		if(item.map(Item::getStatus).orElse(0) == 1) {//itemのステータスが貸出中の場合はエラー
			ErrorResponse errorResponse = generateErrorResponse(Constants.LENDING_ITEM);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		//itemの廃棄フラグが1:廃棄の場合エラー
		Integer deleteFlag = item.map(Item::getDelete_flag).orElse(0);
		if(deleteFlag == 1) {
			ErrorResponse errorResponse = generateErrorResponse(Constants.DELETED_ITEM);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		try {
			this.lendingRepository.save(lending);
			updateItemStatus(item, 1);//itemのステータスを1:貸出中に更新
		}catch(Exception e) {
			ErrorResponse errorResponse = generateErrorResponse(Constants.VALIDATED_INPUT);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(lending);
	}
	@PutMapping("{id}")
	public ResponseEntity<Object> update(@PathVariable("id") int id,
			@RequestBody @Validated Lending lending,
			BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
            // エラーメッセージを収集
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }
		//ステータスがnullの場合は0をセットする
		if(lending.getStatus() == null) {
			lending.setStatus(0);
		}
		//バリデーションチェック
		if(lending.getStatus() != 0 && lending.getStatus() != 1) {//statusのバリデーション
			ErrorResponse errorResponse = generateErrorResponse(Constants.VALIDATED_STATUS_POST);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		//返却期限のバリデーション
		String deadline = lending.getReturn_deadline();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		try {
			LocalDate date = LocalDate.parse(deadline, formatter);			
			if(!isTomorrowOrLater(date)) {
				ErrorResponse errorResponse = generateErrorResponse(Constants.VALIDATED_RETURN_DEADLINE);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
			}
		}catch(Exception e) {
			ErrorResponse errorResponse = generateErrorResponse(Constants.VALIDATED_INPUT);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		//登録日時のバリデーション
		if(lending.getCreated_at() != null) {
			ErrorResponse errorResponse = generateErrorResponse(Constants.VALIDATED_CREATED_AT);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		//登録者のバリデーション
		if(lending.getCreated_by() != null) {
			ErrorResponse errorResponse = generateErrorResponse(Constants.VALIDATED_CREATED_BY);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		Optional<Item> item = this.itemRepository.findById(lending.getItemid());
		if(item.isEmpty()) {//存在しないアイテムidであればエラーを返す
			ErrorResponse errorResponse = generateErrorResponse(Constants.NOT_FOUND_ITEM);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		if(this.lendingRepository.findById(id).isEmpty()) {//存在しないidであればエラーを返す
			ErrorResponse errorResponse = generateErrorResponse(Constants.NOT_FOUND_LENDING);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		//itemの廃棄フラグが1:廃棄の場合エラー
		Integer deleteFlag = item.map(Item::getDelete_flag).orElse(0);
		if(deleteFlag == 1) {
			ErrorResponse errorResponse = generateErrorResponse(Constants.DELETED_ITEM);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		if(lending.getStatus() == 1) {//ステータスが1:返却済みであれば備品テーブルのステータスを0:貸出可に更新
			updateItemStatus(item, 0);//itemのステータスを0:貸出可に更新
		}
		//登録日時は変更できない
		if(lending.getCreated_at() != null) {
			ErrorResponse errorResponse = generateErrorResponse(Constants.VALIDATED_CREATED_AT);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		//登録者は変更できない
		if(lending.getCreated_by() != null) {
			ErrorResponse errorResponse = generateErrorResponse(Constants.VALIDATED_CREATED_BY);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		//登録者、登録日時をセットする
		lending.setCreated_at(this.lendingRepository.findById(id).map(Lending::getCreated_at).orElse(null));
		lending.setCreated_by(this.lendingRepository.findById(id).map(Lending::getCreated_by).orElse(null));
		this.lendingRepository.save(lending);
		return ResponseEntity.status(HttpStatus.OK).body(lending);
	}
	@DeleteMapping("{id}")
	public ResponseEntity<Object> delete(@PathVariable("id") int id) {
		Optional<Lending> lending = this.lendingRepository.findById(id);
		if(lending.isEmpty()) {
			ErrorResponse errorResponse = generateErrorResponse(Constants.VALIDATED_DELETE_LENDING);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		this.lendingRepository.deleteById(id);
		return  ResponseEntity.status(HttpStatus.OK).build();
	}
	private ErrorResponse generateErrorResponse(String errorMessage) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage(errorMessage);
		return errorResponse;
	}
	private void updateItemStatus(Optional<Item> item, Integer status) {
		item.ifPresent(i -> {
			i.setStatus(status);//itemテーブルのステータス更新
			this.itemRepository.save(i);
		});
	}
}
