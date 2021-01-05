export class GlobalUtil {

  static isNotEmpty = (val: string) => {
    return val && val.length > 0;
  }

  static isPresent = (val: any) => {
    return !!val;
  }


}
