# Agouti

使用JSON对各个接口之间进行编排。采用配置的方式来替代传统的硬编码。

# 结构定义

## workFlow 
多个task能够聚合成为一个workFlow，workFlow对task进行一个编排操作。


## task
每一次调用都是一次task,task是执行的最小单位

## 演示
下面json定义了一个请求天气的接口，最终能够解析出最终我们想要的数据出来。

~~~

{
  "name": "agouti", // 流程名字
  "description": "agoutiTest", // 流程描述
  "outputs": { // 最终返回的结果
    "yesterdayData": "$weather.data.yesterday.date",
    "sunrise": "$weather.data.yesterday.sunrise",
    "cityInfo": "$weather.cityInfo.city"
  },
  "tasks": [
    { // API地址
      "target": "GET http://cdn.sojson.com/_city.json",
      // 任务别名
      "alias": "city",
      "method": "",
      // 任务类型
      "taskType": "URL"
    },
    {
      "target": "GET http://t.weather.sojson.com/api/weather/city/${city[25].city_code}",
      "alias": "weather",
      "method": "",
      "taskType": "URL"
    }
  ]
}

~~~

执行：

~~~
String path = "agouti/invoke.json";

AbstractResource resource = new ClassPathResource(
        path, ClassLoader.getSystemClassLoader());

Parser parser = new Parser();
WorkFlow parse = parser.parse(resource);
AgoutiEngine agoutiEngine = new AgoutiEngine();
Object invoke = agoutiEngine.invoke(parse, null);

~~~

执行结果:

~~~
output {yesterdayData=28, sunrise=07:23, cityInfo=天津市} 
~~~


# LICENSE

apache: http://www.apache.org/licenses/LICENSE-2.0