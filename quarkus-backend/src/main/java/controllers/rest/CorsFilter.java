package org.ugomes.controllers.rest;

import javax.ws.rs.container.*;
import java.io.IOException;
// import java.security.Provider;
import javax.ws.rs.ext.*;
// import org.jboss.resteasy.annotations.providers.Provider;

@Provider
public class CorsFilter implements ContainerResponseFilter {
    
    @Override
    public void filter(ContainerRequestContext requestContext, 
        ContainerResponseContext responseContext) throws IOException {
            responseContext.getHeaders().add(
            "Access-Control-Allow-Origin", "*");
            responseContext.getHeaders().add(
            "Access-Control-Allow-Credentials", "true");
            responseContext.getHeaders().add(
            "Access-Control-Allow-Headers",
            "origin, content-type, accept, authorization");
            responseContext.getHeaders().add(
            "Access-Control-Allow-Methods", 
            "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}