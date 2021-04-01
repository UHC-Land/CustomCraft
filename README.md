# CustomCraft
自定义合成/熔炉配方  
此版本根据UHC-Land服务器需求进行了修改，可能不适用于所有服务器！

自定义合成配置(craft.json)示例：
```json
{
  "recipes": [
    {
      "type": 0,
      "maxCraftCount": 1,
      "input": [
        "3:0"
      ],
      "output": "58:0:1"
    },
    {
      "type": 1,
      "maxCraftCount": -1,
      "input": {
        "A": "3:0"
      },
      "shape": [
        "AAA",
        "AAA",
        "AAA"
      ],
      "output": "58:0:1"
    }
  ]
}
```
