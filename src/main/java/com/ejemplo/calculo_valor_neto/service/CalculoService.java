package com.ejemplo.calculo_valor_neto.service;

import com.ejemplo.calculo_valor_neto.calculo.CalcularValorNeto;
import com.ejemplo.calculo_valor_neto.dto.CalculoRequest;
import com.ejemplo.calculo_valor_neto.dto.CalculoResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CalculoService {

    private final CalcularValorNeto calculadora = new CalcularValorNeto();

    public CalculoResponse calcular(CalculoRequest solicitud) {
        if (solicitud.sueldo() == null || solicitud.horasTrabajadas() == null) {
            throw new IllegalArgumentException("sueldo y horasTrabajadas son obligatorios");
        }
        if (solicitud.sueldo().signum() < 0 || solicitud.horasTrabajadas().signum() < 0) {
            throw new IllegalArgumentException("sueldo y horasTrabajadas no pueden ser negativos");
        }
        BigDecimal sueldo = solicitud.sueldo();
        BigDecimal descuento = calculadora.calcularDescuento(sueldo);
        BigDecimal bonificacion = calculadora.calcularBonificacion(solicitud.horasTrabajadas());
        return new CalculoResponse(sueldo, descuento, bonificacion,
                calculadora.calcularValorNeto(sueldo, descuento, bonificacion));
    }
}
