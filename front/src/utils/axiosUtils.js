import axios from 'axios';
import PROPERTIES from '../properties';


export default class AjaxUtils {

  static prepareStandartPost(url, authorization, data, params, callback) {
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': authorization,
    };

    const axiosConfig = {
      baseURL: PROPERTIES.HOST,
      data,
      params,
      headers,
      method: "post",
      url,
      withCredentials: true,
    };

    axios(axiosConfig).then(callback);
  }

  static prepareStandartGet(url, authorization, params, callback) {
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': authorization,
    };

    const axiosConfig = {
      baseURL: PROPERTIES.HOST,
      params,
      headers,
      method: "get",
      url,
      withCredentials: true,
    };

    axios(axiosConfig).then(callback);
  }
}
