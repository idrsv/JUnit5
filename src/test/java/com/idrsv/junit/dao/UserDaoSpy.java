package com.idrsv.junit.dao;

import java.util.HashMap;
import java.util.Map;

public class UserDaoSpy extends UserDAO {


    private Map<Integer,Boolean> answers = new HashMap<>();
    //    private Answer1<Integer, Boolean> answer1;
    private UserDAO userDAO;

    public UserDaoSpy(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    @Override
    public boolean delete(Integer userId) {
        return answers.getOrDefault(userId, userDAO.delete(userId));
    }
}
