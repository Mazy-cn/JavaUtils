function apiResUtils() {
  //通用返回方法,可添加info判断，懒得写了
  function apiResponse(res) {
    if (i18n.locale == "zh_CN") {
      if ((res.data.httpCode == 200)) {
        Message.success(res.data.msgCh);
      } else Message.error(res.data.msgCh);
    } else {
      if ((res.data.httpCode == 200)) {
        Message.success(res.data.msg);
      } else Message.error(res.data.msg);
    }
  }
  //MSG返回
  function apiMsg(res) {
    return i18n.locale == "zh_CN" ? res.data.msgCh : res.data.msg;
  }
  function selfDefinedSuccessMsg(zh_CN,en){
    if(i18n.locale == "zh_CN"){
      Message.success(zh_CN);
    }else{
      Message.success(en);
    }
  }
  function selfDefinedWarnMsg(zh_CN,en){
    if(i18n.locale == "zh_CN"){
      Message.warning(zh_CN);
    }else{
      Message.warning(en);
    }
  }
  function selfDefinedErrorMsg(zh_CN,en){
    if(i18n.locale == "zh_CN"){
      Message.error(zh_CN);
    }else{
      Message.error(en);
    }
  }
  return {
    apiResponse: apiResponse,
    apiMsg: apiMsg,
    selfDefinedSuccessMsg: selfDefinedSuccessMsg,
    selfDefinedWarnMsg: selfDefinedWarnMsg,
    selfDefinedErrorMsg: selfDefinedErrorMsg

  }
}
import i18n from '../../../../examples/locale/i18n/i18n.js';
import { Message } from 'element-ui';
export default apiResUtils;

