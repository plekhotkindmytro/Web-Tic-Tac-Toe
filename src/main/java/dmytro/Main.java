package dmytro;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;

import model.Input;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.gson.Gson;

public class Main {

    // private Set<Game> games = new HashSet<Game>();
    private static XO game;

    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        staticFileLocation("/public"); // Static files

        get("/", (req, res) -> {
            game = new XO();
            Map<String, Object> attributes = new HashMap<>();
            String sessionId = req.session().id();
            return new ModelAndView(attributes, "index.html");
        }, new FreeMarkerEngine());

        Gson gson = new Gson();

        post("/deck", (req, res) -> {
            return game.getDeck();
        }, gson::toJson);

        post("/user-turn", (req, res) -> {
            String userInputString = req.body();
            Input input = gson.fromJson(userInputString, Input.class);

            return game.makeTurn(input.getValue());
        }, gson::toJson);

    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; // return default port if heroku-port isn't set (i.e. on
                     // localhost)
    }
}