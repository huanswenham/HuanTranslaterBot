package translatorBot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
  public static void main(String[] args) throws TelegramApiException {
    TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
    Bot translatorBot = new Bot();

    botsApi.registerBot(translatorBot);
    System.out.println(translatorBot.getBotUsername() + " is ready");
  }
}
