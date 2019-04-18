/*
 * Copyright 2018-2019 zTianzeng Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ztianzeng.agouti.test.controller;

import com.ztianzeng.agouti.test.vo.UserInfoVO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-04-17 20:46
 */
@RestController
@RequestMapping("/origin")
public class OriginalController {

    private static Map<Integer, UserInfoVO> userList = new HashMap<>();

    static {
        userList.put(1, UserInfoVO.builder()
                .userId(1)
                .username("tianzeng")
                .mobile("12345545654")
                .address(new UserInfoVO.Address("city"))
                .build());
    }

    @GetMapping("/getUserInfo")
    public UserInfoVO getUserInfo(@RequestParam("userId") Integer userId) {
        return userList.get(userId);
    }

    @PostMapping("/upload")
    public List upload(@RequestBody UserInfoVO userInfoVO) {
        userList.put(userInfoVO.getUserId(), userInfoVO);
        return list();
    }

    @GetMapping("/list")
    public List list() {
        ArrayList<Map.Entry<Integer, UserInfoVO>> entries = new ArrayList<>(userList.entrySet());

        return entries.stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }
}