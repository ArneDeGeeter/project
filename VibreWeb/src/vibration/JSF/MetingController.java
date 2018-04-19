package vibration.JSF;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import vibration.EJB.MetingEJBLocal;
import vibration.EJB.ProjectManagementEJBLocal;
import vibration.JPA.Experimenten;
import vibration.JPA.Meting;

@Named
@ViewScoped
public class MetingController implements Serializable {

	private static final long serialVersionUID = -4990170456504942443L;

	@EJB
	private MetingEJBLocal metingEJB;

	private LineChartModel metingChart1;
	private LineChartModel metingChart2;
	private LineChartModel metingChart3;
	private int maxT1;
	private float minY1 = Integer.MAX_VALUE;
	private float maxY1 = Integer.MIN_VALUE;
	private int maxT2;
	private float minY2 = Integer.MAX_VALUE;
	private float maxY2 = Integer.MIN_VALUE;
	private int maxT3;
	private float minY3 = Integer.MAX_VALUE;
	private float maxY3 = Integer.MIN_VALUE;
	private Experimenten experiment = new Experimenten();

	public LineChartModel getMetingChart1() {
		return metingChart1;
	}

	public void setMetingChart1(LineChartModel metingChart) {
		this.metingChart1 = metingChart;
	}
	
	public LineChartModel getMetingChart3() {
		return metingChart3;
	}

	public void setMetingChart3(LineChartModel metingChart) {
		this.metingChart1 = metingChart;
	}

	public boolean geefMetingchart1(){
		return !metingEJB.checkSTEM(experiment);
	}
	
	public void createLineModel() throws IOException {
		Meting[] metingenLijst = new Meting[3];
		
		
		experiment = metingEJB.findExperiment(experiment.getId());
		if(experiment!=null){
			
		if (experiment.getMetings().size() != 0) {
			
			metingenLijst[experiment.getMetings().get(0).getType()]= experiment.getMetings().get(0);
			metingenLijst[experiment.getMetings().get(1).getType()]= experiment.getMetings().get(1);
			metingenLijst[experiment.getMetings().get(2).getType()]= experiment.getMetings().get(2);
		}
		
		System.out.println("CreateLineModel active");
		metingChart1 = initLinearModel(metingenLijst[1]);
		metingChart1.setTitle("Frequentie grafiek");
		metingChart1.setLegendPosition("e");
		Axis yAxis = metingChart1.getAxis(AxisType.Y);
		yAxis.setMin(minY1);
		yAxis.setMax(maxY1);
		Axis xAxis = metingChart1.getAxis(AxisType.X);
		xAxis.setMin(0);
		xAxis.setMax(maxT1);
		metingChart2 = initLinearModel2(metingenLijst[0]);
		metingChart2.setTitle("Gemeten waarden met smartphone");
		metingChart2.setLegendPosition("e");
		Axis yAxis2 = metingChart2.getAxis(AxisType.Y);
		yAxis2.setMin(minY2);
		yAxis2.setMax(maxY2);
		Axis xAxis2 = metingChart2.getAxis(AxisType.X);
		xAxis2.setMin(0);
		xAxis2.setMax(maxT2);
		metingChart3 = initLinearModel3(metingenLijst[2]);
		metingChart3.setTitle("Geresampelde en gefilterde data");
		metingChart3.setLegendPosition("e");
		Axis yAxis3 = metingChart3.getAxis(AxisType.Y);
		yAxis3.setMin(minY3);
		yAxis3.setMax(maxY3);
		Axis xAxis3 = metingChart3.getAxis(AxisType.X);
		xAxis3.setMin(0);
		xAxis3.setMax(maxT3);
	}}
	/*
	 * public void CreateLineModel() { try { metingChart = initLinearModel(); }
	 * catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } metingChart.setTitle("Linear Chart");
	 * metingChart.setLegendPosition("e"); Axis yAxis =
	 * metingChart.getAxis(AxisType.Y); yAxis.setMin(0); yAxis.setMax(10); Axis
	 * xAxis = metingChart.getAxis(AxisType.X); xAxis.setMin(0);
	 * xAxis.setMax(5.1);
	 * 
	 * }
	 */

