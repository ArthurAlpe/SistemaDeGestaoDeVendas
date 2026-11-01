package com.example.demo.service.Utils;

public final class CpfUtils {

    private CpfUtils() {}

    public static boolean isValido(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) return false;
        if (cpf.chars().distinct().count() == 1) return false; // 111.111.111-11 etc.

        return calcularDigito(cpf, 9) == Character.getNumericValue(cpf.charAt(9))
                && calcularDigito(cpf, 10) == Character.getNumericValue(cpf.charAt(10));
    }

    private static int calcularDigito(String cpf, int tamanho) {
        int soma = 0;
        int peso = tamanho + 1;
        for (int i = 0; i < tamanho; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * peso--;
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }
}