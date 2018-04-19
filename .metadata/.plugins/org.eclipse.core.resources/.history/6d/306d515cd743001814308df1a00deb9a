package vibration;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import vibration.JPA.Meting;


@Path("content_types")
@Produces(MediaType.TEXT_PLAIN)
public class ContentTypesRestService {
	
	private StaticMetingRepo metingRepo = StaticMetingRepo.getInstance();
	
	
	@GET
	public String getAsPlainText() {
	     return metingRepo.find("0").toString();
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getAsHtml(){
	     return "<!DOCTYPE html PUBLIC> <HTML> <HEAD><TITLE>Book</TITLE></HEAD>"
	     		+ "<BODY><H1>" + metingRepo.find("0").toString() + "</H1></BODY></HTML>";
	}

/*	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Book getAsJsonAndXML() {
		Book book = bookRepo.find("0");
		return book;
	}*/
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean putName(Meting m) {
		System.out.println(m);
		return true;
	}
}
