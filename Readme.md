<h1 align="center"> Conversor de Monedas</h1>

<p align="center"><img src="./statics/images/Badge-Conversor.png"/></p> 

## Tabla de contenidos:

- [Descripción y contexto](#descripción-y-contexto)
- [Dependencias](#dependencias)
- [Limitación de responsabilidades](#limitación-de-responsabilidades)

## Descripción y contexto
Este proyecto es un conversor de divisas simple basado en Java. Utiliza la API de ExchangeRate para obtener tasas de conversión entre monedas y permite a los usuarios convertir entre diferentes monedas como el dólar estadounidense (USD), el peso colombiano (COP) y el real brasileño (BRL).
 	

### Dependencias
**Gson:**
- Biblioteca de Google para trabajar con JSON. Se usa para convertir cadenas JSON en objetos Java y viceversa.

- La clase Menu y CurrencyConverter la utilizan para analizar respuestas HTTP y obtener datos de conversión.

**Dotenv:**
- Una biblioteca para cargar variables de entorno desde un archivo .env.

- Se utiliza para obtener la clave de la API de ExchangeRate sin exponerla directamente en el código fuente.

**HttpClient:**
- Una clase de Java introducida en el JDK 11 para enviar solicitudes HTTP/HTTPS.

- Se usa para realizar solicitudes a la API de ExchangeRate y obtener datos sobre las tasas de conversión.


## Limitación de responsabilidades
Buscaba tener el proyecto terminado antes del 25 de abril para cumplir con la fecha recomendada de entrega. Sin embargo, con las clases universitarias y otros cursos que estoy tomando, no pude completarlo a tiempo.
