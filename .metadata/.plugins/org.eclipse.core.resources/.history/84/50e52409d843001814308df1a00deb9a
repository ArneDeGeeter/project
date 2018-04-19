package vibration.EJB;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vibration.JPA.Experimenten;
import vibration.JPA.Meting;

/**
 * Session Bean implementation class MetingEJB
 */
@Stateless
public class MetingEJB implements MetingEJBLocal {

	@PersistenceContext(unitName = "VibreJPA")
	private EntityManager em;

	public MetingEJB() {
		// TODO Auto-generated constructor stub
	}

	public List<Meting> getMetingen() {
		Query q = em.createQuery("SELECT m FROM Meting m");
		return q.getResultList();
	}

	@Override
	public boolean checkSTEM(Experimenten experiment){
		if(experiment.getProject().getTeacher()){return true;}
		else{return false;}
	}
	
	@Override
	public Experimenten findExperiment(int id) {
		Query q = em.createQuery("SELECT m FROM Experimenten m WHERE m.id = :id");
		q.setParameter("id", id);
		
		List<Experimenten> exp = q.getResultList();
		
		if (exp.size() == 1) {
			return exp.get(0);
		} else
			return null;
	}

	public String geefOpmerking(int id) {
		Experimenten e = findExperiment(id);
		return e.getOpmerking();
	}

	@Override
	public void verwijderExperiment(int id) {
		Experimenten e = findExperiment(id);

		em.merge(e);
		em.remove(e);

	}

	public static byte[] FloatArray2ByteArray(ArrayList<Float> values) {
		ByteBuffer buffer = ByteBuffer.allocate(4 * values.size());

		for (float value : values) {
			buffer.putFloat(value);
		}

		return buffer.array();
	}

	@Override
	public void berekenGrafiek(Meting m,Experimenten e) {
		
		ArrayList<byte[]> ba = new ArrayList<byte[]>();

		//De drie byteArrays opvragen
		byte[] z = m.getZ();
		byte[] y = m.getY();
		byte[] x = m.getX();
		int Hertz = m.getHertz();
		
		// Functie returned 1 float array van float arrays; 1 float array met
		// f-waarden, 1 met de z-waarden, 1 met y-waarden, 1 met x-waarden
		ArrayList<ArrayList<Float>> meting = testMeting(z, y, x, Hertz);

		// Float arrays omzetten naar byte arrays voor te persisten
		for (ArrayList<Float> lf : meting) {
			ba.add(FloatArray2ByteArray(lf));
		}
		em.joinTransaction();
		Meting m1 = new Meting(ba.get(0),ba.get(1),ba.get(2),ba.get(3),e,1);
		em.persist(m1);
		e.addMeting(m1);
		em.merge(m1);
		
		em.joinTransaction();
		Meting m2 = new Meting(ba.get(4),ba.get(5),ba.get(6),ba.get(7),e,2);
		em.persist(m2); 
		e.addMeting(m2);
		em.merge(m2);
		
	}

