package playerlog.playerlog;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Playerlog extends JavaPlugin implements Listener {

    private File chatLogFile;
    private File commandLogFile;

    @Override
    public void onEnable() {
        chatLogFile = new File(getDataFolder(), "chat_log.txt");
        commandLogFile = new File(getDataFolder(), "command_log.txt");

        try {
            if (!chatLogFile.exists()) {
                chatLogFile.getParentFile().mkdirs();
                chatLogFile.createNewFile();
            }
            if (!commandLogFile.exists()) {
                commandLogFile.getParentFile().mkdirs();
                commandLogFile.createNewFile();
            }
        } catch (IOException e) {
            getLogger().severe("Не удалось создать файл для логов!");
            e.printStackTrace();
        }

        // Регистрация событий
        getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String playerName = event.getPlayer().getName();
        String message = event.getMessage();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy[HH:mm:ss]"));

        String logMessage = String.format("%s%s:%s", timestamp, playerName, message);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(chatLogFile, true))) {
            writer.write(logMessage);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String playerName = event.getPlayer().getName();
        String command = event.getMessage();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy[HH:mm:ss]"));

        String logMessage = String.format("%s%s:%s", timestamp, playerName, command);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(commandLogFile, true))) {
            writer.write(logMessage);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
