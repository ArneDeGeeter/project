package vibration;

import java.net.URI;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import vibration.JPA.Meting;

@Path("meting")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class ResponseRestService {

	@Context
	private UriInfo uriInfo;

	private StaticMetingRepo metingRepo = StaticMetingRepo.getInstance();

	@POST
	public Response createMeting(Meting m) {
		if (m == null)
			throw new BadRequestException();

		//metingRepo.createMeting(m);
		URI bookUri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(m.getId())).build();
//		return Response.created(bookUri).build();
		return Response.ok().build();
	}

	@GET
	public Response getAllMetingen() {
		System.out.println("get all books");
		Meting m = new Meting(metingRepo.getMeting());
		return Response.ok(m).build();
	}

}
