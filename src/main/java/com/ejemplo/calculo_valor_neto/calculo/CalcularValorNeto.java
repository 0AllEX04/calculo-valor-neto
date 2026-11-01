package com.ejemplo.calculo_valor_neto.calculo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalcularValorNeto {

    public BigDecimal calcularDescuento(BigDecimal sueldo) {
        BigDecimal descuento = sueldo.compareTo(new BigDecimal("1500")) > 0
                ? sueldo.multiply(new BigDecimal("0.08")) : BigDecimal.ZERO;
        return descuento.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularBonificacion(BigDecimal horasTrabajadas) {
        return horasTrabajadas.compareTo(new BigDecimal("8")) > 0
                ? new BigDecimal("300.00") : new BigDecimal("0.00");
    }

    public BigDecimal calcularValorNeto(BigDecimal sueldo, BigDecimal descuento,
                                        BigDecimal bonificacion) {
        return sueldo.subtract(descuento).add(bonificacion).setScale(2, RoundingMode.HALF_UP);
    }
}
