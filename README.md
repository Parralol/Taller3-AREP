# Taller3-AREP

## Uso del programa

Para usar apropiadamente el programa se debe ejecutar el ciclo de vida en MAVEN en y un IDE de su preferencia se puede ejecutar el programa, el cliente es tu mismo browser, asi que solo es iniciar el servidor y usarlo a gusto.

para poder ver mas a fondo los paths involucrados, se adjuntaran las URIS necesarias para observar los distintos comportamientos

  * http://localhost:35000/action/recieve?cliente.html
  * http://localhost:35000/action/recieve?error.html
  * http://localhost:35000/action/recieve?prueba.css
  * http://localhost:35000/action/recieve?prueba.wepb
  * http://localhost:35000/action/recieve?xd.jpg

### Extensibilidad

Lo unico que necesitas para extender la funcionalidad de este programa es que agregues tus propios paths y sus respectivas respuestas, ya que esta diseñado para que cualquier persona desee hacer un host local de una pagina web

### Diseño

  Este programa sigue el Principio Open/Closed, ya que no es un proyecto lo suficientemente grande para aplicar todo el principio SOLID, se usa el patron Singleton para el HTTPServer, ademas de Builder 

#### PRUEBAS REALIZADAS
 todas las pruebas realizadas fueron de aceptacion, a la hora de probar el programa con diferentes parametros y verificando si tiene el comportamiento deseado

1. prueba de busqueda de archivos
   se usan las siguentes URI con  el fin de verificar la existencia de los archivos, ademas de inspeccionar el elemento

   nosotros contamos los siguientes archivos:

   ![image](https://github.com/Parralol/Taller3-AREP/assets/110953563/c9988a8c-5ae8-4c2b-8aa5-6a256062e8bf)


Para verificar que existan iniciaremos el programa y usaremos inspeccionar elemento en el browser de preferencia,podremos observar que los archivos vitales son subidos a la misma pagina:


![image](https://github.com/Parralol/Taller3-AREP/assets/110953563/47126c36-621a-44c7-87be-2d9c0a545104)

![image](https://github.com/Parralol/Taller3-AREP/assets/110953563/0f90442e-dbd0-440b-a684-7946bedf2204)

![image](https://github.com/Parralol/Taller3-AREP/assets/110953563/6e8c1a4b-6cb0-4925-9060-3495579b98c6)



y Como podremos observar, la pagina cargara correctamente:

![image](https://github.com/Parralol/Taller3-AREP/assets/110953563/3694639c-a7e5-4418-9834-c9a0cf5126ae)

Ahora, si deseamos acceder a los archivos en otra forma, es solo escribir el archivo en el path respectivo



### Prerequisitos

  * conexión a internet
  * SDK javaSE-1.8 y posterior
  * MAVEN

### Respecto a la instalacion

 Para compilar y testear el programa:
 
  >  mvn test-compile -f "Directorio correspondiente del archivo"

## Despliegue

  El siguiente programa no se encuentra desplegado

## Construido con

  * MAVEN
  * Java
  * HTML
  * CSS
  * JS
