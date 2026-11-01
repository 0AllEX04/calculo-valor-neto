package com.ejemplo.calculo_valor_neto.dto;

import java.math.BigDecimal;

public record CalculoRequest(BigDecimal sueldo, BigDecimal horasTrabajadas) {
}
