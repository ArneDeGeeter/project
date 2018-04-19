package vibration.JPA;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the meting database table.
 * 
 */

@XmlRootElement
public class Meting2 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;
	
	@Lob
	private String tijd;


	@Lob
	private String x;

	@Lob
	private String y;

	@Lob
	private String z;

	//bi-directional many-to-one association to Experimenten
	@ManyToOne
	private Experimenten experimenten;

	public Meting2() {
	}

	public Meting2(Meting2 meting) {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Experimenten getExperimenten() {
		return this.experimenten;
	}

	public void setExperimenten(Experimenten experimenten) {
		this.experimenten = experimenten;
	}


	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getZ() {
		return z;
	}

	public void setZ(String z) {
		this.z = z;
	}

	public String getTijd() {
		return tijd;
	}
	
}