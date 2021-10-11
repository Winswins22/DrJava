// Tell Java that this is a package with the name "doctor"
package doctor;

// Import necessary exceptions/classes from packages to read/write to files 
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.util.Scanner;

// Import necessary exceptions/classes from packages to access the API
import java.net.URI;
import java.net.http.*;
import java.lang.InterruptedException;

// import a list to store the amount of symptoms and illnesses a patient may have, as we dont know how many they will have 
import java.util.ArrayList;

// use Dictionary to store Disease_ID -> Disease_Treatment
import java.util.Dictionary;
import java.util.Hashtable;

// import json tools from external Maven repository (https://mvnrepository.com/artifact/org.json/json)
import org.json.*;

// Provide a class to easily manage API calls
public class APIManager {

  // Toggle between development and production
  boolean logDebugInfo = false;

  // Ask the patient to wait while we fetch info from the API
  private void askWait(String waitFor){
    System.out.println("Please wait while I get my clipboard for " + waitFor + ".\n");
  }

  // Manually encode the an array of symptoms into ASCII string to send to API for diagnosis
  private String encodeIntArray(ArrayList<Integer> arrToEncode){

    // Initialize the string with the ASCII value for "["
    String encodedString = "%5B";

    // Loop through the array, adding in the ASCII value for "," (%2C) alongside the ints in the array
    for (int i = 0; i < arrToEncode.size(); i ++){
      
      // add the int to array
      encodedString += Integer.toString(arrToEncode.get(i));

      // if last elem, do not add a comma
      if (i != arrToEncode.size() - 1){
        encodedString += "%2C";
      }

    }
    

    // add the ASCII value for "]" at the end
    encodedString += "%5D";    

    return encodedString;
  }

  // Increment the value in the "api_calls.txt" file if an api call can be made
  // addCall : boolean :Some API calls dont cost an API call. Only a "Diagnose" API call will consume a call. Toggle true/false to add a call. Default : false
  public boolean canMakeAPICall(boolean addCall) {
    
    try {
      // Declare constants
      // The amount of API calls that can be made for $0 is 100. Declaring as 95 to prevent logic errors from going above 100
      int callLimit = 95;

      // Get all necessary items to read the file
      String dataDirectory = "./api_calls.txt";
      File APIData = new File(String.valueOf(dataDirectory));
      Scanner scan = new Scanner(APIData);

      // Read the number in the file
      int fileNumber = Integer.parseInt(scan.nextLine());

      // make sure that the number can be read from the file
      if (logDebugInfo){
        System.out.println ("# of API calls made: " + fileNumber);
      }

      // Get all necessary items to write to the file
      FileWriter writer = new FileWriter(String.valueOf(dataDirectory));

      // Check if more calls can be made
      if (fileNumber < callLimit){
        // Can still make more calls
        // Incremement number in file by one
        if (addCall){
          writer.write(Integer.toString(fileNumber + 1));
        }
        else {
          writer.write(Integer.toString(fileNumber));
        }
        
        writer.close();
        return true;
      }
      // No more calls can be made, Deny access to API
      // Rewrite number to api_calls.txt
      writer.write(Integer.toString(fileNumber));
      System.out.println("Access to API is blocked. No more requests can be made.");
      writer.close();
      return false;
    }
    // catch FileNotFoundException as working with files may cause this exception to be thrown
    catch (FileNotFoundException e){
      System.out.println("Error (FileNotFoundException): ");
      System.out.print(e);
    }
    // catch IOException as working with files may cause this exception to be thrown
    catch (IOException e){
      System.out.println("Error (IOException): ");
      System.out.print(e);
    }
    return false;

  }

  // Overload "canMakeAPICall" so that if "addCall" not specified, assume it will not cost a call, as only the "Diagnosis" call will consume a call
  public boolean canMakeAPICall() {
    return canMakeAPICall(false);
  }

  // Reads data from a hashmap. Since this data is not in the API, only some common diseases have treaments. (Since this is only a prototype though, it should be ok) 
  public String tryToGetDiseaseTreatment(int diseaseID, String disease){

    // use a dictionary to store treatments to diseases. key-value pairs (ID -> Treatment)
    Dictionary IDtoTreatment = new Hashtable();
    IDtoTreatment.put(80, "take care of yourself and make sure to stay hydrated."); // common cold
    IDtoTreatment.put(102, "make sure to sleep on your back or on your side instead of on your front."); // back pain


    if (IDtoTreatment.get(diseaseID) != null){
      return "To treat " + disease + ", "  + IDtoTreatment.get(diseaseID) + "\n";
    }
    else {
      return "You may want to search online for \"" + disease + "\"\n";   
    }

  }

  // do a GET request for all supported body parts in the API
  public JSONArray getBodyLocations(){
    // check if making another API call will exceed the amount of given calls
    if (!canMakeAPICall()){
      JSONArray junk = new JSONArray("[\"junk\":\"junk\"]");
      return junk;
    }

    // ask the patient to wait while we fetch API data
    askWait("body parts");

    try {
      // make an HTTP GET request to retrieve the body parts that could be affected. use .env to hide API key
      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://priaid-symptom-checker-v1.p.rapidapi.com/body/locations?language=en-gb&format=json"))
        .header("x-rapidapi-host", "priaid-symptom-checker-v1.p.rapidapi.com")
        .header("x-rapidapi-key", System.getenv("api-key"))
        .method("GET", HttpRequest.BodyPublishers.noBody())
        .build();
      HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
      
      if (logDebugInfo){
        System.out.println("Raw data:");
        System.out.println(response.body());
      }

      // Create a JSONArray object with the retrieved data and return it
      JSONArray json = new JSONArray(response.body());
      return json;

    }
    // catch IOException as working with APIs may cause this exception to be thrown
    catch (IOException e){
      System.out.println("Error (IOException): ");
      System.out.print(e);
    }
    // catch InterruptedException; as working with APIs may cause this exception to be thrown
    catch (InterruptedException e){
      System.out.println("Error (InterruptedException): ");
      System.out.print(e);
    }

    // Putting this here to silence a compiler error
    JSONArray junk = new JSONArray("[\"junk\":\"junk\"]");
    return junk;

  }

