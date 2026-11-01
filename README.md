# API de cálculo de valor neto

API REST con Java 17, Maven, Spring Boot 3.5.16 y Spring Web. No requiere base de datos.

## Clonar

```bash
git clone https://github.com/0AllEX04/calculo-valor-neto.git
cd calculo-valor-neto
```

Si ya tienes esta carpeta, trabaja directamente en ella; no vuelvas a clonarla dentro.
Los cambios locales deben estar publicados en el remoto para obtenerlos al clonar.

## Ejecutar con Docker

Instala Docker con Docker Compose y arranca Docker Desktop con contenedores Linux.
Desde la raíz del proyecto:

```bash
docker compose up --build
```

La construcción ejecuta las pruebas con Maven y Java 17. La primera ejecución necesita
Internet para descargar imágenes y dependencias. No necesitas Java ni Maven instalados
en el equipo para esta opción. La API queda en `http://localhost:8080`.
El puerto 8080 debe estar libre. Para detenerla, pulsa Ctrl+C y ejecuta:

```bash
docker compose down
```

## Ejecutar sin Docker

Instala un JDK 17 y configura `JAVA_HOME` apuntando a su carpeta. Maven Wrapper descarga
Maven automáticamente; no hace falta instalarlo globalmente.

Windows (PowerShell):

```powershell
.\mvnw.cmd clean verify
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
sh mvnw clean verify
sh mvnw spring-boot:run
```

`clean verify` ejecuta las pruebas y genera `target/calculo-valor-neto-0.0.1-SNAPSHOT.jar`.
También puedes iniciar ese archivo con `java -jar target/calculo-valor-neto-0.0.1-SNAPSHOT.jar`.

## Probar con Postman

### POST

1. Selecciona POST y la URL `http://localhost:8080/api/calculo/valor-neto`.
2. En Body selecciona raw y JSON (`Content-Type: application/json`).
3. Envía:

```json
{"sueldo": 2500, "horasTrabajadas": 10}
```

### GET

Selecciona GET y envía esta URL, sin cuerpo:

```text
http://localhost:8080/api/calculo/valor-neto?sueldo=2500&horasTrabajadas=10
```

Ambos devuelven HTTP 200 y:

```json
{
  "sueldo": 2500,
  "descuento": 200.00,
  "bonificacion": 300.00,
  "valorNeto": 2600.00
}
```

## Reglas y casos de prueba

- Sueldo menor o igual a 1500: descuento cero; mayor a 1500: 8% del sueldo completo.
- Horas mayores a 8: bonificación de S/ 300; 8 o menos: cero.
- Valor neto = sueldo - descuento + bonificación.
- Los importes usan BigDecimal. Descuento y valor neto se redondean a dos decimales
  con HALF_UP. Se conserva el sueldo recibido y se compara con 1500 antes de redondear.
- Se aceptan horas decimales. Ambos campos son obligatorios y no negativos.
  Campos ausentes, valores negativos, tipos incorrectos y JSON inválido producen HTTP 400.

| Sueldo | Horas | Descuento | Bonificación | Valor neto |
|---:|---:|---:|---:|---:|
| 1200 | 8 | 0 | 0 | 1200 |
| 2500 | 8 | 200 | 0 | 2300 |
| 2500 | 10 | 200 | 300 | 2600 |
| 1500 | 8 | 0 | 0 | 1500 |
| 1500.01 | 8 | 120 | 0 | 1380.01 |
| 1500 | 8.01 | 0 | 300 | 1800 |
| 0 | 0 | 0 | 0 | 0 |
| 1500.07 | 8 | 120.01 | 0 | 1380.06 |
| 1499.99 | 7.99 | 0 | 0 | 1499.99 |

Las pruebas cubren estos casos en la calculadora y en ambos endpoints mediante MockMvc,
además de solicitudes inválidas y el arranque del contexto Spring.

## Organización y responsabilidad única

Dentro del paquete existente `com.ejemplo.calculo_valor_neto`:

- `controller/CalculoController`: recibe solicitudes y devuelve respuestas.
- `controller/ApiExceptionHandler`: transforma errores de validación en HTTP 400.
- `service/CalculoService`: valida la entrada y coordina el cálculo.
- `calculo/CalcularValorNeto`: realiza exclusivamente operaciones matemáticas, sin Spring ni HTTP.
- `dto/CalculoRequest` y `dto/CalculoResponse`: representan entrada y salida.

Las únicas dependencias declaradas son Spring Web y Spring Boot Test (solo para pruebas).
