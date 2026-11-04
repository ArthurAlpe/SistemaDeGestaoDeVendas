package com.example.demo.exception;

public class FormaDePagamentoNaoEncontradaException extends RuntimeException{
   public FormaDePagamentoNaoEncontradaException(String mensagem){
       super(mensagem);
   }
}
