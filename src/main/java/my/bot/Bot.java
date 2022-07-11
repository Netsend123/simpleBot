package my.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {

    public int state = 0;
    public String measure;
    public String material;
    public String phase;
    public String typeOfSet;
    public Double iw;

    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().hasText() & update.getMessage().getText().equals("Hello")) {
                state = 1;
                try {
                    execute(sendInlineKeyBoardMessageMeasure(update.getMessage().getChatId(), "kVt", "Amper", "Выберете единицы измерения"));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            } else {
                state = 6;
                iw = Double.valueOf(update.getMessage().getText());
                SendMessage resout = new SendMessage();
                resout.setChatId(String.valueOf(update.getMessage().getChatId()));
                resout.setText(measure + " " + material + " " + " " + typeOfSet + " " + phase + " " + "результат - " + counter(iw));
                try {
                    execute(resout);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        } else if (update.hasCallbackQuery() & state == 1) {
            measure = update.getCallbackQuery().getData();
            state = 2;
            try {
                execute(sendInlineKeyBoardMessageMeasure(update.getCallbackQuery().getMessage().getChatId(), "    1    ", "    3    ", "Выберете количество фаз"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery() & state == 2) {
            phase = update.getCallbackQuery().getData();
            state = 3;
            try {
                execute(sendInlineKeyBoardMessageMeasure(update.getCallbackQuery().getMessage().getChatId(), "     Cu     ", "     Al     ", "Выберете материал"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery() & state == 3) {
            material = update.getCallbackQuery().getData();
            state = 4;
            try {
                execute(sendInlineKeyBoardMessageMeasure(update.getCallbackQuery().getMessage().getChatId(), "KabelKanal", "Shtukaturka", "Выберете тип монтажа"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery() & state == 4) {
            typeOfSet = update.getCallbackQuery().getData();
            state = 5;
            SendMessage sm = new SendMessage();
            sm.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
            sm.setText("Введите значение тока или мощности");
            System.out.println(state);
            try {
                execute(sm);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    public SendMessage sendInlineKeyBoardMessageMeasure(long chatId, String v1, String v2, String txt) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText(v1);
        inlineKeyboardButton1.setCallbackData(v1);
        inlineKeyboardButton2.setText(v2);
        inlineKeyboardButton2.setCallbackData(v2);
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        SendMessage sm = new SendMessage();
        sm.setChatId(String.valueOf(chatId));
        sm.setText(txt);
        sm.setReplyMarkup(inlineKeyboardMarkup);
        return sm;
    }

    public String counter(Double iw) {
        Double i;
        if (measure.equals("kVt")) {
            i = iw * 220;
        } else {
            i = iw;
        }
        return String.valueOf(i);
    }

    public String getBotUsername() {
        return "Calendarmybot";
    }

    public String getBotToken() {
        return "5591926587:AAGkCF98mhP1OywllGY4GRzPf78AYrePE-k";
    }
}

