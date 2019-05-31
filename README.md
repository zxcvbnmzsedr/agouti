# Agouti

使用JSON对各个接口之间进行编排。采用配置的方式来替代传统的硬编码。

# 结构定义

## workFlow 
多个task能够聚合成为一个workFlow，workFlow对task进行一个编排操作。


## task
每一次调用都是一次task,task是执行的最小单位

### HTTP TASK 
使用HTTP进行结果调用，支持使用lb进行负载均衡，默认采用ribbon作为负载均衡实现

可以实现HttpClient进行自定义http请求扩展。

一个简单的HTTP TASK定义
~~~
{
      "name": "uploadList",
      "type": "HTTP",
      "alias": "uploadList",
      "inputParameters": {
        "http_request": {
          "url": "http://localhost:8080/origin/upload",
          "method": "POST",
          "body": {
            "username": "${userList.response.body[0].userId}",
            "mobile": "name2",
            "address": {
              "city": "1"
            },
            "userId": "2"
          },
          "param": {
               "userId": "${uploadList.response.body[1].userId}"
          }
        }
      }
    }
~~~
name: task的名字

type: task类型

alias: task的引用名，设置了这个可以在下面的task中或者输出中对这个task的结果进行引用

inputParameters:入参类型设置

http_request: HTTP请求方式定义
    1. body请求体
    2. param请求参数
    3. url请求的url，如果url是以lb开头，会通过ribbon自动选择服务器

task执行成功后可以通过jsonpath引用:

${taskName.response.body} 响应体

${taskName.response.header} 响应头

${taskName.response.status} 响应码




## 演示
下面json定义了HTTP。

~~~

{
  "name": "getTest1",
  "description": "the post of http",
  "tasks": [
    {
      "name": "userList",
      "type": "HTTP",
      "alias": "userList",
      "inputParameters": {
        "http_request": {
          "url": "http://localhost:8080/origin/list",
          "method": "GET"
        }
      }
    },
    {
      "name": "uploadList",
      "type": "HTTP",
      "alias": "uploadList",
      "inputParameters": {
        "http_request": {
          "url": "http://localhost:8080/origin/upload",
          "method": "POST",
          "body": {
            "username": "${userList.response.body[0].userId}",
            "mobile": "name2",
            "address": {
              "city": "1"
            },
            "userId": "2"
          }
        }
      }
    },
    {
      "name": "userOne",
      "type": "HTTP",
      "alias": "user",
      "inputParameters": {
        "http_request": {
          "url": "http://localhost:8080/origin/getUserInfo",
          "method": "GET",
          "param": {
            "userId": "${uploadList.response.body[1].userId}"
          }
        }
      }
    }
  ],
  "outputParameters": {
    "username": "${user.response.body.username}",
    "address": "${user.response.body.address}",
    "mobile": "${user.response.body.mobile}",
    "city": "${user.response.body.address.city}",
    "userId": "${user.response.body.userId}"
  }
}

~~~

执行：

~~~
BaseExecutor defaultExecutor = new BaseExecutor();
WorkFlowDef workFlowDef = WorkFlowParse.fromResource("getTest1.json");

WorkFlow workFlow = defaultExecutor.startWorkFlow(workFlowDef, null);
workFlow.getOutputs();

~~~

执行结果:

~~~
{
    "address": {
        "city": "1"
    },
    "city": "1",
    "mobile": "name2",
    "userId": 2,
    "username": "1"
}
~~~


# LICENSE

apache: http://www.apache.org/licenses/LICENSE-2.0