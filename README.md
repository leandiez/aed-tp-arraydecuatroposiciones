# Trabajo Practico - AED 2026 - AirMiles

## Estructura de carpetas:

- especificacion : todo lo que se relaciones al informe en LaTex del TP
- src: el codigo (para cuando arranque la parte 2)
- texmf: Las Macros de LaTex de la catedra para especificar
- README.md : este archivo que es la caratula del repo :)

## Instrucciones para Devcontainer

Los Devcontainers, permiten tener las herramientas necesarias para el desarrollo,
sin tener que instalar paquetes en la maquina local sino que utilizando contenedores
de Docker que son accesibles desde Visual Studio Code.

La ventaja es que todos podemos tener el mismo ambiente de ejecucion y que no se 
llena de "basura" la PC con todas las librerias que se necesitan para programar, ya que los
contenedores son unidades descartables.

Para poder ejecutar los Devcontainers, se necesita:

- Tener instalado Docker (puede ser Docker Engine si estas en Linux, o Docker Desktop en Windows/MacOS)
- Tener la extension Devcontainers en Visual Studio Code.

Una vez instalados esos 2 pre-requisitos, ir a la parte inferior izquierda de VSCode,
(el boton con dos >< ) o bien presionar "Shift+Ctrl+P", y seleccionar "Reabrir en contenedor" (Reopen in container).

La primera vez va a demorar porque tiene que crear la imagen de Docker con todas
las dependencias, pero una vez finalizado abajo a la izquierda aparecera que estamos
corriendo VSCode desde un Container, y si creamos una terminal nueva, veremos que ya 
tenemos las herramientas necesarias (en nuestro caso LaTex Live y JAVA).

NOTA: Para poder correr subir los cambios a Git posiblemente haya que hacerlo 
en modo "local" por el tema de las credenciales. Para volver al modo local,
presionar "Shift+Ctrl+P" y buscar el comando "Open folder in local"
