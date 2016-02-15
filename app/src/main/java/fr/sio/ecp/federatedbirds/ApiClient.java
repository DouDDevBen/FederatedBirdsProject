package fr.sio.ecp.federatedbirds;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fr.sio.ecp.federatedbirds.auth.TokenManager;
import fr.sio.ecp.federatedbirds.model.Message;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by Michaël on 24/11/2015.
 */
public class ApiClient {

    private static final String API_BASE = "http://10.0.2.2:8080/";

    private String continuationToken = "aaaa";
    private String limit = "20";

    private static ApiClient mInstance;

    public static synchronized ApiClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiClient(context);
        }
        return mInstance;
    }

    private Context mContext;

    private ApiClient(Context context) {
        mContext = context.getApplicationContext();
    }

    private <T> T get(String path, Map<String, String> parameters, Type type) throws IOException {
        return method("GET", path, parameters, null, type);
    }

    private <T> T get(String path, Type type) throws IOException {
        return method("GET", path, null, null, type);
    }


    private <T> T post(String path, Object body, Type type) throws IOException {
        return method("POST", path, null, body, type);
    }

    private <T> T method(String method, String path, Map<String, String>  parameters, Object body, Type type) throws IOException {
        String url = API_BASE + path;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        String token = TokenManager.getUserToken(mContext);
        if (token != null) {
            connection.addRequestProperty("Authorization", "Bearer " + token);

            if (parameters != null) {
                connection.addRequestProperty("limit", parameters.get("limit"));
                connection.addRequestProperty("continuationToken", parameters.get("continuationToken"));
            }

        }
        if (body != null) {
            Writer writer = new OutputStreamWriter(connection.getOutputStream());
            try {
                new Gson().toJson(body, writer);
            } finally {
                writer.close();
            }
        }
        Reader reader = new InputStreamReader(connection.getInputStream());
        try {
            return new Gson().fromJson(reader, type);
        } finally {
            reader.close();
        }
    }

    public List<Message> getMessages(Long userId) throws IOException {
        TypeToken<List<Message>> type = new TypeToken<List<Message>>() {};
        String path = userId == null ? "messages" : "users/" + userId + "/messages";
        return get(path, type.getType());
    }

    public User getUser(long id) throws IOException {
        return get("users/" + id, User.class);
    }



    public List<User> getUserFollowed(Long userId) throws IOException {
        String id = userId != null ? Long.toString(userId) : "me";
        TypeToken<UsersList> type = new TypeToken<UsersList>() {};

        // Ces parametres seront ajoutés à la requette par la méthode méthod()
        // pour pousser la réutilisation de code de la connection HTTP déja implémentée.
        HashMap<String, String> parametersToAdd = new HashMap<>() ;
        parametersToAdd.put("limit", limit );
        parametersToAdd.put("continuationToken", continuationToken);

        //return get("users", type.getType());
        // List<User> list = get("users/" + id + "/followed", parametersToAdd, type.getType());
        //return list.users;
        //return get("users/" + id + "/followed", type.getType());
        //List<User> users =  (List<User>) list.get(1);

        UsersList usersList = get("users/" + id + "/followed", parametersToAdd, type.getType());
        continuationToken = usersList.cursor;
        return usersList.users;
    }


    public List<User> getUserFollowers(Long userId) throws IOException {
        String id = userId != null ? Long.toString(userId) : "me";
        TypeToken<UsersList> type = new TypeToken<UsersList>() {};

        // même chose que getUserFollowed()
        HashMap<String, String> parametersToAdd = new HashMap<>() ;
        parametersToAdd.put("limit", limit );
        parametersToAdd.put("continuationToken", continuationToken);

        UsersList usersList = get("users/" + id + "/followers", parametersToAdd, type.getType());
        continuationToken = usersList.cursor;
        return usersList.users;
    }

    public String login(String login, String password) throws IOException {
        JsonObject body = new JsonObject();
        body.addProperty("login", login);
        body.addProperty("password", password);
        return post("auth/token", body, String.class);
    }

    // méthode Post de création de compte
    public String createAccount(String login, String password, String email) throws IOException {
        JsonObject body = new JsonObject();
        body.addProperty("login", login);
        body.addProperty("email", email);
        body.addProperty("password", password);
        return post("users", body, String.class);
    }

    // méthode Post de message
    public Message postMessage(String text) throws IOException {
        Message message = new Message();
        message.text = text;
        return post("messages", message, Message.class);
    }

    // métohde post pour suivre ou de-suivre un user
    public User follow(Long id, boolean action) throws IOException {
        JsonObject body = new JsonObject();
        if (action) {
            body.addProperty("followed", "true");
        } else {
            body.addProperty("followed", "false");
        }
        return post("users/" + id.toString() , body, User.class);
    }

    // definition d'une classe static comptenant une liste de User et le contiuationToken
    // afin d'être raccord avec l'implémentation coté serveur qui renvoie en json un objet similaire.
    private static class UsersList {
        public final List<User> users;
        public final String cursor;

        public UsersList(List<User> users, String cursor) {
            this.users = users;
            this.cursor = cursor;
        }
    }

    // A la place de /avatar, le blobstore post par defaut sur /upload coté serveur
    // j'ai donc gardé cette convention ici.
    public String getUrlToPost() throws IOException {
        User u = get("localhost:8080/upload", User.class);
        // At this stage, le shield "avatar" gets the url path to use in order to save the avatar on the blob store.
        return u.avatar;

    }

    // Méthode d'upload d'avatar
    // PARA : String path : url a utiliser pour sauver l'image dans le blobstore.
    // path est conforme au patern "http://localhost:8080/_ah/upload/xxxxxxxx"
    // PARA : String img
    // ici une autre connection HTTP est utilisé (au lieu d'utiliser celle de method() pour plus de simplicité
    public String uploadAvatar(String img, String path) throws IOException {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        String pathToOurImage = img;

        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        URL url = new URL(path);
        connection = (HttpURLConnection) url.openConnection();
        FileInputStream fileInputStream = new FileInputStream(new File(pathToOurImage));

        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        connection.setRequestMethod("POST");
        String token = TokenManager.getUserToken(mContext);
        if (token != null) {
            connection.addRequestProperty("Authorization", "Bearer " + token);
        }
        //connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("ENCTYPE", "multipart/form-data");
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(twoHyphens + boundary + crlf);
        outputStream.writeBytes("Content-Disposition: form-data; name=\"avatarFile\";filename=\"" + pathToOurImage + "\"" + crlf);
        outputStream.writeBytes(crlf);

        bytesAvailable = fileInputStream.available();
        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        buffer = new byte[bufferSize];

        // Read file
        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            outputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        outputStream.writeBytes(crlf);
        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + crlf);

        String serverResponseMessage = connection.getResponseMessage();
        fileInputStream.close();
        outputStream.flush();
        outputStream.close();

        return serverResponseMessage;

    }


}
