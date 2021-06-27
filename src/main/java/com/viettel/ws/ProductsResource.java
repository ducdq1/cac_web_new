package com.viettel.ws;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

@Path("/products")
public class ProductsResource {

	@Context
	UriInfo uriInfo;
	@Context
	HttpServletRequest hsr;

	@Context
	Request request;

	public ProductsResource() {

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getProductsAsHtml() {
		String ipRemoteHost = hsr.getRemoteHost();
		System.out.println(ipRemoteHost + ": connected to webservice");
		return new String("OK men");
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void createProduct(@FormParam("id") String id, @FormParam("productname") String name,
			@FormParam("productcategory") String category, @Context HttpServletResponse servletResponse)
					throws IOException {
	 
	}

}