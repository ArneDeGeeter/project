package vibration.JPA;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the foto database table.
 * 
 */
@Entity
@NamedQuery(name = "Foto.findAll", query = "SELECT f FROM Foto f")
public class Foto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private byte[] foto;

	@Column(name = "`foto naam`")
	private String foto_naam;

	// bi-directional many-to-one association to Project
	@ManyToOne
	private Project project;

	// bi-directional many-to-one association to Experimenten
	@ManyToOne
	private Experimenten experimenten;

	public Foto() {
	}

	public Foto(byte[] f, String fn, Experimenten e, Project p) {
		foto = f;
		foto_naam = fn;
		experimenten = e;
		project = p;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getFoto() {
		return this.foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public String getFoto_naam() {
		return this.foto_naam;
	}

	public void setFoto_naam(String foto_naam) {
		this.foto_naam = foto_naam;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Experimenten getExperimenten() {
		return this.experimenten;
	}

	public void setExperimenten(Experimenten experimenten) {
		this.experimenten = experimenten;
	}

}