# react-native-weroom-xprinter

芯烨热敏打印机SDK封装，只有实现了安卓平台打印功能，其他功能请自行实现

## Installation

```sh
npm install react-native-weroom-xprinter
```

## Usage

```js
import { 
  DeviceType, //设备类型, usb/网络/蓝牙...
  init, // 初始化
  connect, // 连接
  close, // 断开连接
  PrintFonts, // 字体类型 详情参考芯烨官方sdk文档
  printerStatus, // 打印机状态 详情参考芯烨官方sdk文档
  TSPLprint, // 打印（TSPL指令）
  TSPLPrintConfig, // 打印配置
  OnConnectStatus   // 连接状态回调
} from 'react-native-weroom-xprinter';

// 注意：连接之前务必先调用init方法，否则会报错
// 感谢TurboModule设计实现按需初始化，再也不用拖着native包袱了(旧版哪怕没有使用插件，一旦引入必须默认初始化占用资源)

/**
 * 打印方法说明
 * 第一参数类型TSPLPrintConfig[]
 * 第二参数打印份数
 * 下面的例子将原SDK链式调用【e.g. printer.cls().sizeMm(30, 20).text(0, 0, PrintFonts.FNT_SIMPLIFIED_CHINESE, 0, 1, 1, '你好吖blah...').print(1)】改成json格式, 
 * 详情参考芯烨官方sdk文档
 */
TSPLprint(TSPLprint(
  [
    { cls: true },
    {
      sizeMm: { width: 30, height: 20 },
      reference: { x: 0, y: 0 },
      text: {
        x: 0,
        y: 0,
        font: PrintFonts.FNT_SIMPLIFIED_CHINESE,
        rotation: 0,
        xRatio: 1,
        yRatio: 1,
        content: '你好吖blah...',
      },
    },
  ],
  1
))
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
