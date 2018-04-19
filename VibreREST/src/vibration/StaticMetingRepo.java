package vibration;

import java.util.ArrayList;
import java.util.List;

import vibration.JPA.Meting;

public class StaticMetingRepo {
	private static StaticMetingRepo metingRepo = null;

	private Meting meting;
	private ArrayList<Meting> m;

	private StaticMetingRepo() {
		m = new ArrayList<Meting>();
	}

	public static StaticMetingRepo getInstance() {
		if (metingRepo == null)
			metingRepo = new StaticMetingRepo();
		return metingRepo;
	}

	public Meting find(String id) {
		/*
		 * Meting m = null; for (Meting bookIt : Meting) { if
		 * (bookIt.getId().equals(id)) book = bookIt; }
		 */
		return meting;
	}

	public void createMeting(Meting m2) {
		//m2.setId("" + m2.size());
		m.add(m2);
	}

	/*
	 * public void merge(Book book) { for (Book bookIt : books) { if
	 * (bookIt.getId().equals(book.getId())) {
	 * bookIt.setNbOfPage(book.getNbOfPage()); bookIt.setPrice(book.getPrice());
	 * bookIt.setTitle(book.getTitle()); } } }
	 */

	/*
	 * public void remove(Book book) { for (Book bookIt : books) { if
	 * (bookIt.getId().equals(book.getId())) books.remove(bookIt); } }
	 */

	public Meting getMeting() {
		return meting;
	}
}
