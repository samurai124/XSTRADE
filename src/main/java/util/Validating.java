package util;

import model.Identifiable;

import java.util.ArrayList;
import java.util.Scanner;

public class Validating {
    public static Scanner input = new Scanner(System.in);



    // function to validate ints
    public static int validateInts(String fieldName){
        int validatedInt = 0;
        do {
            System.out.print("Enter your "+fieldName+" : ");
            try {
                validatedInt = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ pleas enter a valid "+ fieldName);
                validatedInt = 0;
            }
        } while (validatedInt == 0);
        return validatedInt;
    }

    // function to validate float
    public static float validateFloats(String fieldName){
        float validatedFloat = 0;
        do {
            System.out.print("Enter your "+fieldName+" : ");
            try {
                validatedFloat = Float.parseFloat(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ pleas enter a valid "+ fieldName);
                validatedFloat = 0;
            }
        } while (validatedFloat == 0);
        return validatedFloat;
    }



    // function to validate strings
    public static String validateString(String fieldName){
        String validatedString = "";
        do {
            System.out.print("Enter your "+fieldName+" : ");
            validatedString = input.nextLine();
            if (validatedString.isEmpty()) {
                System.out.println("Please enter a valid"+ fieldName);
            }
        } while (validatedString.isEmpty());
        return  validatedString;
    }

    // function to validate two Strin choices
    public static String validateTwoChoice(String choice1, String choice2) {
        String inputChoice = "";
        do {
            System.out.printf("Enter your choice (%s/%s): ", choice1, choice2);
            inputChoice = input.nextLine().trim();

            if (!inputChoice.equalsIgnoreCase(choice1) && !inputChoice.equalsIgnoreCase(choice2)) {
                System.out.println("❌ Invalid choice. Please enter either '" + choice1 + "' or '" + choice2 + "'.");
                inputChoice = "";
            }
        } while (inputChoice.isEmpty());

        return inputChoice;
    }



    // function to get an element from an array by its id
    public static  <T extends Identifiable> T findById(ArrayList<T> list, int id) {
        T element =list.stream().filter(t->t.getId() == id).findAny().orElse(null);
        return element;
    }


    //


}
