// Tell Java that this is a package with the name "io"
package io;

// Import the Scanner class to call methods not implmented in the RecursiveScanner class
import java.util.Scanner;

// Import custom class "RecursiveScanner" from custom package "recursive"
import recursive.RecursiveScanner;

// Import custom class "APIManager" from custom package "doctor" to get API data
import doctor.APIManager;

// import a list to store the amount of symptoms and diseases a patient may have, as we dont know how many they will have 
import java.util.ArrayList;

// import json tools from external Maven repository (https://mvnrepository.com/artifact/org.json/json)
import org.json.*;

// Manage input-output so that code is easier to read in the Main file
public class IOManager{

  // Introduce Dr. Java to user
  public void introduce(){
    System.out.println("Hello, my name is Dr. Java, and welcome to my office. Today, I'll be diagnosing any illnesses you may have. Let's get started.\n");
  }

  // Leave greeting
  public void sayGoodbye(){
    System.out.println("Alright, this wraps up this visit. Please follow the instructions outlined to cure your illnesses. If you need more help, drop into my office any time. Best of luck to you, goodbye.\n");
  }

  // gets the user's name as a String
  public String requestName(){
    
    Scanner scan = new Scanner(System.in);
    String name = "";

    System.out.println("Hello! What is your name?");

    name = scan.nextLine();
    return name;

  }

  // returns the user's gender as "M" or "W"
  public String requestGender(){
    
    Scanner scan = new Scanner(System.in);
    String gender = "";

    // Declare these as variables instead of string literals so we can use these in String.equalsIgnoreCase() (string literals dont work)
    String M = "M";
    String F = "F";

    // Try to get the user's gender until they enter F or M
    while (true){
      System.out.println("Enter your gender. \"F\" for female and \"M\" for male.");
      gender = scan.nextLine();

      if (gender.equalsIgnoreCase(M)){
        return "M";
      }
      else if (gender.equalsIgnoreCase(F)){
        return "F";
      }
      else {
        System.out.println("Oops! Try again.");
      }
    }

  }

  // Get the user's yearOfBirth with RecursiveScanner
  public int requestYearOfBirth(){
    int yearOfBirth = -1;

    RecursiveScanner rScan = new RecursiveScanner();

    // try to get the user's year of birth until they enter a whole number
    System.out.println("\nPlease enter your year of birth as a whole number:");
    yearOfBirth = rScan.nextInt(true, "Oops! Try again. Try entering a whole number.\n");

    System.out.println();

    return yearOfBirth;
  } 

  // Get the body location which the user is suffering pain from given a list of possible answers
  // returns an int because that's what the API uses for its calls, a number to id a body part
  public int requestHurtBodyLocation(JSONArray data){

    //declare vars
    RecursiveScanner rScan = new RecursiveScanner();

    String nameOfBodyPart = "";
    int ID = 0;
    int injuredBodyPartID = 0;

    // prompt user
    System.out.println("Type in the ID corresponding to the body part that is hurt.\n");

    // output the data in the form:
    // part_id    part_name
    System.out.printf("%-8s %s\n", "Part ID", "Part Name");

    for (int i = 0; i < data.length(); i ++){
      nameOfBodyPart = data.getJSONObject(i).getString("Name");
      ID = data.getJSONObject(i).getInt("ID");

      System.out.printf("%-8d %s\n", ID, nameOfBodyPart);
    }

    System.out.println();

    // request until a valid ID is given
    injuredBodyPartID = rScan.nextInt(true);

    return injuredBodyPartID;

  } 

  // Get the body part sublocation which the user is suffering pain from given a list of possible answers
  // returns an int because that's what the API uses for its calls, a number to id a body part
  public int requestHurtBodySublocation(JSONArray data){

    //declare vars
    RecursiveScanner rScan = new RecursiveScanner();

    String nameOfBodyPartSublocation = "";
    int ID = 0;
    int injuredBodyPartSublocationID = 0;

    // prompt user
    System.out.println("Type in the ID corresponding to the body part sublocation that is hurt.\n");

    // output the data in the form:
    // part_id    part_name
    System.out.printf("%-8s %s\n", "Part ID", "Part Name");
    
    for (int i = 0; i < data.length(); i ++){
      nameOfBodyPartSublocation = data.getJSONObject(i).getString("Name");
      ID = data.getJSONObject(i).getInt("ID");

      System.out.printf("%-8d %s\n", ID, nameOfBodyPartSublocation);
    }

    System.out.println("");

    // request until a valid ID is given
    injuredBodyPartSublocationID = rScan.nextInt(true);

    return injuredBodyPartSublocationID;

  } 

