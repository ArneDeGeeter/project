import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;


class Process{
	int pid;
	int arrivalTime;
	int serviceTime;
	int remainingTime;
	int startTime;
	int eindtijd=0;
	int omloopTijd;
	int wachtTijd;
	int normOmloopTijd;
	
	public Process(int a,int b,int c, int d) {
		pid=a;arrivalTime=b;serviceTime=c;remainingTime=d;
	}
	
	public int getRemainingTime() {return remainingTime;}
	
	public int getArrivalTime() {
		return arrivalTime;
	}

	public int getServiceTime() {
		return serviceTime;
	}

	public void setStartTime(int s) {
		startTime = s;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setEindtijd(int i) {
		eindtijd = i;
	}

	public int getEindtijd() {
		return eindtijd;
	}

	public int setOmlooptijd() {
		omloopTijd = eindtijd - arrivalTime;
		return omloopTijd;
	}

	public int setWachtTijd() {
		wachtTijd = eindtijd - arrivalTime - serviceTime;
		return wachtTijd;
	}

	public int setNormOmloopTijd() {
		normOmloopTijd = (eindtijd - arrivalTime) / serviceTime;
		return normOmloopTijd;
	}

	public int getWachtTijd() {
		return wachtTijd;
	}

	public int getNormOmloopTijd() {
		return normOmloopTijd;
	}

	public int getOmlooptijd() {
		return omloopTijd;
	}
	
	public boolean processed() {
		remainingTime--;
		if(remainingTime>0) {return false;}
		else {return true;}
	}
}

class ArrivalTimeComparator implements Comparator<Process>{
	@Override
	public int compare(Process o1, Process o2) {
		return o1.getArrivalTime()-o2.getArrivalTime();
	}
}

class RemainingTimeComparator implements Comparator<Process>{
	@Override
	public int compare(Process o1, Process o2) {
		return o1.getRemainingTime()-o2.getRemainingTime();
	}
}

class ServiceTimeComparator implements Comparator<Process>{
	@Override
	public int compare(Process o1, Process o2) {
		return o1.getServiceTime()-o2.getServiceTime();
	}
}

public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}
  
