import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export enum DeviceType {
  USB = 1, // USB
  BLUETOOTH = 2, // 蓝牙
  ETHERNET = 3, // 以太网
  SERIAL = 4, // 串口
  MAC = 5, // 网卡地址
}
export enum PrintFonts {
  FNT_8_12 = '1',
  FNT_12_20 = '2',
  FNT_16_24 = '3',
  FNT_24_32 = '4',
  FNT_32_48 = '5',
  FNT_14_19 = '6',
  FNT_14_25 = '7',
  FNT_21_27 = '8',
  FNT_SIMPLIFIED_CHINESE = 'TSS24.BF2',
  FNT_TRADITIONAL_CHINESE = 'TST24.BF2',
}

export type OnConnectStatus = (success: boolean) => void;

export type TSPLPrintConfig = {
  sizeInch?: { width: number; height: number };
  sizeMm?: { width: number; height: number };
  gapInch?: { m: number; n: number };
  gapMm?: { m: number; n: number };
  speed?: number;
  density?: number;
  direction?: number;
  cls?: boolean;
  box?: { x: number; y: number; w: number; h: number; thickness: number };
  bar?: { x: number; y: number; w: number; h: number };
  barcode?: {
    x: number;
    y: number;
    codeType: string;
    height: number;
    readable: number;
    rotation: number;
    narrow: number;
    wide: number;
    content: string;
  };
  text?: {
    x: number;
    y: number;
    font: string;
    rotation: number;
    xRatio: number;
    yRatio: number;
    content: string;
  };
  offsetInch?: number;
  offsetMm?: number;
  reference?: { x: number; y: number };
  qrcode?: {
    x: number;
    y: number;
    ecLevel: string;
    cellWidth: number;
    mode: string;
    rotation: number;
    data: string;
  };
  feed?: number;
  backFeed?: number;
  limitFeedMm?: number;
  limitFeedInch?: number;
  home?: boolean;
  codePage?: string;
  sound?: { level: number; interval: number };
  bitmap?: {
    x: number;
    y: number;
    mode: number;
    width: number;
    absolutePath: string;
  };
  erase?: { x: number; y: number; width: number; height: number };
  reverse?: { x: number; y: number; width: number; height: number };
  cut?: boolean;
  setPeel?: boolean;
  setTear?: boolean;
  blineInch?: { m: number; n: number };
  blineMm?: { m: number; n: number };
};

export interface Spec extends TurboModule {
  init(): void;
  connect(
    address: string,
    type: DeviceType,
    connectResult: OnConnectStatus
  ): boolean;
  close(): boolean;
  printerStatus(timeout: number): Promise<number>;
  TSPLprint(configArray: TSPLPrintConfig[], count: number): Promise<number>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('WeroomXprinter');
