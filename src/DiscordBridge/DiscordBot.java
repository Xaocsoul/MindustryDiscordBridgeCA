package DiscordBridge;

import mindustry.game.EventType;
import mindustry.gen.Call;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.EventListener;
import java.util.Objects;

public class DiscordBot {
    static JDA jda;
    static String channel;

    public DiscordBot(String token, String channel1) {
        try {
            jda = JDABuilder.createDefault(token).build();
        } catch (LoginException e) {
            System.out.println("failed to login to discord");
        }
        channel = channel1;
        jda.addEventListener(new messageListener());
    }

    static class messageListener extends ListenerAdapter {
        @Override
        public void onMessageReceived(MessageReceivedEvent event){
            if(event.getChannel().getId().equals(channel) && event.getAuthor() != jda.getSelfUser()) {
                String message = event.getMessage().getContentRaw();
                if(message.length() > 150){
                    message = message.substring(0,149);
                }
                Call.sendMessage("(discord) " + event.getAuthor().getName() + ": " + message);
            }
        }
    }

    public void sendToDiscord(String message){
        Objects.requireNonNull(jda.getTextChannelById(channel)).sendMessage(message).queue();
    }
}
