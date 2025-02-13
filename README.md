# micro-clients

<!-- LOGO DEL PROYECTO -->
<br />
<div align="center">
  <a href="#">
    <img src="https://www.qindel.com/wp-content/uploads/2023/04/spring-boot.jpeg" alt="Logo" width="500">
  </a>
</div>

## About The Project

`micro-clients` Este servicio gestiona cliente persosa para el servicio bancario.
---

## Features

- **API Integration**:
    - Available endpoints:
        - `/clientes/crear`: Crea un cliente con su informacion
        - `/clientes/actualizar`: Actualiza cliente
        - `/clientes/obtener`: Obtiene un cliente o clientes
        - `/clientes/eliminar`: Elimina cliente

---

## Tecnologia usada

- **Lenguaje**: Java Jdk17
- **Framework**: [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.5/maven-plugin)
- **Configuracion**: `application.properties`

---

## Instalacion

### Prerequisites

1. [JDK 17+](https://adoptium.net/)
2. [Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.5/maven-plugin)
3. [IntelliJ IDEA](https://www.jetbrains.com/idea/)
4. Docker

### Steps

1. Clonar repositorio:
   ```bash
   git clone /Users/casa/Documents/Projects/Online/GitHub/micro-clients.git
   cd 
2. Iniciar aplicacion:
   ```bash
   ./mvn clean install
3. Crear la imagen Docker(ver archivo Dockerfile)
    ```bash 
    docker build -t clientes:latest .
4. Crear BD mysql (si au no existe)
    ```bash
    docker pull mysql:9.1.0
5. Iniciar los contenedores definidos en docker-compose.yml
    ```bash
    docker-compose up -d

### Pruebas

1. En el archivo *clientes.postman.json* estan los endpoints para su uso

## License

This project is licensed under the MIT License. Please consult the `LICENSE` file for more details.
