package rest;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import db.DbConnection;
import db.entities.Record;

@Path("/records")
public class Endpoints {

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readAll() {
		EntityManager em = null;
		try {
			em = DbConnection.factory.createEntityManager();
			TypedQuery<Record> query = em.createQuery("SELECT r FROM Record r ORDER BY r.id", Record.class);
			List<Record> resultList = query.getResultList();
			Gson gson = new Gson();
			String json = gson.toJson(resultList);
			return Response.status(200).entity(json).build();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(String body) throws IOException {
		Gson gson = new Gson();
		Record record = gson.fromJson(body, Record.class);
		
		EntityManager em = null;
		try {
			em = DbConnection.factory.createEntityManager();
			em.getTransaction().begin();
			em.persist(record);
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return Response.status(200).build();
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") long id, String body) {
		Gson gson = new Gson();
		Record record = gson.fromJson(body, Record.class);
		
		EntityManager em = null;
		try {
			em = DbConnection.factory.createEntityManager();
			em.getTransaction().begin();
			em.merge(record);
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return Response.status(200).build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") long id) {
		EntityManager em = null;
		try {
			em = DbConnection.factory.createEntityManager();
			em.getTransaction().begin();
			Record record = em.find(Record.class, id);
			if (record == null) {
				return Response.status(404).build();
			}
			em.remove(record);
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return Response.status(200).build();
	}
	
}
