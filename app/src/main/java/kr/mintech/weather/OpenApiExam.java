package kr.mintech.weather;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import kr.mintech.weather.api.APIRequest;
import kr.mintech.weather.common.PlanetXSDKConstants.CONTENT_TYPE;
import kr.mintech.weather.common.PlanetXSDKConstants.HttpMethod;
import kr.mintech.weather.common.PlanetXSDKException;
import kr.mintech.weather.common.RequestBundle;
import kr.mintech.weather.common.RequestListener;
import kr.mintech.weather.common.ResponseMessage;


public class OpenApiExam extends Activity implements OnClickListener {

  // 화면 UI
  Button btnClear;
  Button btnSend;
  TextView tvResult;

  // 통신에 사용되는 변수
  APIRequest api;
  RequestBundle requestBundle;
// http://apis.skplanetx.com/melon/newreleases/songs?version={version}&page={page}&count={count}
// String URL = "http://apis.skplanetx.com/melon/newreleases/songs";
  String URL = "http://apis.skplanetx.com/weather/dust";
  Map<String, Object> param;

  // 통신 결과 수신 및 화면 반영
  String hndResult = "";
  Handler msgHandler = new Handler(){
    public void dispatchMessage(Message msg) {
      tvResult.setText(hndResult);
    };
  };

  // Activity Start
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.openapiexam);

    initUI();
  }

  // 화면 구성요소 초기화
  public void initUI() {
    btnClear = (Button) findViewById(R.id.btnClear);
    btnSend = (Button) findViewById(R.id.btnSend);
    tvResult = (TextView) findViewById(R.id.tvResult);

    // 리스너 설정
    btnSend.setOnClickListener(this);
    btnClear.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();

    switch (id) {
      case R.id.btnClear: {
        // 결과 화면 초기화
        tvResult.setText("");
        break;
      }
      case R.id.btnSend: {
        // Open API 호출
        commWithOpenApiServer();
        break;
      }
    }
  }

  public void commWithOpenApiServer() {
    // AppKey 설정
    api = new APIRequest();
    APIRequest.setAppKey("8aa2f9e4-0120-333f-add1-a714d569a1e9");

    // url에 삽입되는 파라미터 설정
    param = new HashMap<String, Object>();
    param.put("version", "1");
    param.put("lat", "37");
    param.put("lon", "122");

    // 호출시 사용될 값 설정
    requestBundle = new RequestBundle();
    requestBundle.setUrl(URL);
    requestBundle.setParameters(param);
    requestBundle.setHttpMethod(HttpMethod.GET);
    requestBundle.setResponseType(CONTENT_TYPE.JSON);

    try {
      // 비동기 호출
      api.request(requestBundle, reqListener);
    } catch (PlanetXSDKException e) {
      e.printStackTrace();
    }
  }

  // Opean API 통신시 사용하는 비동기 Listener
  RequestListener reqListener = new RequestListener() {
    @Override
    public void onPlanetSDKException(PlanetXSDKException e) {
      hndResult = e.toString();
      msgHandler.sendEmptyMessage(0);
    }

    @Override
    public void onComplete(ResponseMessage result) {
      // 응답을 받아 메시지 핸들러에 알려준다.
      hndResult = result.getStatusCode() + "\n" + result.toString();
      msgHandler.sendEmptyMessage(0);
    }
  };
}
