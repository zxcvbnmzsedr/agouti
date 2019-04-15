package com.ztianzeng.agouti.core.engine;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-03-12 11:37
 */
public class MethodTest {
    public User hehe(){
        return new User("username");
    }

    public void haha(User user){
        System.out.println(user);
    }

}