package com.ejemplo.calculo_valor_neto.dto;

import java.math.BigDecimal;

public record CalculoResponse(BigDecimal sueldo, BigDecimal descuento,
                              BigDecimal bonificacion, BigDecimal valorNeto) {
}
