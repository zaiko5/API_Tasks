package com.taskList.Auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

//Servicio encargado de todo lo relacionado a los tokens (validar, generar, extraer, verificar expiracion)
@Service
public class JWTService {

    private final String SECRET_KEY = "axel123456789"; //Clave para generar el token.

    //Funcion para generar el token, regresa un token tipo String.
    public String generarToken(UserDetails userDetails) { //Necesita un objeto UD (implementacion)
        return Jwts.builder() //Punto de partida para el TOKEN.
                .setSubject(userDetails.getUsername()) //Definir a quien le pertenece el token (username).
                //Claim: dar informacion de cual es el rol del usuario.
                //Iterator: Iterar sobre la lista de getAuthorities.
                //Next: Obtener el siguiente xd.
                //.getAuthority: devuelve el nombre del rol o permiso asociado al usuario, que normalmente es algo como "ROLE_USER", "ROLE_ADMIN", etc.
                .claim("rol", userDetails.getAuthorities().iterator().next().getAuthority())
                .setIssuedAt(new Date()) //Establecer fecha de inicio de expedicion del token.
                //Definir la hora de expiracion del token.
                //CurrentTimeMillis: Obtener el tiempo actual en milisegundos.
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 día
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) //Firmar el token con la clave secreta.
                .compact();
    }

    //Funcion para extraer el username dado el token
    public String extraerUsername(String token) {
        return Jwts.parser() //Creando y configurando el analisis de token.
                .setSigningKey(SECRET_KEY) //Establecer la clave secreta para verificar la firma.
                .parseClaimsJws(token) //Analizando los claims del token (verificando que esté correcta).
                .getBody() //Obtener los claims del token.
                .getSubject(); //Obtener el user del token (o el sujeto en si, puede ser email o username).
    }

    //Funcion para validar si el token está expirado o no.
    private boolean estaExpirado(String token) { //Necesita como parámetro el token.
        //Verificando que el token sea valido
        Date expiration = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration(); //Obteniendo la expiracion del token
        //Retorna un booleano comparando si la expiracion obtenida es anterior de la fecha actual.
        return expiration.before(new Date());
    }

    //Funcion para validar el token dado este mismo y un userDetails (implementacion) para obtener el username de este.
    public boolean validarToken(String token, UserDetails userDetails) { //Necesita el token y un objeto userDetails (implementacion) para obtener directamente el username.
        String username = extraerUsername(token); //Obtenemos el username del token con la funcion extraerUsername.
        //Retornamos un booleano, comparando si el username del token es igual user del userDetailsImpl y verificando que no esté expirado)
        return username.equals(userDetails.getUsername()) && !estaExpirado(token);
    }


}