  // Get the body part sublocation which the user is suffering from symptoms given a list of possible answers
  // returns an ArrayList of ints because we dont know how many symptoms the patient will have
  public ArrayList<Integer> requestSymptoms(JSONArray data){

    //declare vars
    RecursiveScanner rScan = new RecursiveScanner();

    ArrayList<Integer> symptomIDs = new ArrayList<Integer>();
    String nameOfSymptom = "";
    int ID = 0;
    int symptomID = 0;

    // prompt user
    System.out.println("Type in the ID(s) corresponding to the body part sublocation that is hurt. Enter -1 when you are done.\n");

    // output the data in the form:
    // symptom_id    symptom_name
    System.out.printf("%-20s %s\n\n", "Symptom ID", "Symptom Name");

    for (int i = 0; i < data.length(); i ++){
      nameOfSymptom = data.getJSONObject(i).getString("Name");
      ID = data.getJSONObject(i).getInt("ID");

      System.out.printf("%-20d %s\n", ID, nameOfSymptom);
    }

    System.out.println();

    // request until IDs until sentinel value is given=
    while (true){
      symptomID = rScan.nextInt(true);

      // check for sentinel value
      if (symptomID == -1){
        break;
      }

      // add id to arraylist
      symptomIDs.add(symptomID);
    }

    return symptomIDs;

  } 

  // tries to get disease treaments by using tryToGetDiseaseTreatment from APIManager. May fail as all entries in tryToGetDiseaseTreatment were created by me manually :(((
  public void tryToDisplayDiseaseTreaments(JSONArray data){
    // declare vars for displaying potential disease treatments

    // variables for only displaying treatments for relevant diseases. 
    // Steps:
    //  1: check diseaseAccuracy > ignoreToleranceIfAbove. If above, display treatment
    //  2: check if tolerance - (ignoreToleranceIfAbove - diseaseAccuracy) > 0. If yes, display and subtract from tolerance.
    // Example:
    // Angina, 80% (displayed, 80>50)
    // Cold, 20% (displayed, 50-30 > 0, tolerance-=30)
    // Fever, 20% (not displayed as 20-30 < 0)
    int tolerance = 50;
    int ignoreToleranceIfAbove = 50;

    // make sure at least 1 disease treatment is outputted
    boolean needToOutputOneTreatement = true;

    // contains tryToGetDiseaseTreatment()
    APIManager API = new APIManager();

    // response from tryToGetDiseaseTreatment()
    String diseaseTreatment = "";

    String diseaseName = "";
    double diseaseAccuracy = 0;
    int diseaseID = 0;


    // loop thru the diagnosis and display treatments while checking tolerance
    for (int i = 0; i < data.length(); i ++){
      // check if tolerance will allow more disease outputs
      diseaseAccuracy = data.getJSONObject(i).getJSONObject("Issue").getDouble("Accuracy");

      // check if disease accuracy is high enough to skip the check. also check if one treatment has been outputted yet
      if (needToOutputOneTreatement || diseaseAccuracy < ignoreToleranceIfAbove){
          // check if tolerance can handle outputting more low chances of diseases
          if (tolerance - (ignoreToleranceIfAbove - diseaseAccuracy) > 0){
            tolerance -= (ignoreToleranceIfAbove - diseaseAccuracy);
          }
          else{
            return;
          }
      }

      needToOutputOneTreatement = false;

      // get neccesary data to make the call to tryToGetDiseaseTreatment()
      diseaseID = data.getJSONObject(i).getJSONObject("Issue").getInt("ID");
      diseaseName = data.getJSONObject(i).getJSONObject("Issue").getString("Name");

      diseaseTreatment = API.tryToGetDiseaseTreatment(diseaseID, diseaseName);

      // print out disease treatment
      System.out.println(diseaseTreatment);
    }

  }

  // Display the user's diagnosis in a table
  public void displayDiagnosis(JSONArray data){

    //declare vars for displaying diagnosis
    String diseaseName = "";
    // the chance that the user has an disease based on their responses
    double diseaseAccuracy = 0;

    // output the data in the form:
    // disease_name    disease_accuracy   
    System.out.printf("%-60s %11s\n\n", "Illness Name", "Accuracy");

    for (int i = 0; i < data.length(); i ++){
      diseaseName = data.getJSONObject(i).getJSONObject("Issue").getString("Name");
      diseaseAccuracy = data.getJSONObject(i).getJSONObject("Issue").getDouble("Accuracy");

      System.out.printf("%-60s %10.2f%s\n", diseaseName, diseaseAccuracy, "%");
    }

    System.out.println("\n\n");

  } 
  
  
  


}