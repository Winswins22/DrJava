// Tell Java that this is a package with the name "doctor"
package doctor;

// Import necessary exceptions/classes from packages to read/write to files 
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.util.Scanner;

public class APIManager {

  // Toggle between development and production
  boolean logDebugInfo = true;

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
      if (true){
        System.out.println ("# of API calls made: " + fileNumber);
      }

      // Get all necessary items to write to the file
      FileWriter writer = new FileWriter(String.valueOf(dataDirectory));

      // Check if more calls can be made
      if (fileNumber < callLimit){
        // Can still make more calls
        // Incremement number in file by one
        writer.write(Integer.toString(fileNumber + 1));
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

  // Overload "canMakeAPICall" so that if "addCall" not specified, assume it will not cost a call, as only the "Diagnosis" call will sonsume a call
  public boolean canMakeAPICall() {
    return canMakeAPICall(false);
  }

  // Tries to get JSON data from a file. Since this data is not in the API, only some common diseases have treaments. (Since this is only a prototype though, it should be ok) 
  public String tryToGetDiseaseTreatment(String disease){

    boolean canGetTreatment = false;

    if (canGetTreatment){
      return "Your treatment is ___\n";
    }
    else {
      return "You may want to search online for \"" + disease + "\"\n";   
    }

  }

  

}