package vibration.JPA;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the experimenten database table.
 * 
 */
@Entity
@NamedQuery(name = "Experimenten.findAll", query = "SELECT e FROM Experimenten e")
public class Experimenten implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private String opmerking;

	private String titel;

	// bi-directional many-to-one association to Project
	@ManyToOne
	private Project project;

	// bi-directional many-to-one association to Foto
	@OneToMany(mappedBy = "experimenten")
	private List<Foto> fotos;

	// bi-directional many-to-one association to Meting
	@OneToMany(mappedBy = "experimenten")
	private List<Meting> metings;

	public Experimenten() {
		date = new Date();
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getOpmerking() {
		return this.opmerking;
	}

	public void setOpmerking(String opmerking) {
		this.opmerking = opmerking;
	}

	public String getTitel() {
		return this.titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<Foto> getFotos() {
		return this.fotos;
	}

	public void setFotos(List<Foto> fotos) {
		this.fotos = fotos;
	}

	public Foto addFoto(Foto foto) {
		getFotos().add(foto);
		foto.setExperimenten(this);

		return foto;
	}

	public Foto removeFoto(Foto foto) {
		getFotos().remove(foto);
		foto.setExperimenten(null);

		return foto;
	}

	public List<Meting> getMetings() {
		return this.metings;
	}

	public void setMetings(List<Meting> metings) {
		this.metings = metings;
	}

	public Meting addMeting(Meting meting) {
		getMetings().add(meting);
		meting.setExperimenten(this);

		return meting;
	}

	public Meting removeMeting(Meting meting) {
		getMetings().remove(meting);
		meting.setExperimenten(null);

		return meting;
	}

}