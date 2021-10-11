/* 
File Name: DrJava 
By: Winston Ho
Date Updated: *see history*
Version: Alpha 0.2.1a
Assignment: My Programming Challenge #1

Notes:
- Program may lock down if too many API calls are made

Sources used:
https://www.youtube.com/watch?v=lHFlAYaNfdo
https://rapidapi.com/priaid/api/symptom-checker/
https://www.youtube.com/watch?v=Hv_a3ZBSO_g
https://www.youtube.com/watch?v=hUyDHkqU6gc
https://www.tutorialspoint.com/maven/maven_creating_project.htm
https://www.youtube.com/watch?v=U-5VHRvOFpA
https://stackoverflow.com/questions/2591098/how-to-parse-json-in-java
*/

/*
Steps:
- Ask the user their age and gender
- Ask the user which body part and body part sublocation hurts from a list of parts (gotten from the API)
- Ask the user what symptoms they have (gotten from the API)
- Make a call to the API to retrieve the disease
- Give the user the diagnosis (requires year of birth, gender, symptoms)
- Give the user possible treatments (brute forced cuz prototype)
*/

// import a list to store the amount of symptoms and illnesses a patient may have, as we dont know how many they will have 
import java.util.ArrayList;

// Import custom class "APIManager" from custom package "doctor" to get API data
import doctor.APIManager;

// Import custom class "RecursiveScanner" from custom package "recursive" to get user input
import recursive.RecursiveScanner;

// Import custom class "IOManager" from custom package "io" to get data from the user
import io.IOManager;

// import json tools from external Maven repository to process API data(https://mvnrepository.com/artifact/org.json/json)
import org.json.*;

class Main {

  public static void main(String[] args) {
    // Import the 2 classes for better organization of code
    IOManager userInteractions = new IOManager();
    APIManager API = new APIManager();

    // These will hold the user's data, neccessary for the diagnosis of potential issues
    int yearOfBirth = 0;
    String gender = "";

    int injuredBodyPartID = 0;
    int injuredBodyPartSublocationID = 0;
    // use a list to store the amount of symptoms a patient may have, as we dont know how many they will have 
    ArrayList<Integer> symptomIDs = new ArrayList<Integer>();

    // These will hold data from the API. initialized to [{"hello" : "world"}]
    JSONArray bodyParts = new JSONArray("[{\"hello\":\"world\"}]");
    JSONArray bodyPartSublocations = new JSONArray("[{\"hello\":\"world\"}]");
    JSONArray potentialSymptoms = new JSONArray("[{\"hello\":\"world\"}]");
    JSONArray diagnosedIllnesses = new JSONArray("[{\"hello\":\"world\"}]");
    
    // Start user interaction to get data
    // Introduce the doctor to make the program seem more human-like
    userInteractions.introduce();

    // Get the user's age and birth year
    gender = userInteractions.requestGender();
    yearOfBirth = userInteractions.requestYearOfBirth();
    
    // Get the user's injured body part
    bodyParts = API.getBodyLocations();
    injuredBodyPartID = userInteractions.requestHurtBodyLocation(bodyParts);

    // Get the user's injured body part sublocation
    bodyPartSublocations = API.getBodySublocations(injuredBodyPartID);
    injuredBodyPartSublocationID = userInteractions.requestHurtBodySublocation(bodyPartSublocations);

    // Get the user's symptoms
    potentialSymptoms = API.getSymptoms(injuredBodyPartSublocationID, gender);
    symptomIDs = userInteractions.requestSymptoms(potentialSymptoms);

    // Get and display the user's diagnosis
    diagnosedIllnesses = API.getDiagnosis(symptomIDs, gender, yearOfBirth);
    userInteractions.displayDiagnosis(diagnosedIllnesses);

    // Get the user's treatment, if possible (May not be possible as I had to manually enter an entry for each disease, but its just a prototype so its ok)
    userInteractions.tryToDisplayDiseaseTreaments(diagnosedIllnesses);

    userInteractions.sayGoodbye();
  }




}