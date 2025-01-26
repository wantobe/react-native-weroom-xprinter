import React from 'react';
import { StyleSheet, View, TextInput, Button } from 'react-native';
import {
  DeviceType,
  close,
  connect as open,
  init,
  PrintFonts,
  printerStatus,
  TSPLprint,
} from 'react-native-weroom-xprinter';

export default function App() {
  const [pstate, setPstate] = React.useState<number | string>('');
  const [address, setAddress] = React.useState<string>('192.168.80.190');

  function initPrinter() {
    init();
  }

  function connect() {
    open(address, DeviceType.ETHERNET, function (code) {
      console.log('订阅连接状态：' + code);
    });
  }

  function disconnect() {
    const res = close();
    console.log('断开连接：' + res);
  }

  function print() {
    TSPLprint(
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
    );
  }
  function getPrinterStatus() {
    printerStatus(1000).then((code) => {
      console.log('打印机状态：' + code);
      setPstate(code);
    });
  }

  return (
    <View style={styles.sectionContainer}>
      <Button title="初始化" onPress={initPrinter} />
      <TextInput
        style={styles.textInput}
        onChangeText={setAddress}
        placeholder="请输入打印机地址"
        value={address}
      />
      <Button title="连接" onPress={connect} />
      <Button title="断开" onPress={disconnect} />
      <Button title="打印" onPress={print} />
      <Button title={'查询状态' + pstate} onPress={getPrinterStatus} />
    </View>
  );
}

const styles = StyleSheet.create({
  textInput: {
    margin: 10,
    height: 40,
    borderColor: 'black',
    borderWidth: 1,
    paddingLeft: 5,
    paddingRight: 5,
    borderRadius: 5,
  },
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
});
