package DiscordBridge;

import arc.*;
import arc.files.Fi;
import arc.util.*;
import mindustry.Vars;
import mindustry.game.EventType.*;
import mindustry.maps.Map;
import mindustry.mod.*;
import mindustry.net.WorldReloader;

import java.io.*;
import java.util.EventListener;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class DiscordPlugin extends Plugin{


    String token;
    String channel;
    //called when game initializes
    @Override
    public void init(){
        File config = new File(System.getProperty("user.dir"),"discord.json");

        //create config file if it doesn't exist
        if(!config.exists()){
            JSONObject data = new JSONObject();
            data.put("token", "<put token here>");
            data.put("channel_id", "<put discord channel id here>");

            try {
                config.createNewFile();
                FileWriter writer = new FileWriter(config);
                data.writeJSONString(writer);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.log(Log.LogLevel.info,"Created discord config file, please fill it in and restart the server in order to use the discord bridge");
        } else {
            try {
                JSONParser parser = new JSONParser();
                JSONObject data = (JSONObject) parser.parse(new FileReader(config));
                token = (String) data.get("token");
                channel = (String) data.get("channel_id");

                DiscordBot bot = new DiscordBot(token,channel);

                Events.on(PlayerChatEvent.class, event -> {
                    if(!event.message.startsWith("/")){
                        bot.sendToDiscord(event.player.name + ": " + event.message);
                    }
                });

                Events.on(PlayerJoin.class, event -> {
                    bot.sendToDiscord(event.player.name + " has connected");
                });

                Events.on(PlayerLeave.class, event -> {
                    bot.sendToDiscord(event.player.name + " has disconnected");
                });
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    //register commands that run on the server
    @Override
    public void registerServerCommands(CommandHandler handler){

    }

    //register commands that player can invoke in-game
    @Override
    public void registerClientCommands(CommandHandler handler){

    }
}
