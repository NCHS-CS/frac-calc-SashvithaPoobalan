// Sashvitha Poobalan
// Period 6
// Fraction Calculator Project

import java.util.*;

//Description: This program takes the String input from the user and prints it out in the console. If the user types in random letters other than test 1 it will print the user's input again.
// If the user types in "quit" the program loop will end and the console will print "Goodbye!"1=
public class FracCalc {

   // It is best if we have only one console object for input
   public static Scanner console = new Scanner(System.in);
   
   // This main method well loop through user input and then call the
   // correct method to execute the user's request for help, test, or
   // the mathematical operation on fractions. or, quit.
   // DO NOT CHANGE THIS METHOD!!
   public static void main(String[] args) {
   
      // initialize to false so that we start our loop
      boolean done = false;
      
      // When the user types in "quit", we are done.
      while (!done) {
         // prompt the user for input
         String input = getInput();
         
         // special case the "quit" command
         if (input.equalsIgnoreCase("quit")) {
            done = true;
         } 
         else if (!UnitTestRunner.processCommand(input, FracCalc::processCommand)) {
        	   // We allowed the UnitTestRunner to handle the command first.
            // If the UnitTestRunner didn't handled the command, process normally.
            String result = processCommand(input);
            
            // print the result of processing the command
            System.out.println(result);
         }
      }
      
      System.out.println("Goodbye!");
      console.close();
   }

   // Prompt the user with a simple, "Enter: " and get the line of input.
   // Return the full line that the user typed in.
   public static String getInput() {
      //Asks the user for a String and returns the input value
      System.out.println("Enter: ");
      String input = console.nextLine();
      return input;
   }
   
   // processCommand will process every user command except for "quit".
   // It will return the String that should be printed to the console.
   // This method won't print anything.
   // DO NOT CHANGE THIS METHOD!!!
   public static String processCommand(String input) {

      if (input.equalsIgnoreCase("help")) {
         return provideHelp();
      }
      
      // if the command is not "help", it should be an expression.
      // Of course, this is only if the user is being nice.
      return processExpression(input);
   }

   public static String processExpression(String input) {
      
      //these parts use the help method getOperator and getSecondNum to find the operator and the second number
      char operator= getOperator(input);
      String num2 = getSecondNum(input, operator);

      //finds the first number of input (the number that comes before the operator)
      int index1 = input.indexOf(" " + operator + " ");
      String num1 = input.substring (0, index1);
  
      //All of this converts the first number into an improper fraction
      //We are converting the mixed numbers into an improper fraction because it is easier to caluclate that way
      int whole1 = getWholeNum(num1);
      int numer1= getNumerator(num1);
      int denom1 = getDenominator(num1);
      int imp1 = calculateImpNumer(whole1, numer1, denom1);

      //Converts the second number into an improper fraction
      int whole = getWholeNum(num2);
      int numer = getNumerator(num2);
      int denom = getDenominator(num2);
      int imp2 = calculateImpNumer(whole, numer, denom);
      
      //Prevents the error from showing if user is trying to divide by 0. It instead gives a preset error message.
      if (denom1 ==0 || denom==0 ){
            return "Error: Division by zero \n       You can't divide a number by zero \n       Please try entering a different expression";
         }
 
      // The reult number of the numerator and denominator is automatically set to 0 and incremented later in the program
      int rNumer = 0;
      int rDenom = 0;

      //Uses the improper fraction coverts it into common denominators and does math
      //Idententifies what operator is being used and evaluates the user's input accordingly 
      if (operator == '+'){
         rNumer = imp1 * denom + imp2 * denom1;
         rDenom = denom1 * denom;
      }else if (operator == '-'){
         rNumer = imp1 * denom - imp2 * denom1;
         rDenom = denom1 * denom;
      }else if (operator == '*'){
         rNumer = imp1 *imp2;
         rDenom = denom1 * denom;
      }else if (operator =='/'){
         rNumer = imp1 * denom;
         rDenom = imp2 * denom1;
      }

      //finds the gcd of the improper fractions and reduces the fraction
      //The math.abs takes in the absolute value of both numerator and denominator and finds the greatest common denominator
      int gcd = gcd(Math.abs(rNumer), Math.abs(rDenom));
      rNumer /= gcd;
      rDenom/= gcd;

      //Normalize the negative denominator
      if(rDenom <0){
         rNumer *= -1;
         rDenom *= -1;
      }

      //this converts everything back to a mixed number
      int rWhole = rNumer / rDenom;
      int rFrac = Math.abs(rNumer % rDenom);

      //if the number is equal to 0 hen turn whole into a string
      if (rFrac == 0){
         return Integer.toString(rWhole);

      } else if (rWhole == 0) {
         if(rNumer < 0) {
            return "-" + rFrac + "/" + rDenom;
         }else{
            return rFrac + "/" + rDenom;
         }
      } else {
         if (rWhole < 0 && rNumer < 0){
            return rWhole + " " + rFrac + "/" + rDenom;
         }else {
            return rWhole + " " + rFrac + "/" + rDenom;
         }
      }
   }
   //the parameter whole gets the whole number part fo the fraction
   //numer gets the numerator
   //denom gets the denominator
   //return returns the imorob=per ge=raction numerator

