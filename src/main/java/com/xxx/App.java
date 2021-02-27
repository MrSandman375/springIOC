package com.xxx;

import com.xxx.dao.UserDao;
import com.xxx.service.UserService;
import org.dom4j.DocumentException;

public class App {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, DocumentException, IllegalAccessException {
        //得到工厂的实现对象
        MyClassPathXmlApplicationContext pathXmlApplicationContext = new MyClassPathXmlApplicationContext("spring.xml");
        //得到对应的实例化对象
        UserDao userDao = (UserDao) pathXmlApplicationContext.getBean("userDao");
        UserService userService = (UserService) pathXmlApplicationContext.getBean("userService");
        //执行
        userDao.test();
        userService.test();

        UserDao userDao02 = (UserDao) pathXmlApplicationContext.getBean("userDao");
        userDao02.test();
        System.out.println(userDao == userDao02);
    }
}
