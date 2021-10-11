// Tell Java that this is a package with the name "recursive"
package recursive;

// Import the Scanner class to use in the RecursiveScanner class, as we need it to take in input from the user
import java.util.Scanner;

public class RecursiveScanner {

  // Initialize the RecursiveScanner class
  public RecursiveScanner(){
  }
  
  // Java's Scanner.nextInt(), but with user error handling 
  public int nextInt(boolean log, String onErrMessage) {
    // Create a Scanner class to take in input from the user until the user enters an int 
    Scanner scan =  new Scanner (System.in);
    // Create a variable to store the user's input
    int userInt;

    // Use a try-catch block to handle the times when the user doesn't enter an int
    try {
      // Try to get the user's input
      userInt = scan.nextInt();
      // Return the answer
      return userInt;
    }
    catch(Exception e){
      // User did not enter a int
      if (log){
        System.out.println(onErrMessage);
      }
      // Call another instance of nextInt to get the int.
      return nextInt(log, onErrMessage);
    }

  }

  // Use overloading to allow default parameters
  public int nextInt(boolean log) {
    return nextInt(log, "Oops! Try again.\n");
  }

  // Use overloading to allow default parameters
  public int nextInt() {
    return nextInt(false, "");
  }
  
  // Java's Scanner.nextDouble(), but with user error handling 
  public double nextDouble(boolean log, String onErrMessage) {
    // Create a Scanner class to take in input from the user until the user enters a double 
    Scanner scan =  new Scanner (System.in);
    // Create a variable to store the user's input
    double userDouble;

    // Use a try-catch block to handle the times when the user doesn't enter an double
    try {
      // Try to get the user's input
      userDouble = scan.nextDouble();
      // Return the answer
      return userDouble;
    }
    catch(Exception e){
      // User did not enter a double
      if (log){
        System.out.println(onErrMessage);
      }
      // Call another instance of nextDouble to get the double.
      return nextDouble();
    }

  }

  // Use overloading to allow default parameters
  public double nextDouble(boolean log) {
    return nextDouble(log, "Oops! Try again.");
  }

  // Use overloading to allow default parameters
  public double nextDouble() {
    return nextDouble(false, "");
  }

}