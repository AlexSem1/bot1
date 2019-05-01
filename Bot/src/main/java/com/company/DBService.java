package com.company;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.sql.SQLException;

public class DBService {
    private final String url = "jdbc:sqlite:Bot_db.db";
    private ConnectionSource source;
    private Dao<Users, String> dao;

    public DBService() throws SQLException {
        source = new JdbcConnectionSource(url);
        dao = DaoManager.createDao(source, Users.class);
    }

    public void StartSubscribe(Message message) throws SQLException {
        Users account = new Users();
        account.setId(message.getChatId());
        account.setSubscribe(0);
        dao.create(account);
    }

    public void Subscribe(Message message) throws SQLException {
        Users account = dao.queryForId(message.getChatId().toString());
        account.setId(message.getChatId());
        account.setSubscribe(1);
        account.setLat(message.getLocation().getLatitude());
        account.setLon(message.getLocation().getLongitude());
        dao.update(account);
    }

    public boolean IsSubscribe(Message message) throws SQLException {
        Users account = dao.queryForId(message.getChatId().toString());
        if (account.getSubscribe() == 1)
            return true;
        return false;
    }

    public void DeleteSubscribe(Message message) throws SQLException {
        Users account = dao.queryForId(message.getChatId().toString());
        account.setId(message.getChatId());
        account.setSubscribe(0);
        dao.update(account);
    }

    public String SubscribeWeather(Message message) throws SQLException {
        Users account = dao.queryForId(message.getChatId().toString());
        return (account.getLat() + "&lon=" + account.getLon());
    }
}
