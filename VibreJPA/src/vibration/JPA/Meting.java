package vibration.JPA;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the meting database table.
 * 
 */
@Entity
@NamedQuery(name = "Meting.findAll", query = "SELECT m FROM Meting m")
public class Meting implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;
	
	@Column(name = "hertz")
	private int hertz;
	
	private int type;
	
	private byte[] tijd;

	private byte[] x;

	private byte[] y;

	private byte[] z;

	// bi-directional many-to-one association to Experimenten
	@ManyToOne
	private Experimenten experimenten;

	public Meting() {
	}

	public Meting(Meting meting) {
		// TODO Auto-generated constructor stub
	}
	public Meting(byte[] fr, byte[] zz, byte[] yy, byte[] xx, Experimenten exp, int h) {
		tijd = fr;
		z = zz;
		y = yy;
		x = xx;
		experimenten = exp;
		hertz= h;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getTijd() {
		return this.tijd;
	}

	public void setTijd(byte[] tijd) {
		this.tijd = tijd;
	}

	public byte[] getX() {
		return this.x;
	}

	public void setX(byte[] x) {
		this.x = x;
	}

	public byte[] getY() {
		return this.y;
	}

	public void setY(byte[] y) {
		this.y = y;
	}

	public byte[] getZ() {
		return this.z;
	}

	public void setZ(byte[] z) {
		this.z = z;
	}

	public Experimenten getExperimenten() {
		return this.experimenten;
	}

	public void setExperimenten(Experimenten experimenten) {
		this.experimenten = experimenten;
	}

	public int getHertz() {
		return hertz;
	}

	public void setHertz(int hertz) {
		this.hertz = hertz;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}