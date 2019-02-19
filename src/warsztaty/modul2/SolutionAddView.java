package warsztaty.modul2;

import java.util.Scanner;

public class SolutionAddView {

    public static void main(String[] args) {
        Solution solution = new Solution();
        Exercise exercise = new Exercise();
        Exercise[] exercisesArray = exercise.loadAllExercisesByUserWithoutSolution(Integer.valueOf(args[0]));
        Solution[] solutionArray = solution.loadAllByUserId(Integer.valueOf(args[0]));
        String answer;
        int answerInt = 0;

        System.out.println("Wybierz jedną z opcji: ");
        System.out.println("    add – dodawanie rozwiązania,");
        System.out.println("    view – przeglądanie swoich rozwiązań.");
        answer = scanInput();
        if (answer.equals("add")) {
            boolean bCheck = false;
            for (int i = 0; i < exercisesArray.length; i++) {
                System.out.println("ID zadania: " + exercisesArray[i].getId() +
                        ", Tytul zadania: " + exercisesArray[i].getTitle() +
                        ", opis zadania: " + exercisesArray[i].getDescription());
            }
            if (exercisesArray.length == 0) {
                System.out.println("Uzytkownik uzupelnil wszystkie rozwiazania!");
            } else {
                System.out.println("Podaj ID zadania, do ktorego chcesz wprowadzic rozwiazanie: ");
                do {
                    try {
                        bCheck = false;
                        answerInt = Integer.parseInt(scanInput());
                        for (int i = 0; i < exercisesArray.length; i++) {
                            int arrayInt = exercisesArray[i].getId();
                            if (answerInt != arrayInt) {
                                bCheck = true;
                                System.out.println("Podane ID jest niewlasciwe! Podaj ponownie!");
                            } else {
                                bCheck = false;
                                break;
                            }
                        }
                    } catch (NumberFormatException e) {
//                    e.printStackTrace();
                        System.out.println("wrong data type");
                        bCheck = true;
                    }
                } while (bCheck == true);

                System.out.println("Prosze podac rozwiazanie: ");
                answer = scanInput();
                Solution solToBeSaved = new Solution();
                solToBeSaved.setDescription(answer);
                solToBeSaved.setExercise_id(answerInt);
                solToBeSaved.setUsers_id(Integer.parseInt(args[0]));
                solToBeSaved.saveToDB();
            }
        } else if (answer.equals("view")) {
            for (int i = 0; i < solutionArray.length; i++) {
                System.out.println("ID zadania: " + solutionArray[i].getExercise_id() +
                        ", opis zadania: " + solutionArray[i].getDescription());
            }
        } else if (answer.equals("quit")) {
        } else {
            System.out.println("Bledny wybor!");
        }
    }

    // method that will take input from console
    public static String scanInput() {
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }
}
/*
"Wybierz jedną z opcji:

    add – dodawanie rozwiązania,
    view – przeglądanie swoich rozwiązań."

 */