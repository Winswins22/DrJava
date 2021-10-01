/* 
File Name: DrJava 
By: Winston Ho
Date Updated: *see repl history*
Version: Alpha 0.2.1a
Assignment: My Programming Challenge #1

Description: 

Sources:
https://www.youtube.com/watch?v=lHFlAYaNfdo


*/

/*
Steps:
- Ask the user their age and gender
- Ask the user which body part hurts from a list of parts (gotten from the API)
- Ask the user what symptoms they have (gotten from the API)
- Make a call to the API to retrieve the disease
- Give the user the diagnosis (requires age, gender, symptoms)
*/

// Import custom class "APIManager" from custom package "doctor"
import doctor.APIManager;

// Import custom class "RecursiveScanner" from custom package "recursive"
import recursive.RecursiveScanner;

class Main {
  public static void main(String[] args) {
    APIManager API = new APIManager();
    //System.out.println(API.test());
    System.out.println(API.canMakeAPICall(true));
  }
}