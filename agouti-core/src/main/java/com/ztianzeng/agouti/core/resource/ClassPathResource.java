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
 *
 */

package com.ztianzeng.agouti.core.resource;

import com.ztianzeng.agouti.core.AgoutiException;

import java.io.File;
import java.io.InputStream;

/**
 * 从class path中读取资源文件
 *
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-01-28 18:01
 */
public class ClassPathResource extends AbstractResource {


    private final String fileRelativePath;

    private final ClassLoader classLoader;


    public ClassPathResource(String fileRelativePath,
                             ClassLoader classLoader) {
        if (fileRelativePath == null || "".equals(fileRelativePath)) {
            throw new AgoutiException("path is empty " + fileRelativePath);
        }
        this.fileRelativePath = fileRelativePath;
        this.classLoader = classLoader;
    }

    @Override
    public InputStream read() {
        InputStream inputStream = classLoader.getResourceAsStream(fileRelativePath);
        if (inputStream == null) {
            throw new AgoutiException("Unable to load for resource: " + fileRelativePath);
        }
        return inputStream;
    }

    @Override
    public String getAbsolutePath() {
        return new File(fileRelativePath).getAbsolutePath();
    }


    @Override
    public String getFilename() {
        return fileRelativePath.substring(fileRelativePath.lastIndexOf("/") + 1);
    }

    @Override
    public String getRelativePath() {
        return fileRelativePath;
    }
}