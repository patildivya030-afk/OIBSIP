import java.util.HashMap;
import java.util.Map;

public class UserStore {

    private final Map<String, User> users = new HashMap<>();

    public UserStore() {
        users.put("student", new User("student", "pass123", "Student"));
    }

    public boolean usernameTaken(String username) {
        return users.containsKey(username);
    }

    public boolean register(String username, String password, String displayName) {
        if (usernameTaken(username)) {
            return false;
        }
        users.put(username, new User(username, password, displayName));
        return true;
    }

    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.checkPassword(password)) {
            return user;
        }
        return null;
    }
}