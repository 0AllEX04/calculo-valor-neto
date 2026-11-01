package com.ejemplo.calculo_valor_neto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CalculoApiTests {

    @Autowired
    private MockMvc mvc;

    @ParameterizedTest
    @CsvSource({"1200,8,0,0,1200", "2500,8,200,0,2300", "2500,10,200,300,2600",
            "1500,8,0,0,1500", "1500,8.01,0,300,1800", "1500.01,8,120,0,1380.01",
            "0,0,0,0,0", "1500.07,8,120.01,0,1380.06", "1499.99,7.99,0,0,1499.99"})
    void ambosEndpointsCalculan(String sueldo, String horas, double descuento,
                               double bonificacion, double neto) throws Exception {
        var solicitudes = java.util.List.of(
                get("/api/calculo/valor-neto").param("sueldo", sueldo).param("horasTrabajadas", horas),
                post("/api/calculo/valor-neto").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sueldo\":" + sueldo + ",\"horasTrabajadas\":" + horas + "}"));
        for (var solicitud : solicitudes) {
            mvc.perform(solicitud)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.sueldo").value(Double.parseDouble(sueldo)))
                    .andExpect(jsonPath("$.descuento").value(descuento))
                    .andExpect(jsonPath("$.bonificacion").value(bonificacion))
                    .andExpect(jsonPath("$.valorNeto").value(neto));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"{}", "{\"sueldo\":100}", "{\"sueldo\":-1,\"horasTrabajadas\":8}",
            "{\"sueldo\":100,\"horasTrabajadas\":-1}", "{\"sueldo\":\"abc\",\"horasTrabajadas\":8}", "{"})
    void postRechazaDatosInvalidos(String json) throws Exception {
        mvc.perform(post("/api/calculo/valor-neto").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "?sueldo=100", "?sueldo=-1&horasTrabajadas=8",
            "?sueldo=100&horasTrabajadas=-1", "?sueldo=abc&horasTrabajadas=8"})
    void getRechazaDatosInvalidos(String query) throws Exception {
        mvc.perform(get("/api/calculo/valor-neto" + query)).andExpect(status().isBadRequest());
    }
}