	public ArrayList<ArrayList<Float>> testMeting(byte[] z, byte[] yy, byte[] x, int H) {

		ArrayList<byte[]> xyz = new ArrayList<byte[]>();
		xyz.add(z);
		xyz.add(yy);
		xyz.add(x);

		ArrayList<ArrayList<Float>> lijst = new ArrayList<ArrayList<Float>>();
		try {

			Process p = Runtime.getRuntime().exec("cmd");

			BufferedReader inp = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

			byte[] tijd;

			double Herz = H; 

			ArrayList<Float> y = new ArrayList<Float>();
			double sizeArray;
			double eindTijd;
			double periode;
			int a;
			double aantalSeconden;
			String ba = "";
			
			out.write("octave-cli\n");
			
			for (int u = 0; u < 3; u++) { // HOOFDFORLUS

				tijd = xyz.get(u);
				y.clear();
				ByteBuffer buf = ByteBuffer.wrap(tijd);
				for (int i = 0; i < tijd.length / 4; i++) {
					y.add(buf.getFloat());
				}

				// Waarden voor Octave
				sizeArray = y.size(); // Geeft aantal meetpunten
				eindTijd = sizeArray - 1;
				periode = 1 / Herz;
				a = 0;
				aantalSeconden = eindTijd / Herz;

				

				out.write("t = 0:" + periode + ":" + aantalSeconden + ";\n");

				out.write("Fs = 100;\n");
				out.write("t_resampled = " + a + ":1/Fs:" + aantalSeconden + ";\n");

				// Array aanmaken van gemeten waarden
				
				StringBuilder sb = new StringBuilder();
				sb.append("z=[");
				
				for (int aa = 0; aa < y.size() - 1; aa++) {
					sb.append( y.get(aa) + ",");
				}
				sb.append( y.get(y.size() - 1) + "];");

				String zArray = sb.toString();
				out.write(zArray + "\n");

				// Wonderscript van Octave zijn werk laten doen
				out.write("data_resampled = interp1(t, z, t_resampled, 'spline');\nt_resampled = t_resampled - t_resampled(1);\n"
						+ "L = length(data_resampled);\nf = Fs*(0:(L/2))/L;\nA2_data = fft(data_resampled); A2 = abs(A2_data/L);\n"
						+ "A_data = A2(1:L/2+1); A_data(2:end-1) = 2*A_data(2:end-1);\nf1 = 1/Fs*2; f2 = 4/Fs*2;\n"
						+ "filter_order = 4;\npkg load signal\n[b,a] = butter(filter_order,[f1 f2]);\n"
						+ "data_filtered"+u+" = filtfilt(b,a,data_resampled);\nA2_data = fft(data_filtered"+u+"); A2 = abs(A2_data/L);\n"
						+ "A_data"+u+" = A2(1:L/2+1); A_data"+u+"(2:end-1) = 2*A_data"+u+"(2:end-1);\n");
				
				
			}
			//Eerst lengtes van de arrays laten uitprinten zodat we weten hoeveel getallen we moeten verwachten
			for(int i=0;i<2;i++){
				out.write("printf('%d ',length(A_data));\n");
				out.write("printf('%f ', A_data"+i+")\nprintf('%f ', f)\n");
				out.write("printf('%d ',length(data_filtered"+i+"));\n");
				out.write("printf('%f ', data_filtered"+i+")\nprintf('%f ', t_resampled)\n");
				
			}
			out.write("printf('%d ',length(A_data));\n");
			out.write("printf('%f ', A_data"+2+")\nprintf('%f ', f)\n");
			out.write("printf('%d ',length(data_filtered"+2+"));\n");
			out.write("printf('%f ', data_filtered"+2+")\nprintf('%f ', t_resampled);\n");
			
			out.write("quit\n");

			out.flush();

			ba = inp.readLine();
			
			
			
			
			
			// 1 grote string omzetten naar 6 float arrays van meetwaarden en
			// f-waarden
			String[] getallen = ba.split(" ");
			ArrayList<Float> f = new ArrayList<Float>();
			ArrayList<Float> zWaarde = new ArrayList<Float>();
			ArrayList<Float> yWaarde = new ArrayList<Float>();
			ArrayList<Float> xWaarde = new ArrayList<Float>();
			ArrayList<Float> tWaarde = new ArrayList<Float>();
			ArrayList<Float> zMeetWaarde = new ArrayList<Float>();
			ArrayList<Float> yMeetWaarde = new ArrayList<Float>();
			ArrayList<Float> xMeetWaarde = new ArrayList<Float>();
			
			
			int i=0;
			
			int size=Integer.parseInt(getallen[i]);
			i++;
			
			//Z-waarde toevoegen
			for(int e=i;e<i+size;e++){
				zWaarde.add(Float.parseFloat(getallen[e]));
			}
			i+=size;
			
			//Frequenties toevoegen
			for(int e=i;e<i+size;e++){
				f.add(Float.parseFloat(getallen[e]));
			}
			i+=size;
			
			size=Integer.parseInt(getallen[i]);
			i++;
			
			//Z-meetwaarden toevoegen
			for(int e=i;e<i+size;e++){
				zMeetWaarde.add(Float.parseFloat(getallen[e]));
			}
			i+=size;
			
			//T-waarden toevoegen
			for(int e=i;e<i+size;e++){
				tWaarde.add(Float.parseFloat(getallen[e]));
			}
			
			i+=size;
			
			size=Integer.parseInt(getallen[i]);
			i++;
			
			//Y-waarden toevoegen
			for(int e=i;e<i+size;e++){
				yWaarde.add(Float.parseFloat(getallen[e]));
			}
			i+=2*size;
			
			
			size=Integer.parseInt(getallen[i]);
			i++;
			
			//Y-meetwaarden toevoegen
			for(int e=i;e<i+size;e++){
				yMeetWaarde.add(Float.parseFloat(getallen[e]));
			}
			i+=2*size;
			size=Integer.parseInt(getallen[i]);
			i++;

			//X-waarden toevoegen
			for(int e=i;e<i+size;e++){
				xWaarde.add(Float.parseFloat(getallen[e]));
			}
			i+=2*size;
			
			
			size=Integer.parseInt(getallen[i]);
			i++;
			
			//X-meetwaarden toevoegen
			for(int e=i;e<i+size;e++){
				xMeetWaarde.add(Float.parseFloat(getallen[e]));
			}

			
			lijst.add(f);
			lijst.add(zWaarde);
			lijst.add(yWaarde);
			lijst.add(xWaarde);
			
			lijst.add(tWaarde);
			lijst.add(zMeetWaarde);
			lijst.add(yMeetWaarde);
			lijst.add(xMeetWaarde);
			
			
			
			return lijst;

		} catch (IOException e) {
			e.printStackTrace();
			return lijst;
		}

	}
}