public void start(Stage stage) {
	stage.setTitle("Grafieken SRT");
	//SRT and RR
	// defining the axes
	final NumberAxis xAxis = new NumberAxis();
	final NumberAxis yAxis = new NumberAxis();
			
	final NumberAxis xAxis2 = new NumberAxis();
	final NumberAxis yAxis2 = new NumberAxis();
	xAxis.setLabel("");
			
	// creating the chart
	final LineChart<Number, Number> lineChart1 = new LineChart<Number, Number>(xAxis, yAxis);
	final LineChart<Number, Number> lineChart2 = new LineChart<Number, Number>(xAxis2, yAxis2);
	lineChart1.setTitle("genormalizeerde TAT");
	lineChart2.setTitle("Wachttijd");
	// defining a series
	XYChart.Series series1 = new XYChart.Series();
	series1.setName("genormalizeerde TAT ifv percentiel");
	XYChart.Series series2 = new XYChart.Series();
	series2.setName("Wachttijd ifv percentiel");

	
	  
	Process p;
	int pid;
	int at;
	int st;
	int clock=10000;
	double aantalProc = 50000;
	int normOmloopPPC = 0;
	int wachtPPC = 0;
	double gemOmloop = 0;
	double gemNormOmloop = 0;
	double gemWacht = 0;
	double percentiel = aantalProc / 100;
	
	Comparator<Process> ATcomp = new ArrivalTimeComparator();	
	Comparator<Process> RTcomp = new RemainingTimeComparator();	
	
	//2 priority queue's aanmaken
	PriorityQueue<Process> ATqueue = new PriorityQueue<Process>((int)aantalProc, ATcomp);
	PriorityQueue<Process> RTqueue = new PriorityQueue<Process>((int)aantalProc, RTcomp);  
	
    try {

	File fXmlFile = new File("processen50000.xml");
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);
			
	//optional, but recommended
	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	doc.getDocumentElement().normalize();

	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			
	NodeList nList = doc.getElementsByTagName("process");
			
	System.out.println("----------------------------");

	
	for (int temp = 0; temp < nList.getLength(); temp++) {

		Node nNode = nList.item(temp);
				
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;

			
			
			pid= Integer.parseInt(eElement.getElementsByTagName("pid").item(0).getTextContent());
			at= Integer.parseInt(eElement.getElementsByTagName("arrivaltime").item(0).getTextContent());
			st=Integer.parseInt(eElement.getElementsByTagName("servicetime").item(0).getTextContent());
			
			if(at<clock) {clock=at;}
			
			p=new Process(pid,at,st,st);
			
			ATqueue.add(p);
		}
	}
    } catch (Exception e) {
	e.printStackTrace();
    }
    /*
    ArrayList<Process> pLijst = new ArrayList<Process>();
    
    for(int i=0;i<10;i++) {
    	pLijst.add(processenLijst.get(i));
    }
    processenLijst=pLijst;
    */
    
    //Start van Shortest Remaining Time-Algorithme
    
    //clock=(int)Math.ceil(clock/10);
    
    ArrayList<Process> afgewerkteProcessen=new ArrayList<Process>();
    boolean stoppen;
    //System.out.println(processenLijst.size());
    while(ATqueue.size()!=0||RTqueue.size()!=0) {
    	if(ATqueue.size()!=0) {
    	stoppen=false;
    	while(!stoppen) {
    		if(ATqueue.element().getArrivalTime()<clock) {RTqueue.add(ATqueue.remove());if(ATqueue.size()==0) {stoppen=true;}}
    		else {stoppen=true;}
    	}
    	}
    	
    	
    	if(RTqueue.size()!=0) {
    		if(RTqueue.element().processed()) {
    			RTqueue.element().setEindtijd(clock);
    			//System.out.println(RTqueue.element().pid+ " zijn eindtijd: "+RTqueue.element().eindtijd+ " zijn arrival time"+RTqueue.element().arrivalTime);
    			afgewerkteProcessen.add(RTqueue.remove());
    			
    		}
    	}
    	clock++;
    	
     }
    
    System.out.println(clock);
    System.out.println(afgewerkteProcessen.get(0).getEindtijd());
    
    double gemOmloopTijd=0;
    double gemNormOmloopTijd=0;
    double gemWachtTijd=0;
    
    for(Process proc:afgewerkteProcessen) {
    	gemOmloopTijd+=proc.setOmlooptijd();
    	gemNormOmloopTijd+=proc.setNormOmloopTijd();
    	gemWachtTijd+=proc.setWachtTijd();
    	
    }
    
    
    
    gemOmloopTijd=gemOmloopTijd/afgewerkteProcessen.size();
    gemNormOmloopTijd=gemNormOmloopTijd/afgewerkteProcessen.size();
    gemWachtTijd=gemWachtTijd/afgewerkteProcessen.size();
    
    System.out.println("Gemiddelde genormaliseerde omlooptijd: "+gemNormOmloopTijd);
    System.out.println("Gemiddelde omlooptijd: "+gemOmloopTijd);
    
    
    System.out.println("Done.");
    
    Comparator<Process> STcomp = new ServiceTimeComparator();
    PriorityQueue<Process> STqueue = new PriorityQueue<Process>((int)aantalProc, STcomp);
    
    for(Process proc:afgewerkteProcessen) {
    	STqueue.add(proc);}
    
    int i = 0;
	for (Process proc : STqueue) {
		normOmloopPPC += proc.getNormOmloopTijd();
		wachtPPC += proc.getWachtTijd();

		if (i % (percentiel) == 0) {
			series1.getData().add(new XYChart.Data(i / percentiel, normOmloopPPC / percentiel));
			series2.getData().add(new XYChart.Data(i / percentiel, wachtPPC / percentiel));
			normOmloopPPC = 0;
			wachtPPC = 0;
		}
		i++;
	}
	

	FlowPane root = new FlowPane();
	root.getChildren().addAll(lineChart1, lineChart2);
	Scene scene = new Scene(root,800, 1000);
	lineChart1.getData().add(series1);
	lineChart2.getData().add(series2);
	stage.setScene(scene);

	stage.show();
    
    
 
}

}