package vibration;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import vibration.EJB.LocationEJBLocal;
import vibration.EJB.LogincaseLocal;
import vibration.EJB.MetingEJBLocal;
import vibration.EJB.ProjectManagementEJBLocal;
import vibration.EJB.UserManagementEJBLocal;
import vibration.JPA.Experimenten;
import vibration.JPA.Locaty;
import vibration.JPA.Meting;
import vibration.JPA.Meting2;
import vibration.JPA.Personen;
import vibration.JPA.Project;

@Path("parameter_service")
@javax.enterprise.context.RequestScoped
public class ParameterRestService {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1953184904583821340L;
	@PersistenceContext
	private EntityManager em = Persistence.createEntityManagerFactory("VibreJPA").createEntityManager();
	private static final String SECRET = Base64.getEncoder().encodeToString("SecretKeyForProjectVerySecure".getBytes());

	@EJB
	private UserManagementEJBLocal userEJB;

	@EJB
	private ProjectManagementEJBLocal projEJB;

	@EJB
	private MetingEJBLocal metingEJB;

	@EJB
	private LocationEJBLocal locationEJB;

	@EJB
	private LogincaseLocal loginEJB;

	@GET
	@Path("path/{text}")
	public String pathParamService(@PathParam("text") String userText) {

		return "Path param: " + userText;
	}

	@GET
	@Path("query")
	public String queryParamService(@QueryParam("zip") Long zip, @QueryParam("city") String city) {
		return "Zip: " + zip + " and city: " + city;
	}

	@GET
	@Path("login")
	public String login(@HeaderParam("naam") String naam, @HeaderParam("wachtwoord") String wachtwoord) {
		wachtwoord = loginEJB.createHash(wachtwoord);
		return werkendeLoginUser(naam, wachtwoord);
	}

	@GET
	@Path("token")
	public String token(@HeaderParam("token") String token) {
		return checkKey(token) != null ? "check" : "fail";
	}

	@GET
	@Path("metingOphalen")
	@Produces(MediaType.APPLICATION_JSON)
	public String metingOphalen(@HeaderParam("token") String token, @HeaderParam("experimentID") String exID,
			@HeaderParam("arrayID") String arrayID) throws IOException {
		Jws<Claims> claims = checkKey(token);
		if (claims == null) {
			return "fail";
		}
		int persId = (int) claims.getBody().get("id");
		if (userEJB.findExperimentByID(Integer.parseInt(exID)).getProject().getPersonen().getIdpersonen() == persId) {

			Meting meting = userEJB.findMeting(Integer.parseInt(exID), Integer.parseInt(arrayID));
			ArrayList<Float> xList = getFloatArray(meting.getX());
			ArrayList<Float> yList = getFloatArray(meting.getY());
			ArrayList<Float> zList = getFloatArray(meting.getZ());

			JSONObject mainNode = new JSONObject();
			mainNode.put("x", xList.toString());
			mainNode.put("y", yList.toString());
			mainNode.put("z", zList.toString());
			return mainNode.toString();
		}
		return null;

	}

	private ArrayList<Float> getFloatArray(byte[] x) throws IOException {
		ByteArrayInputStream bas = new ByteArrayInputStream(x);
		DataInputStream ds = new DataInputStream(bas);
		ArrayList<Float> xList = new ArrayList<>();
		for (int i = 0; i < x.length / 4; i++) {
			xList.add(ds.readFloat());
		}
		return xList;

	}

	@GET
	@Path("header")
	public String headerParamService(@HeaderParam("user") String userAgent, @HeaderParam("token") String token,
			@HeaderParam("xMeting") String xM) throws IOException {
		byte[] byteArray = xM.getBytes();
		ByteArrayInputStream bas = new ByteArrayInputStream(byteArray);
		DataInputStream ds = new DataInputStream(bas);
		float[] fArr = new float[byteArray.length / 4]; // 4 bytes per float
		for (int i = 0; i < fArr.length; i++) {
			fArr[i] = ds.readFloat();
		}

		return "user agent: " + userAgent;
	}

	@POST
	@Path("nieuwProject")
	@Consumes(MediaType.APPLICATION_JSON)
	public String nieuwProject(@HeaderParam("token") String token, @HeaderParam("type") String type,
			@HeaderParam("projectNaam") String projectNaam, Meting2 m,
			@HeaderParam("experimentNaam") String experimentNaam, @HeaderParam("long") String longi,
			@HeaderParam("lat") String lat, @HeaderParam("hertz") String hertz) throws IOException {
		Jws<Claims> claims = checkKey(token);
		if (claims != null) {

			String projToken = projEJB.createProject(projectNaam, type.equals("normaal"),
					(int) claims.getBody().get("id"));
			Project proj = projEJB.vindProjectByToken(projToken);

			Locaty location = new Locaty();

			location.setLat(Float.valueOf(lat));
			location.setLng(Float.valueOf(longi));
			location.setProject(proj);
			locationEJB.persistLocaty(location);

			return maakMeting(token, type, String.valueOf(projEJB.vindProjectByToken(projToken).getId()), m,
					experimentNaam, Integer.parseInt(hertz));
		}
		return null;

	}

