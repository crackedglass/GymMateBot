package ru.gymmate.gymmate_bot.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class BotConfig {

    @Bean
    public TelegramBot bot(Environment environment) {
        return new TelegramBot(environment.getProperty("TOKEN"));
    }

    @Bean
    ApplicationRunner botRunner(TelegramBot bot) {
        return args -> {

            log.info("Bot with token {} is running", bot.getToken());

            // Register for updates
            bot.setUpdatesListener(updates -> {

                for (Update update : updates) {
                    bot.execute(new SendMessage(update.message().chat().id(), update.message().text()));
                }

                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }, e -> {
                if (e.response() != null) {
                    e.response().errorCode();
                    e.response().description();
                } else {
                    e.printStackTrace();
                }
            });
        };
    }
}
