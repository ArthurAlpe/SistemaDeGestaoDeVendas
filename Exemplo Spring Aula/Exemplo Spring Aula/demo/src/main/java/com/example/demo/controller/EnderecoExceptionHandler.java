package com.example.demo.controller;

import com.example.demo.service.Utils.ApiResponse;
import com.example.demo.service.Utils.ErrorResponse;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/*
    Converte os erros do CRUD em respostas HTTP (404, 409, 400 e 500)
    Válido somente para EnderecoController, não sendo possível utilizar nos demais (assignableTypes).
*/

@RestControllerAdvice(assignableTypes = EnderecoController.class)
public class EnderecoExceptionHandler {
    private ResponseEntity<ApiResponse<Object>> erro(HttpStatus status, String titulo, String mensagem) {
        return ResponseEntity.status(status).body(new ApiResponse<Object>(new ErrorResponse(titulo, mensagem)));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<Object>> naoEncontrado(NoSuchElementException ex) {
        return erro(HttpStatus.NOT_FOUND, "Não encontrado", ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Object>> conflito(IllegalStateException ex) {
        return erro(HttpStatus.CONFLICT, "Conflito", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> regraViolada(IllegalArgumentException ex) {
        return erro(HttpStatus.BAD_REQUEST, "Regra de negócio violada", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> dadosInvalidos(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining("; "));
        return erro(HttpStatus.BAD_REQUEST, "Dados inválidos", msg);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiResponse<Object>> requisicaoInvalida(Exception ex) {
        return erro(HttpStatus.BAD_REQUEST, "Requisição inválida", "Corpo da requisição malformado ou tipo de dado inválido");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> integridade(DataIntegrityViolationException ex) {
        return erro(HttpStatus.CONFLICT, "Conflito", "A operação viola a integridade dos dados (registro com vínculos)");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> erroInterno(Exception ex) {
        return erro(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno", "Erro interno do servidor");
    }
}