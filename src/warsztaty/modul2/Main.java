package warsztaty.modul2;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        User user = new User();
        user.setEmail("ddssadfddasdddsss@o2.pl");
        user.setFirstName("Tomek");;
        user.setPassword("admin123");
        user.saveToDB();

        System.out.println(Arrays.toString(user.loadAllUsers()));
    }
}
