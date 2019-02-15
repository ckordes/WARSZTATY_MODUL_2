package warsztaty.modul2;

public class Main {

    public static void main(String[] args) {
        Users user = new Users();
        Exercise exercise = new Exercise();
        UserGroup userGroup = new UserGroup();

//        user.setEmail("6@o2.pl");
//        user.setFirstName("Tomek");
//        user.setPassword("admin123");
//        user.setUser_group_id(1);
//        user.saveToDB();

//        System.out.println(Arrays.toString(user.loadAllUsers()));


//        --------------------------------------------------------------------------
//        testing methods
//        user.runUsers();
//        exercise.runExercise();
        userGroup.runUserGroups();
    }
}
