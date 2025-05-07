// model/UserManager.java
package model;

import java.io.*;
import java.util.*;

public class UserManager {
    private static final String DATA_FILE = "users.dat";
    private Map<String, User> users = new HashMap<>();

    public UserManager() { loadUsers(); }

    private void loadUsers() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            users = (Map<String, User>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            users = new HashMap<>();
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRegistered(String name) {
        return users.containsKey(name);
    }

    public boolean register(String name, String pwd) {
        if (isRegistered(name)) return false;
        users.put(name, new User(name, hash(pwd)));
        saveUsers();
        return true;
    }

    public boolean login(String name, String pwd) {
        User u = users.get(name);
        return u != null && u.getPasswordHash().equals(hash(pwd));
    }

    private String hash(String pwd) {
        // 简单 MD5 或 SHA-256
        return Integer.toHexString(pwd.hashCode());
    }
}