	private LineChartModel initLinearModel(Meting meting) throws IOException {
		LineChartModel model = new LineChartModel();
		Map<Object, Number> x = new HashMap<Object, Number>();
		Map<Object, Number> y = new HashMap<Object, Number>();
		Map<Object, Number> z = new HashMap<Object, Number>();
		x.put(0, 1);
		y.put(0, 1);
		z.put(0, 1);
		

		if (experiment != null) {
			if (experiment.getMetings().size() != 0) {
				if ((meting != null) && (meting.getX() != null) && (meting.getY() != null) && (meting.getZ() != null)) {

					if(meting.getTijd()==null){System.out.println("a\na\na\na\na\na\na\na\na\naBatman");}
					
					byte[] bufferx = meting.getX();
					byte[] buffery = meting.getY();
					byte[] bufferz = meting.getZ();
					byte[] buffert = meting.getTijd();
					DataInputStream dsx = new DataInputStream(new ByteArrayInputStream(bufferx));
					DataInputStream dsy = new DataInputStream(new ByteArrayInputStream(buffery));
					DataInputStream dsz = new DataInputStream(new ByteArrayInputStream(bufferz));
					DataInputStream dst = new DataInputStream(new ByteArrayInputStream(buffert));
					ArrayList<Float> flx = new ArrayList<Float>();
					ArrayList<Float> fly = new ArrayList<Float>();
					ArrayList<Float> flz = new ArrayList<Float>();
					ArrayList<Float> flt = new ArrayList<Float>();
					//maxT1 = (int) Math.ceil(flt.get((buffert.length / 4)-1));
					//maxT1 = bufferx.length / 4;
					for (int i = 0; i < bufferx.length / 4; i++) {
						flx.add(dsx.readFloat());
						fly.add(dsy.readFloat());
						flz.add(dsz.readFloat());
						flt.add(dst.readFloat());
						minY1 = Math.min(Math.min(minY1, flx.get(i)), Math.min(fly.get(i), flz.get(i)));
						maxY1 = Math.max(Math.max(maxY1, flx.get(i)), Math.max(fly.get(i), flz.get(i)));
						x.put(flt.get(i), flx.get(i));
						y.put(flt.get(i), fly.get(i));
						z.put(flt.get(i), flz.get(i));
					}
					maxT1=(int)Math.ceil(flt.get(flt.size()-1));
				}
			}
		}
		LineChartSeries series1 = new LineChartSeries();
		series1.setLabel("Series 1");

		LineChartSeries series2 = new LineChartSeries();
		series2.setLabel("Series 2");

		LineChartSeries series3 = new LineChartSeries();
		series3.setLabel("Series 3");

		series1.setData(x);
		series1.setShowMarker(false);
		series2.setData(y);
		series2.setShowMarker(false);
		series3.setData(z);
		series3.setShowMarker(false);

		model.addSeries(series1);
		model.addSeries(series2);
		model.addSeries(series3);

		return model;
	}

	private LineChartModel initLinearModel2(Meting meting) throws IOException {
		LineChartModel model = new LineChartModel();
		Map<Object, Number> x = new HashMap<Object, Number>();
		Map<Object, Number> y = new HashMap<Object, Number>();
		Map<Object, Number> z = new HashMap<Object, Number>();
		x.put(0, 1);
		y.put(0, 1);
		z.put(0, 1);

		if (experiment != null) {
			if (experiment.getMetings().size() > 1) {
				if ((meting != null) && (meting.getX() != null) && (meting.getY() != null) && (meting.getZ() != null)) {

					byte[] bufferx = meting.getX();
					byte[] buffery = meting.getY();
					byte[] bufferz = meting.getZ();
					// byte[] buffert = m.getTijd();
					DataInputStream dsx = new DataInputStream(new ByteArrayInputStream(bufferx));
					DataInputStream dsy = new DataInputStream(new ByteArrayInputStream(buffery));
					DataInputStream dsz = new DataInputStream(new ByteArrayInputStream(bufferz));
					ArrayList<Float> flx = new ArrayList<Float>();
					ArrayList<Float> fly = new ArrayList<Float>();
					ArrayList<Float> flz = new ArrayList<Float>();
					ArrayList<Float> flt = new ArrayList<Float>();
					maxT2 = bufferx.length / 4;
					for (int i = 0; i < bufferx.length / 4; i++) {
						flx.add(dsx.readFloat());
						fly.add(dsy.readFloat());
						flz.add(dsz.readFloat());
						flt.add(Float.valueOf(i));
						minY2 = Math.min(Math.min(minY2, flx.get(i)), Math.min(fly.get(i), flz.get(i)));
						maxY2 = Math.max(Math.max(maxY2, flx.get(i)), Math.max(fly.get(i), flz.get(i)));
						x.put(i, flx.get(i));
						y.put(i, fly.get(i));
						z.put(i, flz.get(i));
					}
				}
			}
		}
		LineChartSeries series1 = new LineChartSeries();
		series1.setLabel("Series 1");

		LineChartSeries series2 = new LineChartSeries();
		series2.setLabel("Series 2");

		LineChartSeries series3 = new LineChartSeries();
		series3.setLabel("Series 3");

		series1.setData(x);
		series1.setShowMarker(false);
		series2.setData(y);
		series2.setShowMarker(false);
		series3.setData(z);
		series3.setShowMarker(false);

		model.addSeries(series1);
		model.addSeries(series2);
		model.addSeries(series3);

		return model;
	}