  // do a GET request for all supported body sublocations in the API
  public JSONArray getBodySublocations(int sublocationID){
    // check if making another API call will exceed the amount of given calls
    if (!canMakeAPICall()){
      JSONArray junk = new JSONArray("[\"junk\":\"junk\"]");
      return junk;
    }

    // ask the patient to wait while we fetch API data
    askWait("body part sublocations");

    try {
      // make an HTTP GET request to retrieve the sublocations of the body part given. use .env to hide API key
      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://priaid-symptom-checker-v1.p.rapidapi.com/body/locations/" + Integer.toString(sublocationID) + "?language=en-gb"))
        .header("x-rapidapi-host", "priaid-symptom-checker-v1.p.rapidapi.com")
        .header("x-rapidapi-key", System.getenv("api-key"))
        .method("GET", HttpRequest.BodyPublishers.noBody())
        .build();
      HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
      
      if (logDebugInfo){
        System.out.println("Raw data:");
        System.out.println(response.body());
      }

      // Create a JSONArray object with the retrieved data and return it
      JSONArray json = new JSONArray(response.body());
      return json;

    }
    // catch IOException as working with APIs may cause this exception to be thrown
    catch (IOException e){
      System.out.println("Error (IOException): ");
      System.out.print(e);
    }
    // catch InterruptedException; as working with APIs may cause this exception to be thrown
    catch (InterruptedException e){
      System.out.println("Error (InterruptedException): ");
      System.out.print(e);
    }

    // Putting this here to silence a compiler error
    JSONArray junk = new JSONArray("[\"junk\":\"junk\"]");
    return junk;

  }

  // do a GET request for all supported body sublocations symptoms in the API
  public JSONArray getSymptoms(int sublocationID, String gender){
    // check if making another API call will exceed the amount of given calls
    if (!canMakeAPICall()){
      JSONArray junk = new JSONArray("[\"junk\":\"junk\"]");
      return junk;
    }

    // ask the patient to wait while we fetch API data
    askWait("symptoms for body part sublocations");

    try {
      // make an HTTP GET request to retrieve the symptoms given the sublocation of the body part and gender. use .env to hide API key
      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://priaid-symptom-checker-v1.p.rapidapi.com/symptoms/" + Integer.toString(sublocationID) + "/" + ((gender == "M") ? "man" : "woman") + "?language=en-gb"))
        .header("x-rapidapi-host", "priaid-symptom-checker-v1.p.rapidapi.com")
        .header("x-rapidapi-key", System.getenv("api-key"))
        .method("GET", HttpRequest.BodyPublishers.noBody())
        .build();
      HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
      
      if (logDebugInfo){
        System.out.println("Raw data:");
        System.out.println(response.body());
      }

      // Create a JSONArray object with the retrieved data and return it
      JSONArray json = new JSONArray(response.body());
      return json;

    }
    // catch IOException as working with APIs may cause this exception to be thrown
    catch (IOException e){
      System.out.println("Error (IOException): ");
      System.out.print(e);
    }
    // catch InterruptedException; as working with APIs may cause this exception to be thrown
    catch (InterruptedException e){
      System.out.println("Error (InterruptedException): ");
      System.out.print(e);
    }

    // Putting this here to silence a compiler error
    JSONArray junk = new JSONArray("[\"junk\":\"junk\"]");
    return junk;

  }


  // do a GET request for potential ilnesses in the API
  public JSONArray getDiagnosis(ArrayList<Integer> symptoms, String gender, int dateOfBirth){
    // check if making another API call will exceed the amount of given calls
    if (!canMakeAPICall(true)){
      JSONArray junk = new JSONArray("[\"junk\":\"junk\"]");
      return junk;
    }

    // ask the patient to wait while we fetch API data
    askWait("your diagnosis");

    try {
      // make an HTTP GET request to retrieve the the user's diagnosis using an encoded symptoms array, gender, and date of birth. use .env to hide API key
      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://priaid-symptom-checker-v1.p.rapidapi.com/diagnosis?language=en-gb&symptoms=" + encodeIntArray(symptoms) + "&year_of_birth="+ Integer.toString(dateOfBirth)+ "&gender=" + ((gender == "M") ? "male" : "female")))
        .header("x-rapidapi-host", "priaid-symptom-checker-v1.p.rapidapi.com")
        .header("x-rapidapi-key", System.getenv("api-key"))
        .method("GET", HttpRequest.BodyPublishers.noBody())
        .build();
      HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
      
      if (logDebugInfo){
        System.out.println("Raw data:");
        System.out.println(response.body());
      }

      // Create a JSONArray object with the retrieved data and return it
      JSONArray json = new JSONArray(response.body());
      return json;

    }
    // catch IOException as working with APIs may cause this exception to be thrown
    catch (IOException e){
      System.out.println("Error (IOException): ");
      System.out.print(e);
    }
    // catch InterruptedException; as working with APIs may cause this exception to be thrown
    catch (InterruptedException e){
      System.out.println("Error (InterruptedException): ");
      System.out.print(e);
    }

    // Putting this here to silence a compiler error
    JSONArray junk = new JSONArray("[\"junk\":\"junk\"]");
    return junk;

  }

}