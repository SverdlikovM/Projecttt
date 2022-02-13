package edu.sumdu.tss.elephant.controller;

import java.io.*;
import java.net.HttpURLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.sumdu.tss.elephant.Helper;
import edu.sumdu.tss.elephant.Server;
import edu.sumdu.tss.elephant.model.*;
import edu.sumdu.tss.elephant.helper.Hmac;
import edu.sumdu.tss.elephant.helper.Keys;
import edu.sumdu.tss.elephant.helper.UserRole;
import edu.sumdu.tss.elephant.helper.utils.StringUtils;

public class ApiControllerTest {

    static User user;
    static Server server;
    static Database DB;

    @BeforeAll
    static void init() {
        //User
        Keys.loadParams(new File(Helper.configFile));
        user = UserService.newDefaultUser();
        user.setLogin(Helper.genEmail());
        user.setPassword(Helper.genPassword());
        user.setRole(UserRole.BASIC_USER.getValue());
        UserService.save(user);
        UserService.initUserStorage(user.getUsername());
        DbUserService.initUser(user.getUsername(), user.getPassword());

        //DB
        String dbName = StringUtils.randomAlphaString(6);
        DB = new Database();
        DB.setName(dbName);
        DB.setOwner(user.getUsername());
        DatabaseService.create(dbName, user.getUsername(), user.getUsername());

        //Server
        server = new Server();
        server.start(Integer.parseInt(Keys.get("APP.PORT")));
    }

    int sendRequest(String path) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        URL url = new URL(Keys.get("APP.URL") + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("publickey", user.getPublicKey());
        connection.setRequestProperty("signature", Hmac.calculate(path, user.getPrivateKey()));

        int status = connection.getResponseCode();
        System.out.println("\nStatus code: " + status);
        getResponse(connection);

        connection.disconnect();
        return status;
    }

    void getResponse(HttpURLConnection connection) throws IOException {
        InputStream input = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder response = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }

        reader.close();
        System.out.println("Response: " + response + "\n");
    }

    @Test
    void createBackupSuccess() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        final int expectedStatus = 204;

        String backup = StringUtils.randomAlphaString(6);
        String url = "/api/v1/database/" + DB.getName() + "/create/" + backup;

        int status = sendRequest(url);
        try {
            BackupService.delete(user.getUsername(), DB.getName(), backup);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }

        assertEquals(expectedStatus, status);
    }

    @Test
    void createBackupError_forbidden() throws IOException {
        final int expectedStatus = 403;

        String backup = StringUtils.randomAlphaString(6);
        String backupPath = "/api/v1/database/" + DB.getName() + "/create/" + backup;
        URL url = new URL(Keys.get("APP.URL") + backupPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("publickey", user.getPublicKey());
        connection.setRequestProperty("signature", user.getPrivateKey());

        int status = connection.getResponseCode();
        System.out.println("\nStatus code: " + status);
        getResponse(connection);
        connection.disconnect();

        assertEquals(expectedStatus, status);
    }

    @Test
    void createBackupError_notFound() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        final int expectedStatus = 404;

        String backup = StringUtils.randomAlphaString(6);
        String dbName = StringUtils.randomAlphaString(6);
        String path = "/api/v1/database/" + dbName + "/create/" + backup;

        int status = sendRequest(path);
        assertEquals(expectedStatus, status);
    }

    @Test
    void updateBackupSuccess() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        final int expectedStatus = 204;

        String backup = StringUtils.randomAlphaString(6);
        BackupService.perform(DB.getOwner(), DB.getName(), backup);
        String url = "/api/v1/database/" + DB.getName() + "/create/" + backup;

        int status = sendRequest(url);
        try {
            BackupService.delete(user.getUsername(), DB.getName(), backup);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }
        assertEquals(expectedStatus, status);
    }

    @Test
    void resetDatabaseSuccess() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        final int expectedStatus = 204;

        String backupName = StringUtils.randomAlphaString(6);
        BackupService.perform(DB.getOwner(), DB.getName(), backupName);
        String url = "/api/v1/database/" + DB.getName() + "/reset/" + backupName;

        int status = sendRequest(url);
        try {
            BackupService.delete(user.getUsername(), DB.getName(), backupName);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }
        assertEquals(expectedStatus, status);
    }

    @Test
    void resetDatabaseError_forbidden() throws IOException {
        final int expectedStatus = 403;

        String backup = StringUtils.randomAlphaString(6);
        BackupService.perform(DB.getOwner(), DB.getName(), backup);
        String backupName = "/api/v1/database/" + DB.getName() + "/reset/" + backup;
        URL url = new URL(Keys.get("APP.URL") + backupName);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("publickey", user.getPublicKey());
        connection.setRequestProperty("signature", user.getPrivateKey());

        int status = connection.getResponseCode();
        System.out.println("\nStatus code: " + status);
        getResponse(connection);
        connection.disconnect();

        try {
            BackupService.delete(user.getUsername(), DB.getName(), backupName);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }
        assertEquals(expectedStatus, status);
    }

    @Test
    void resetDatabaseError_notFound() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        final int expectedStatus = 404;

        String backup = StringUtils.randomAlphaString(6);
        BackupService.perform(DB.getOwner(), DB.getName(), backup);
        String dbName = StringUtils.randomAlphaString(6);
        String url = "/api/v1/database/" + dbName + "/reset/" + backup;

        int status = sendRequest(url);
        assertEquals(expectedStatus, status);
    }

    @AfterAll
    static void delete() {
        Helper.dropDB(DB);
        Helper.dropTablespace(user.getUsername());
        Helper.dropUser(user.getUsername());
        Helper.deleteUser(user.getLogin());
        server.stop();
    }
}