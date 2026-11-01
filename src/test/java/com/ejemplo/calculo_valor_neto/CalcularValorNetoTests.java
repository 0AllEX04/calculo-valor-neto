package com.ejemplo.calculo_valor_neto;

import com.ejemplo.calculo_valor_neto.calculo.CalcularValorNeto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CalcularValorNetoTests {

    private final CalcularValorNeto calculadora = new CalcularValorNeto();

    @ParameterizedTest
    @CsvSource({"1200,8,0,0,1200", "2500,8,200,0,2300", "2500,10,200,300,2600",
            "1500,8,0,0,1500", "1500.01,8,120,0,1380.01", "1500,8.01,0,300,1800",
            "0,0,0,0,0", "1500.07,8,120.01,0,1380.06", "1499.99,7.99,0,0,1499.99"})
    void calculaReglasYLimites(BigDecimal sueldo, BigDecimal horas, BigDecimal descuentoEsperado,
                              BigDecimal bonoEsperado, BigDecimal netoEsperado) {
        BigDecimal descuento = calculadora.calcularDescuento(sueldo);
        BigDecimal bono = calculadora.calcularBonificacion(horas);
        assertThat(descuento).isEqualByComparingTo(descuentoEsperado);
        assertThat(bono).isEqualByComparingTo(bonoEsperado);
        assertThat(calculadora.calcularValorNeto(sueldo, descuento, bono)).isEqualByComparingTo(netoEsperado);
    }
}
