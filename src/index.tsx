import WeroomXprinter, {
  DeviceType,
  PrintFonts,
  type OnConnectStatus,
  type TSPLPrintConfig,
} from './NativeWeroomXprinter';

export { DeviceType, PrintFonts, type TSPLPrintConfig, type OnConnectStatus };

export function init() {
  return WeroomXprinter.init();
}

export function connect(
  address: string,
  type: DeviceType,
  connectResult: OnConnectStatus
): boolean {
  return WeroomXprinter.connect(address, type, connectResult);
}

export function close(): boolean {
  return WeroomXprinter.close();
}

export function printerStatus(timeout: number): Promise<number> {
  return WeroomXprinter.printerStatus(timeout);
}

/**
 * TSPL打印（Text Script Programming Language）
 * @param configArray 配置
 * @param count 份数
 * @returns 打印完返回打印状态
 */
export function TSPLprint(
  configArray: TSPLPrintConfig[],
  count: number
): Promise<number> {
  return WeroomXprinter.TSPLprint(configArray, count);
}