	@POST
	@Path("toevoegenToken")
	@Consumes(MediaType.APPLICATION_JSON)
	public String toevoegenToken(@HeaderParam("token") String token, @HeaderParam("type") String type,
			@HeaderParam("projToken") String projectToken, Meting2 m,
			@HeaderParam("experimentNaam") String experimentNaam, @HeaderParam("long") String longi,
			@HeaderParam("lat") String lat, @HeaderParam("hertz") String hertz) throws IOException {

		return maakMeting(token, type, String.valueOf(projEJB.vindProjectByToken(projectToken).getId()), m,
				experimentNaam, Integer.parseInt(hertz));

	}

	@GET
	@Path("project")
	@Produces("text/plain")
	public String project(@HeaderParam("token") String token) throws IOException {
		Jws<Claims> claims = checkKey(token);
		if (claims == null) {
			return "fail";
		}
		List<Project> projects = userEJB.findUserProjects(userEJB.geefPersoon((int) claims.getBody().get("id")));
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < projects.size(); i++) {
			s.append(
					projects.get(i).getId() + "," + projects.get(i).getTitel() + "," + projects.get(i).getProjectToken()
							+ "," + projects.get(i).getPersonen().getIdpersonen() + "," + projects.get(i).getPublic_())
					.append(System.lineSeparator());
		}
		return s.toString();

	}

	@GET
	@Path("experiment")
	@Produces("text/plain")
	public String experiment(@HeaderParam("token") String token, @HeaderParam("projID") String projID)
			 {
		List<Experimenten> experimenten = userEJB.findExperiment(userEJB.findProject(Integer.parseInt(projID)));
		StringBuilder s = new StringBuilder();

		for (int i = 0; i < experimenten.size(); i++) {
			s.append(experimenten.get(i).getId() + "," + experimenten.get(i).getTitel() + ","
					+ experimenten.get(i).getDate().getTime() + ",");
			if (experimenten.get(i).getMetings().size() != 0) {
				s.append(experimenten.get(i).getMetings().get(0).getId()).append(System.lineSeparator());
			} else {
				s.append("null").append(System.lineSeparator());

			}
		}
		return s.toString();

	}

	@POST
	@Path("meting")
	@Consumes(MediaType.APPLICATION_JSON)
	public String getMeting(@HeaderParam("token") String token, @HeaderParam("type") String type,
			@HeaderParam("hertz") String hertz, @HeaderParam("projID") String projID, Meting2 m,
			@HeaderParam("experimentNaam") String experimentNaam) {
		return maakMeting(token, type, projID, m, experimentNaam, Integer.parseInt(hertz));

	}

	private ArrayList<Float> stringToArray(String stringList) {
		ArrayList<Float> list = new ArrayList<>();

		int spatie = 0;
		for (int i = 0; i < stringList.length(); i++) {
			if (stringList.charAt(i) == ' ') {
				list.add(Float.parseFloat(stringList.substring(spatie + 1, i - 2)));
				spatie = i;
			} else if (stringList.charAt(i) == ']') {
				list.add(Float.parseFloat(stringList.substring(spatie + 1, i - 1)));
				spatie = i;

			}
		}
		return list;
	}

	// TODO: Format Function
	private String maakMeting(String token, String type, String projID, Meting2 m, String experimentNaam, int hertz) {
		Jws<Claims> claims = token != null ? checkKey(token) : null;
		if (claims != null || type.equals("toevoegenViaToken")) {
			ArrayList<Float> x = stringToArray(m.getX());
			ArrayList<Float> y = stringToArray(m.getY());
			ArrayList<Float> z = stringToArray(m.getZ());

			Meting meting = new Meting();
			meting.setX(floatArray2ByteArray(x));
			meting.setY(floatArray2ByteArray(y));
			meting.setZ(floatArray2ByteArray(z));
			meting.setHertz((hertz));

			Experimenten e = new Experimenten();
			e.setDate(Calendar.getInstance().getTime());
			e.setTitel(experimentNaam);

			Project p = userEJB.findProject(Integer.parseInt(projID));
			e.setProject(p);
			meting.setExperimenten(e);

			userEJB.addMetingExperiment(meting, e);
			return "a";
		}
		return null;
	}

	public static byte[] floatArray2ByteArray(List<Float> values) {
		ByteBuffer buffer = ByteBuffer.allocate(4 * values.size());

		for (float value : values) {
			buffer.putFloat(value);
		}

		return buffer.array();
	}

	private String werkendeLoginUser(String naam, String wachtwoord) {
		Query q = em.createQuery("SELECT p FROM Personen p WHERE p.email = :email");
		q.setParameter("email", naam);
		List<Personen> personen = q.getResultList();
		if (personen.size() == 1) {
			if (personen.get(0).getWachtwoord().equals(wachtwoord)) {

				return getToken(personen.get(0));
			}
		}
		return "fail";
	}

	public String getToken(Personen p) {

		String compactJws = Jwts.builder().claim("id", p.getIdpersonen()).setSubject(p.getNaam())
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		checkKey(compactJws);
		return compactJws;

	}

	/**
	 * Checks if JWT token is correct
	 * 
	 * @param t
	 *            JWT Token
	 * 
	 * @return claims if token correct, else null
	 */
	private Jws<Claims> checkKey(String t) {
		try {
			Jwts.parser().setSigningKey(SECRET).parseClaimsJws(t);
			return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(t);
		} catch (SignatureException |MissingClaimException |IncorrectClaimException e) {
			return null;
		}

	}
}