	private LineChartModel initLinearModel3(Meting meting) throws IOException {
		LineChartModel model = new LineChartModel();
		Map<Object, Number> x = new HashMap<Object, Number>();
		Map<Object, Number> y = new HashMap<Object, Number>();
		Map<Object, Number> z = new HashMap<Object, Number>();
		x.put(0, 1);
		y.put(0, 1);
		z.put(0, 1);

		if (experiment != null) {
			if (experiment.getMetings().size() > 2) {
				if ((meting != null) && (meting.getX() != null) && (meting.getY() != null) && (meting.getZ() != null)) {

					byte[] bufferx = meting.getX();
					byte[] buffery = meting.getY();
					byte[] bufferz = meting.getZ();
					byte[] buffert = meting.getTijd();
					DataInputStream dsx = new DataInputStream(new ByteArrayInputStream(bufferx));
					DataInputStream dsy = new DataInputStream(new ByteArrayInputStream(buffery));
					DataInputStream dsz = new DataInputStream(new ByteArrayInputStream(bufferz));
					DataInputStream dst = new DataInputStream(new ByteArrayInputStream(buffert));
					ArrayList<Float> flx = new ArrayList<Float>();
					ArrayList<Float> fly = new ArrayList<Float>();
					ArrayList<Float> flz = new ArrayList<Float>();
					ArrayList<Float> flt = new ArrayList<Float>();
					//maxT3 = bufferx.length / 4;
					//maxT3 = (int) Math.ceil(flt.get((buffert.length / 4)-1));
					for (int i = 0; i < bufferx.length / 4; i++) {
						flx.add(dsx.readFloat());
						fly.add(dsy.readFloat());
						flz.add(dsz.readFloat());
						flt.add(dst.readFloat());
						minY3 = Math.min(Math.min(minY3, flx.get(i)), Math.min(fly.get(i), flz.get(i)));
						maxY3 = Math.max(Math.max(maxY3, flx.get(i)), Math.max(fly.get(i), flz.get(i)));
						x.put(flt.get(i)*10, flx.get(i));
						y.put(flt.get(i)*10, fly.get(i));
						z.put(flt.get(i)*10, flz.get(i));
					}
					maxT3=(int)Math.ceil(flt.get(flt.size()-1)*10);
				}
			}
		}
		LineChartSeries series1 = new LineChartSeries();
		series1.setLabel("Series 1");

		LineChartSeries series2 = new LineChartSeries();
		series2.setLabel("Series 2");

		LineChartSeries series3 = new LineChartSeries();
		series3.setLabel("Series 3");

		series1.setData(x);
		series1.setShowMarker(false);
		series2.setData(y);
		series2.setShowMarker(false);
		series3.setData(z);
		series3.setShowMarker(false);

		model.addSeries(series1);
		model.addSeries(series2);
		model.addSeries(series3);

		return model;
	}
	
	public Experimenten getExperiment() {
		return experiment;
	}

	public void setExperiment(Experimenten experiment) {
		this.experiment = experiment;
	}

	public String toonOpmerking() {
		if (experiment != null) {
			return metingEJB.geefOpmerking(experiment.getId());
		}
		return "";
	}

	public LineChartModel getMetingChart2() {
		return metingChart2;
	}

	public void setMetingChart2(LineChartModel metingChart2) {
		this.metingChart2 = metingChart2;
	}

	public void testMeting() {
		if (experiment.getDate() == null) {
			experiment = metingEJB.findExperiment(experiment.getId());
		}
		Meting meting = experiment.getMetings().get(0);
		metingEJB.berekenGrafiek(meting, experiment);
	}
}
