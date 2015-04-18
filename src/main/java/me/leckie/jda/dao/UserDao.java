package me.leckie.jda.dao;

import me.leckie.jda.moudle.User;

import java.util.List;

public interface UserDao {

    public long save(User user) throws Exception;

    public void update(User user) throws Exception;

    public void delete(long id) throws Exception;

    public List<User> list() throws Exception;

    public User get(long id) throws Exception;
}
