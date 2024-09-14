package dev.tonimatas.systembot;

import dev.tonimatas.systembot.events.SlashCommands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class SystemBot {
    public static JDA jda;
    public static Logger logger = LoggerFactory.getLogger(SystemBot.class);
    
    public static void main(String[] args) {
        File file = new File("token.txt");

        if (!file.exists()) {
            logger.warn("Token file not exist. Creating it...");
            
            try {
                if (!file.createNewFile()) throw new RuntimeException("Token file already exists");
            } catch (IOException e) {
                logger.error("Error creating the token file. Please create it manually.", e);
                throw new RuntimeException(e);
            }
            
            logger.warn("In the first line of the token.txt add the token of your discord bot.");
            return;   
        }

        if (!file.canRead()) {
            logger.error("Can't read the token file.");
            return;
        }
        
        String token;
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            
            token = reader.readLine();
            
            reader.close();
        } catch (Exception e) {
            logger.error("Error creating the scanner to read the token file. Check it.");
            throw new RuntimeException(e);
        }

        if (token.isBlank() || token.isEmpty()) {
            logger.error("Invalid or null token. Check it.");
            return;
        }

        JDABuilder builder = JDABuilder.createDefault(token);
        
        builder.addEventListeners(new SlashCommands());
        builder.enableIntents(List.of(GatewayIntent.values()));
        

        try {
            jda = builder.build().awaitReady();
        } catch (InterruptedException e) {
            logger.error("Error starting the bot.");
            throw new RuntimeException(e);
        }

        CommandListUpdateAction commands = jda.updateCommands();
        commands = commands.addCommands(Commands.slash("ping", "See the ping to the bot."));
        commands.queue();
    }
}