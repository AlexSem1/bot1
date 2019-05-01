package com.company;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    private static String PROXY_HOST = "80.211.12.190";
    private static Integer PROXY_PORT = 1080;
    private static String PROXY_USER = "free";
    private static String PROXY_PASSWORD = "free";

    protected Bot(DefaultBotOptions botOptions) {
        super(botOptions);
    }

    public static void main(String[] args) throws SQLException {
        // write your code here

        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(PROXY_USER, PROXY_PASSWORD.toCharArray());
            }
        });

        ApiContextInitializer.init();

        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

        botOptions.setProxyHost(PROXY_HOST);
        botOptions.setProxyPort(PROXY_PORT);
        botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot(botOptions));
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        KeyboardButton button = new KeyboardButton("Subscribe");
        keyboardFirstRow.add(button.setRequestLocation(true));
        KeyboardButton button1 = new KeyboardButton("Get Location Weather");
        keyboardFirstRow.add(button1.setRequestLocation(true));
        keyboardSecondRow.add(new KeyboardButton("Delete Subscribe"));
        keyboardSecondRow.add(new KeyboardButton("Get Subscribe Weather"));
        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    @Override
    public String getBotToken() {
        return "854434826:AAHuPI7t66hBJlekKtkLRssg7V0gJ47fGM0";
    }

    @Override
    public void onUpdateReceived(Update update) {
        //Model model = new Model();
        Model1 model = new Model1();
        Message message = update.getMessage();
        if (message.hasLocation()) {
            try {
                DBService dbService = new DBService();
                if (!dbService.IsSubscribe(message)) {
                    dbService.Subscribe(message);
                    sendMsg(message, "Вы подписались на погоду!!");
                }
                sendMsg(message, Weather.getWeather(message, model));
            } catch (IOException e) {
                sendMsg(message, "Такой город не найден!!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (message != null && message.hasText()) {
            switch (message.getText()) {
                case ("/start"):
                    sendMsg(message, "Ну, допустим, привет!!");
                    try {
                        DBService dbService = new DBService();
                        dbService.StartSubscribe(message);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case ("/help"):
                    sendMsg(message, "Ты думал тебе тут помогут?!!");
                    break;
                case ("/settings"):
                    sendMsg(message, "Нехрен настраивать пользуйся чем дают!!");
                    break;
                case ("Delete Subscribe"):
                    try {
                        DBService dbService = new DBService();
                        dbService.DeleteSubscribe(message);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    sendMsg(message, "Подписка удалена.");
                    break;
                case ("Get Subscribe Weather"):
                    try {
                        DBService dbService = new DBService();
                        sendMsg(message, Weather.getSubWeather(dbService.SubscribeWeather(message), model));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;

                default:
                    try {
                        sendMsg(message, Weather.getWeather(message, model));
                    } catch (IOException e) {
                        sendMsg(message, "Такой город не найден!!");
                    }

            }
        }
    }

    private void sendMsg(Message message, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(s);
        setButtons(sendMessage);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        return "NastyaWeatherBot";
    }


}
