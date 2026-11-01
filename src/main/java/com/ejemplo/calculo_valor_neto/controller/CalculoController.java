package com.ejemplo.calculo_valor_neto.controller;

import com.ejemplo.calculo_valor_neto.dto.CalculoRequest;
import com.ejemplo.calculo_valor_neto.dto.CalculoResponse;
import com.ejemplo.calculo_valor_neto.service.CalculoService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/calculo/valor-neto")
public class CalculoController {

    private final CalculoService service;

    public CalculoController(CalculoService service) {
        this.service = service;
    }

    @PostMapping
    public CalculoResponse calcularPost(@RequestBody CalculoRequest solicitud) {
        return service.calcular(solicitud);
    }

    @GetMapping
    public CalculoResponse calcularGet(@RequestParam BigDecimal sueldo,
                                      @RequestParam BigDecimal horasTrabajadas) {
        return service.calcular(new CalculoRequest(sueldo, horasTrabajadas));
    }
}
