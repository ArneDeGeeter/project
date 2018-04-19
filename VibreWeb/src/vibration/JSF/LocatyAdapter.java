package vibration.JSF;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import vibration.JPA.Locaty;

public class LocatyAdapter extends TypeAdapter<Locaty> {

	@Override
	public Locaty read(JsonReader arg0) throws IOException {
		return null;
	}

	@Override
	public void write(JsonWriter writer, Locaty locatie) throws IOException {
		 if (locatie == null) {
	         writer.nullValue();
	         return;
	       }
		 int id=0;
		 if(!locatie.getProject().getFotos().isEmpty()){
			 id=locatie.getProject().getFotos().get(0).getId();
		 }
		 writer.beginObject();
		 writer.name("lat").value(locatie.getLat());
		 writer.name("lng").value(locatie.getLng());
		 writer.name("adres").value(locatie.getAdres());
		 writer.name("naam").value(locatie.getNaam());
		 writer.name("image id").value(id);
		 writer.name("project titel").value(locatie.getProject().getTitel());
		 writer.name("eigenaar").value(locatie.getProject().getPersonen().getNaam());
		 writer.endObject();
		
	}

}