   //Converts the mixed number to an improper fraction numerator
   public static int calculateImpNumer (int whole, int numer, int denom){
      int imp = Math.abs(whole) * denom + numer;
      if (whole < 0){
      imp = -imp;
      }
      return imp;
   }

   //Returns the greatest common divisor
   //Euclidean algorithm https://en.wikipedia.org/wiki/Euclidean_algorithm
   public static int gcd(int a, int b){
      a = Math.abs(a);
      b= Math.abs(b);

      while (b!=0){
         int temp =b;
         b = a%b;
         a =temp;
      }
      return a;
   }
   //Checks which operator the input contains and returns that operator (for CP2)
   //This operator is gonna be used to perform the math itself later
   public static char getOperator(String input){
      if (input.indexOf(" + ") !=-1){
         return '+';
      } else if (input.indexOf(" - ") !=-1){
         return '-';
      }else if (input.indexOf(" * ") !=-1){
         return '*';
      }else {
         return '/';
      }
   }

   //gets the second number of the users input (numbers after operator)
   public static String getSecondNum(String input, char operator){
      int index = input.indexOf(" " + operator + " ");
      String num = input.substring(index + 3);
      return num;
   }
   //returns the whole number based off of the user input
   public static int getWholeNum(String getSecondNum){

      //checks if the second number or the numbers after the output is a mixed numebr and returns only the whole number
      if (getSecondNum.contains("_")){
         int i  = getSecondNum.indexOf("_");
         //the integer.parseInt converst the string value into an int because the method is supposed to return an int
         int whole = Integer.parseInt(getSecondNum.substring(0,i));
         return whole;
      } else if(getSecondNum.contains("/")){
         return 0;
      }else{
         return Integer.parseInt(getSecondNum);
      }
   }

    //Returns the numerator part of the number from input
    public static int getNumerator(String getSecondNum){

      if (getSecondNum.contains("_")){
         //gets the index of where the space is
         int j = getSecondNum.indexOf("_");
         //finds index of the slash or dividing sign
         int k = getSecondNum.indexOf("/");
         //gets the number between the space and slash
         //the +1 is to make sure to get the number AFTER the space and not including the space
         String l = getSecondNum.substring(j+1, k);
         //this part converst the string into an int
         int numer = Integer.parseInt(l);
         return numer;
      } else if (getSecondNum.contains("/"))
         {
         //this part gets the number that comes before the slash mark
         int k = getSecondNum.indexOf("/");
         String l = getSecondNum.substring(0,k);
         //converts string to an int
         int numer = Integer.parseInt(l);
         return numer;
      } else
         {
         //if none of the oconditions apple, there is no numerator meanignt here is not fraction number entered
         return 0;
      }
    }

    //Returns the denominator part of the number from input
    public static int getDenominator(String getSecondNum){
      if (getSecondNum.contains("_")){
         int s = getSecondNum.indexOf("_");
         String frac = getSecondNum.substring(s + 1);
         int d = frac.indexOf("/");
         String deno = frac.substring(d + 1);
         int denom = Integer.parseInt(deno);
         return denom;

      } else if (getSecondNum.contains("/")){
         int d = getSecondNum.indexOf("/");
         String deno = getSecondNum.substring(d + 1);
         int denom = Integer.parseInt(deno);
         return denom;
      } else
       {
         return 1;
       }

    }
   // Returns a string that is helpful to the user about how
   // to use the program. These are instructions to the user.
   public static String provideHelp() {
     
      String help = " \n";
      help += "Welcome to my Fraction Calculator! \n I see that you are either running into an error or need help running the program. The rules to my program is simple and as followed below. \n 1) Make sure you are entering a space between the operators of your expression";
      help += "Ex: \n       1_+_2 //See the _ (underscore) that's the space \n";
      help += " 2) Check for any typos in your expressions \n";
      help += " 3) Make sure not to enter any expressions that involes dividing by 0\n";
      help += " Type quit to exit the program!\n";
      help += " Enjoy!";
      
      return help;
   }
}
