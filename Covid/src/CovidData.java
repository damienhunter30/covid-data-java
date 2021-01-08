import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CovidData{
	public static String dataMod;
	
	public static void fetchData() throws Exception{
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://coronavirus-smartable.p.rapidapi.com/stats/v1/IE/")).header("x-rapidapi-key", "32c9f433d9msh09008507f8ad5f6p197d5ejsnfbe987478ae8")
				.header("x-rapidapi-host", "coronavirus-smartable.p.rapidapi.com").method("GET", HttpRequest.BodyPublishers.noBody()).build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		formatData(response.body());
	}
	
	public static void formatData(String response) throws IOException{
		String[] data = response.split(",");
		FileWriter writer = new FileWriter("C:\\temp\\coviddata.csv");
		writer.write("Date,Cases,Deaths,Recovered,\n");
	    
		for(int x = 0; x < data.length; x++){
			dataMod = data[x].replace("\n", "").replace("{", "").replace("}", "").trim();
			dataMod = dataMod.replace("]", "").replace("\"", "").replace("history: [", "").trim();
			
			if(dataMod.contains("date:") || dataMod.contains("confirmed:") || dataMod.contains("deaths:") || dataMod.contains("confirmed:") || dataMod.contains("recovered:")){
				dataMod = dataMod.replace("date: ", "").replace("confirmed: ", "").replace("deaths: ", "").replace("recovered: ", "");
				dataMod = dataMod.replace("T00:00:00", "");
				System.out.print(dataMod + ", ");
				writer.write(dataMod + ",");
				
				if(x % 4 == 0){
					System.out.println();
					writer.write(",\n");
				}
			}
		}
		writer.close();
	}

	public static void main(String[] args) throws Exception{
		CovidData.fetchData();
	}
}